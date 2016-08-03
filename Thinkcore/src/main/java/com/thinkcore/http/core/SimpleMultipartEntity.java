/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

/*
 This code is taken from Rafael Sanches' blog.
 http://blog.rafaelsanches.com/2011/01/29/upload-using-multipart-post-using-httpclient-in-android/
 */

package com.thinkcore.http.core;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import com.thinkcore.http.TAsyncHttpClient;
import com.thinkcore.utils.log.TLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simplified multipart entity mainly used for sending one or more files.
 */
class SimpleMultipartEntity implements HttpEntity {

	private static final String TAG = SimpleMultipartEntity.class
			.getSimpleName();

	private static final String STR_CR_LF = "\r\n";
	private static final byte[] CR_LF = STR_CR_LF.getBytes();
	private static final byte[] TRANSFER_ENCODING_BINARY = ("Content-Transfer-Encoding: binary" + STR_CR_LF)
			.getBytes();

	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	private final String mBoundary;
	private final byte[] mBoundaryLine;
	private final byte[] mBoundaryEnd;
	private boolean mIsRepeatable;

	private final List<FilePart> mFileParts = new ArrayList();

	// The buffer we use for building the message excluding files and the last
	// boundary
	private final ByteArrayOutputStream mOut = new ByteArrayOutputStream();

	private final ResponseHandlerInterface mProgressHandler;

	private int mBytesWritten;

	private int mTotalSize;

	public SimpleMultipartEntity(ResponseHandlerInterface progressHandler) {
		final StringBuilder buf = new StringBuilder();
		final Random rand = new Random();
		for (int i = 0; i < 30; i++) {
			buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}

		mBoundary = buf.toString();
		mBoundaryLine = ("--" + mBoundary + STR_CR_LF).getBytes();
		mBoundaryEnd = ("--" + mBoundary + "--" + STR_CR_LF).getBytes();

		this.mProgressHandler = progressHandler;
	}

	public void addPart(String key, String value, String contentType) {
		try {
			mOut.write(mBoundaryLine);
			mOut.write(createContentDisposition(key));
			mOut.write(createContentType(contentType));
			mOut.write(CR_LF);
			mOut.write(value.getBytes());
			mOut.write(CR_LF);
		} catch (final IOException e) {
			// Shall not happen on ByteArrayOutputStream
			TLog.e(TAG,
					"addPart ByteArrayOutputStream exception" + e.getMessage());
		}
	}

	public void addPart(String key, String value) {
		addPart(key, value, "text/plain; charset=UTF-8");
	}

	public void addPart(String key, File file) {
		addPart(key, file, null);
	}

	public void addPart(String key, File file, String type) {
		mFileParts.add(new FilePart(key, file, normalizeContentType(type)));
	}

	public void addPart(String key, String streamName, InputStream inputStream,
			String type) throws IOException {

		mOut.write(mBoundaryLine);

		// Headers
		mOut.write(createContentDisposition(key, streamName));
		mOut.write(createContentType(type));
		mOut.write(TRANSFER_ENCODING_BINARY);
		mOut.write(CR_LF);

		// Stream (file)
		final byte[] tmp = new byte[4096];
		int l;
		while ((l = inputStream.read(tmp)) != -1) {
			mOut.write(tmp, 0, l);
		}

		mOut.write(CR_LF);
		mOut.flush();

		TAsyncHttpClient.silentCloseOutputStream(mOut);
	}

	private String normalizeContentType(String type) {
		return type == null ? RequestParams.APPLICATION_OCTET_STREAM : type;
	}

	private byte[] createContentType(String type) {
		String result = "Content-Type: " + normalizeContentType(type)
				+ STR_CR_LF;
		return result.getBytes();
	}

	private byte[] createContentDisposition(String key) {
		return ("Content-Disposition: form-data; name=\"" + key + "\"" + STR_CR_LF)
				.getBytes();
	}

	private byte[] createContentDisposition(String key, String fileName) {
		return ("Content-Disposition: form-data; name=\"" + key
				+ "\"; filename=\"" + fileName + "\"" + STR_CR_LF).getBytes();
	}

	private void updateProgress(int count) {
		mBytesWritten += count;
		mProgressHandler.sendProgressMessage(mBytesWritten, mTotalSize);
	}

	private class FilePart {
		public File file;
		public byte[] header;

		public FilePart(String key, File file, String type) {
			header = createHeader(key, file.getName(), type);
			this.file = file;
		}

		private byte[] createHeader(String key, String filename, String type) {
			ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
			try {
				headerStream.write(mBoundaryLine);

				// Headers
				headerStream.write(createContentDisposition(key, filename));
				headerStream.write(createContentType(type));
				headerStream.write(TRANSFER_ENCODING_BINARY);
				headerStream.write(CR_LF);
			} catch (IOException e) {
				// Can't happen on ByteArrayOutputStream
				TLog.e(TAG,
						"createHeader ByteArrayOutputStream exception"
								+ e.getMessage());
			}
			return headerStream.toByteArray();
		}

		public long getTotalLength() {
			long streamLength = file.length() + CR_LF.length;
			return header.length + streamLength;
		}

		public void writeTo(OutputStream out) throws IOException {
			out.write(header);
			updateProgress(header.length);

			FileInputStream inputStream = new FileInputStream(file);
			final byte[] tmp = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(tmp)) != -1) {
				out.write(tmp, 0, bytesRead);
				updateProgress(bytesRead);
			}
			out.write(CR_LF);
			updateProgress(CR_LF.length);
			out.flush();
			TAsyncHttpClient.silentCloseInputStream(inputStream);
		}
	}

	// The following methods are from the HttpEntity interface

	@Override
	public long getContentLength() {
		long contentLen = mOut.size();
		for (FilePart filePart : mFileParts) {
			long len = filePart.getTotalLength();
			if (len < 0) {
				return -1; // Should normally not happen
			}
			contentLen += len;
		}
		contentLen += mBoundaryEnd.length;
		return contentLen;
	}

	@Override
	public Header getContentType() {
		return new BasicHeader("Content-Type", "multipart/form-data; boundary="
				+ mBoundary);
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	public void setIsRepeatable(boolean isRepeatable) {
		this.mIsRepeatable = isRepeatable;
	}

	@Override
	public boolean isRepeatable() {
		return mIsRepeatable;
	}

	@Override
	public boolean isStreaming() {
		return false;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		mBytesWritten = 0;
		mTotalSize = (int) getContentLength();
		mOut.writeTo(outstream);
		updateProgress(mOut.size());

		for (FilePart filePart : mFileParts) {
			filePart.writeTo(outstream);
		}
		outstream.write(mBoundaryEnd);
		updateProgress(mBoundaryEnd.length);
	}

	@Override
	public Header getContentEncoding() {
		return null;
	}

	@Override
	public void consumeContent() throws IOException,
			UnsupportedOperationException {
		if (isStreaming()) {
			throw new UnsupportedOperationException(
					"Streaming entity does not implement #consumeContent()");
		}
	}

	@Override
	public InputStream getContent() throws IOException,
			UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"getContent() is not supported. Use writeTo() instead.");
	}
}