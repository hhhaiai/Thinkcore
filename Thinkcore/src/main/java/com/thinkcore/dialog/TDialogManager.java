package com.thinkcore.dialog;

import java.util.LinkedList;

import android.app.ProgressDialog;
import android.content.Context;

public class TDialogManager {
	private static LinkedList<TDialog> mProgressDialogs = new LinkedList<TDialog>();

	// 隐藏进度条框
	public static void hideProgressDialog(Context context) {
		TDialog progressDialog = hasProgressDialog(context);
		if (progressDialog != null) {
			progressDialog.dismiss();
			mProgressDialogs.remove(progressDialog);
			progressDialog = null;
		}
	}

	public static void showProgressDialog(Context context, CharSequence title,
			CharSequence message) {
		showProgressDialog(context, title, message, true);
	}

	public static void showProgressDialog(Context context, CharSequence title,
			CharSequence message, boolean cancelAble) { // 显示
		TDialog progressDialog = hasProgressDialog(context);

		if (progressDialog == null) {
			progressDialog = new TDialog(context);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
			progressDialog.setIndeterminate(false);// 设置进度条是否为不明确
			mProgressDialogs.add(progressDialog);
		}

		progressDialog.setCancelable(cancelAble); // 是否可取消
		progressDialog.setTitle(title);// 设置标题
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	private static TDialog hasProgressDialog(Context context) { // 是否存在
		for (TDialog progressDialog : mProgressDialogs) {
			if (progressDialog.getContextByActivity() == context)
				return progressDialog;
		}

		return null;
	}
}
