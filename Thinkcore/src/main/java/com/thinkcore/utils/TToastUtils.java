package com.thinkcore.utils;

import com.thinkcore.TApplication;
import com.thinkcore.utils.log.TLog;

import android.content.Context;
import android.widget.Toast;

/**
 * Responsible for sending Toasts under all circumstances.
 * <p/>
 */
public final class TToastUtils {

	/**
	 * Sends a Toast and ensures that any Exception thrown during sending is
	 * handled.
	 * 
	 * @param context
	 *            Application context.
	 * @param toastResourceId
	 *            Id of the resource to send as the Toast message.
	 * @param toastLength
	 *            Length of the Toast.
	 */
	public static void makeText(Context context, int toastResourceId,
			int toastLength) {
		String content = TApplication.getResString(toastResourceId);
		if (TStringUtils.isEmpty(content))
			return;

		try {
			Toast.makeText(context, content, toastLength).show();
		} catch (RuntimeException e) {
			TLog.e(TToastUtils.class.getSimpleName(),
					"Could not send crash Toast" + e.getMessage());
		}
	}

	public static void makeText(Context context, String content) {
		if (TStringUtils.isEmpty(content))
			return;

		try {
			Toast.makeText(context, content, Toast.LENGTH_LONG).show();
		} catch (RuntimeException e) {
			TLog.e(TToastUtils.class.getSimpleName(),
					"Could not send crash Toast" + e.getMessage());
		}
	}

	public static void makeText(int toastResourceId, int toastLength) {
		String content = TApplication.getResString(toastResourceId);
		if (TStringUtils.isEmpty(content))
			return;
		try {
			Toast.makeText(TApplication.getInstance().getApplicationContext(),
					content, toastLength).show();
		} catch (RuntimeException e) {
			TLog.e(TToastUtils.class.getSimpleName(),
					"Could not send crash Toast" + e.getMessage());
		}
	}

	public static void makeText(String content, int toastLength) {
		if (TStringUtils.isEmpty(content))
			return;

		try {
			Toast.makeText(TApplication.getInstance().getApplicationContext(),
					content, toastLength).show();
		} catch (RuntimeException e) {
			TLog.e(TToastUtils.class.getSimpleName(),
					"Could not send crash Toast" + e.getMessage());
		}
	}

	public static void makeText(String content) {
		if (TStringUtils.isEmpty(content))
			return;
		try {
			Toast.makeText(TApplication.getInstance().getApplicationContext(),
					content, Toast.LENGTH_LONG).show();
		} catch (RuntimeException e) {
			TLog.e(TToastUtils.class.getSimpleName(),
					"Could not send crash Toast" + e.getMessage());
		}
	}
}
