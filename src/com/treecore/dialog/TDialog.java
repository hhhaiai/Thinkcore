package com.treecore.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class TDialog extends ProgressDialog {
	private Context mContext;

	// private Dialog mDialog;

	public TDialog(Context context) {
		super(context);
		mContext = context;
	}

	public TDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public Context getContextByActivity() {
		return mContext;
	}
}
