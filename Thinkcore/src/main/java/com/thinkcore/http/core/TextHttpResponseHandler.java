package com.thinkcore.http.core;

import android.util.Log;

import org.apache.http.Header;

import com.thinkcore.http.TAsyncHttpClient;
import com.thinkcore.utils.log.TLog;

import java.io.UnsupportedEncodingException;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link TAsyncHttpClient}. The
 * {@link #onSuccess(int, Header[], String)} method is designed
 * to be anonymously overridden with your own response handling code.
 * <p>
 * &nbsp;
 * </p>
 * Additionally, you can override the
 * {@link #onFailure(int, Header[], String, Throwable)},
 * {@link #onStart()}, and {@link #onFinish()} methods as required.
 * <p>
 * &nbsp;
 * </p>
 * For example:
 * <p>
 * &nbsp;
 * </p>
 * 
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get(&quot;http://www.google.com&quot;, new TextHttpResponseHandler() {
 * 	&#064;Override
 * 	public void onStart() {
 * 		// Initiated the request
 * 	}
 * 
 * 	&#064;Override
 * 	public void onSuccess(String responseBody) {
 * 		// Successfully got a response
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFailure(String responseBody, Throwable e) {
 * 		// Response failed :(
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFinish() {
 * 		// Completed the request (either success or failure)
 * 	}
 * });
 * </pre>
 */
public abstract class TextHttpResponseHandler extends AsyncHttpResponseHandler {
	private static final String TAG = TextHttpResponseHandler.class
			.getSimpleName();

	/**
	 * Creates new instance with default UTF-8 encoding
	 */
	public TextHttpResponseHandler() {
		this(DEFAULT_CHARSET);
	}

	/**
	 * Creates new instance with given string encoding
	 * 
	 * @param encoding
	 *            String encoding, see {@link #setCharset(String)}
	 */
	public TextHttpResponseHandler(String encoding) {
		super();
		setCharset(encoding);
	}

	/**
	 * Called when request fails
	 * 
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response headers if any
	 * @param responseString
	 *            string response of given charset
	 * @param throwable
	 *            throwable returned when processing request
	 */
	public abstract void onFailure(int statusCode, Header[] headers,
			String responseString, Throwable throwable);

	/**
	 * Called when request succeeds
	 * 
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response headers if any
	 * @param responseString
	 *            string response of given charset
	 */
	public abstract void onSuccess(int statusCode, Header[] headers,
			String responseString);

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
		onSuccess(statusCode, headers,
				getResponseString(responseBytes, getCharset()));
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			byte[] responseBytes, Throwable throwable) {
		onFailure(statusCode, headers,
				getResponseString(responseBytes, getCharset()), throwable);
	}

	/**
	 * Attempts to encode response bytes as string of set encoding
	 * 
	 * @param charset
	 *            charset to create string with
	 * @param stringBytes
	 *            response bytes
	 * @return String of set encoding or null
	 */
	public static String getResponseString(byte[] stringBytes, String charset) {
		try {
			return stringBytes == null ? null
					: new String(stringBytes, charset);
		} catch (UnsupportedEncodingException e) {
			TLog.e(TAG, "Encoding response into string failed" + e.getMessage());
			return null;
		}
	}

}
