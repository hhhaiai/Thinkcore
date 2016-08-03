package com.thinkcore.http.core;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import com.thinkcore.TApplication;
import com.thinkcore.http.TAsyncHttpClient;
import com.thinkcore.storage.TFilePath;
import com.thinkcore.utils.log.TLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class FileAsyncHttpResponseHandler extends
		AsyncHttpResponseHandler {

	protected final File mFile;
	private static final String TAG = FileAsyncHttpResponseHandler.class
			.getSimpleName();

	/**
	 * Obtains new FileAsyncHttpResponseHandler and stores response in passed
	 * file
	 * 
	 * @param file
	 *            File to store response within, must not be null
	 */
	public FileAsyncHttpResponseHandler(File file) {
		super();
		assert (file != null);
		this.mFile = file;
	}

	/**
	 * Obtains new FileAsyncHttpResponseHandler against context with target
	 * being temporary file
	 * 
	 * @param context
	 *            Context, must not be null
	 */
	public FileAsyncHttpResponseHandler(String fileName) {
		super();
		this.mFile = getTemporaryFile(fileName);
	}

	/**
	 * Attempts to delete file with stored response
	 * 
	 * @return false if the file does not exist or is null, true if it was
	 *         successfully deleted
	 */
	public boolean deleteTargetFile() {
		return getTargetFile() != null && getTargetFile().delete();
	}

	/**
	 * Used when there is no file to be used when calling constructor
	 * 
	 * @param context
	 *            Context, must not be null
	 * @return temporary file or null if creating file failed
	 */
	protected File getTemporaryFile(String fileName) {
		// assert (TFilePathManager.getInstance() != null);
		try {
			return new File(TFilePath.getDownloadDirectory(TApplication
					.getInstance()), fileName);
		} catch (Throwable t) {
			TLog.e(TAG, "Cannot create temporary file" + t.getMessage());
		}
		return null;
	}

	/**
	 * Retrieves File object in which the response is stored
	 * 
	 * @return File file in which the response is stored
	 */
	protected File getTargetFile() {
		assert (mFile != null);
		return mFile;
	}

	@Override
	public final void onFailure(int statusCode, Header[] headers,
			byte[] responseBytes, Throwable throwable) {
		onFailure(statusCode, headers, throwable, getTargetFile());
	}

	/**
	 * Method to be overriden, receives as much of file as possible Called when
	 * the file is considered failure or if there is error when retrieving file
	 * 
	 * @param statusCode
	 *            http file status line
	 * @param headers
	 *            file http headers if any
	 * @param throwable
	 *            returned throwable
	 * @param file
	 *            file in which the file is stored
	 */
	public abstract void onFailure(int statusCode, Header[] headers,
			Throwable throwable, File file);

	@Override
	public final void onSuccess(int statusCode, Header[] headers,
			byte[] responseBytes) {
		onSuccess(statusCode, headers, getTargetFile());
	}

	/**
	 * Method to be overriden, receives as much of response as possible
	 * 
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response http headers if any
	 * @param file
	 *            file in which the response is stored
	 */
	public abstract void onSuccess(int statusCode, Header[] headers, File file);

	@Override
	protected byte[] getResponseData(HttpEntity entity) throws IOException {
		if (entity != null) {
			InputStream instream = entity.getContent();
			long contentLength = entity.getContentLength();
			if (contentLength == 0) {
			}
			FileOutputStream buffer = new FileOutputStream(getTargetFile());
			if (instream != null) {
				try {
					byte[] tmp = new byte[BUFFER_SIZE];
					int l, count = 0;
					// do not send messages if request has been cancelled
					while ((l = instream.read(tmp)) != -1
							&& !Thread.currentThread().isInterrupted()) {
						count += l;
						buffer.write(tmp, 0, l);
						sendProgressMessage(count, (int) contentLength);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					TAsyncHttpClient.silentCloseInputStream(instream);
					buffer.flush();
					TAsyncHttpClient.silentCloseOutputStream(buffer);
				}
			}
		}
		return null;
	}

}
