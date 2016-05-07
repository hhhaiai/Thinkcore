package com.treecore.activity.fragment;

import com.treecore.TApplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

public class TListFragment extends ListFragment {
	protected View mThis;

	@Override
	public void onAttach(Activity activity) {// 当该Fragment被添加、显示到Activity时，回调该方法
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDetach() {// 当该Fragment从它所属的Activity中被删除时回调该方法
		super.onDetach();
	}

	public static String getResString(int id) {
		return TApplication.getInstance().getString(id);
	}
}
