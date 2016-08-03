package com.thinkcore.activity.fragment;

import com.thinkcore.TApplication;
import com.thinkcore.utils.THandler;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TFragment extends Fragment {
	protected View mThis;
	private THandler<TFragment> mTHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {// 当Activity中的onCreate方法执行完后调用
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// onCreate之后 显示的组件（为Fragment加载布局时调用）
		return super.onCreateView(inflater, container, savedInstanceState);// 父类返回null
	}

	@Override
	public void onAttach(Activity activity) { // onCreate之前触发（Fragment和Activity建立关联的时候调用）
		super.onAttach(activity);

		mTHandler = new THandler<TFragment>(this) {

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
	public void onActivityCreated(Bundle savedInstanceState) {// onCreateView 之后
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {// Fragment中的布局被移除时调用
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mTHandler = null;
	}

	@Override
	public void onDetach() {// Fragment和Activity解除关联的时候调用
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
