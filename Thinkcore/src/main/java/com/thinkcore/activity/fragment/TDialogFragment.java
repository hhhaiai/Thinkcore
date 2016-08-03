package com.thinkcore.activity.fragment;

import com.thinkcore.TApplication;
import com.thinkcore.activity.TActivity;
import com.thinkcore.utils.THandler;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class TDialogFragment extends DialogFragment {
	protected View mThis;
	private TActivity mTActivity;
	private THandler<DialogFragment> mTHandler;

	public TActivity getTActivity() {
		return mTActivity;
	}

	@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof TActivity)) {
			throw new IllegalStateException(getClass().getSimpleName()
					+ " must be attached to a SherlockFragmentActivity.");
		}
		mTActivity = (TActivity) activity;
		super.onAttach(activity);

		mTHandler = new THandler<DialogFragment>(this) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if (getOwner() == null || getOwner().getActivity() == null)
					return;

				Bundle data = msg.getData();
				handleEvent(msg.what, data.getString("core" + 0),
						data.getString("core" + 1), data.getString("core" + 2),
						data.getString("core" + 3), data.getString("core" + 4));
			}
		};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mTHandler = null;
	}

	@Override
	public void onDetach() {
		mTActivity = null;
		super.onDetach();
	}

	public static String getResString(int id) {
		return TApplication.getInstance().getString(id);
	}

	protected void handleEvent(int id, String... params) {
	}

	public void sentPostEvent(final int second, final int id,
			final String... params) {
		new AsyncTask<String, Integer, Boolean>() {

			@Override
			protected Boolean doInBackground(String... params) {
				try {
					Thread.sleep(second * 1000);
				} catch (Exception e) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				sentEvent(id, params);
			}
		}.execute("");
	}

	public void sentEvent(int id, String... params) {
		if (mTHandler == null)
			return;
		Message message = new Message();
		if (params != null && params.length > 0) {
			Bundle bundle = new Bundle();
			for (int i = 0; i < params.length; i++) {
				bundle.putString("core" + i, params[i]);
			}
			message.setData(bundle);
		}
		message.what = id;
		mTHandler.sendMessage(message);
	}
}
