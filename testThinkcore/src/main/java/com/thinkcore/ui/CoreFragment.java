package com.thinkcore.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinkcore.activity.TFragment;
import com.thinkcore.event.TEvent;
import com.thinkcore.eventbus.TEventBus;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class CoreFragment extends TFragment {
	protected View mThis;

	@Override
	public void onCreate(Bundle savedInstanceState) {// 当Activity中的onCreate方法执行完后调用
		super.onCreate(savedInstanceState);
		TEventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// onCreate之后 显示的组件（为Fragment加载布局时调用）
		return super.onCreateView(inflater, container, savedInstanceState);// 父类返回null
	}

	@Override
	public void onAttach(Activity activity) { // onCreate之前触发（Fragment和Activity建立关联的时候调用）
		super.onAttach(activity);
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
		TEventBus.getDefault().unregister(this);
	}

	@Override
	public void onDetach() {// Fragment和Activity解除关联的时候调用
		super.onDetach();
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void processEvent(TEvent event) {
	}
}
