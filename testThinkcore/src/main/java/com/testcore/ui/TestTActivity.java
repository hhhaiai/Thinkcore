package com.testcore.ui;

import com.testcore.R;
import com.thinkcore.activity.TAppActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TestTActivity extends TAppActivity implements OnClickListener {
	private String TAG = TestTActivity.class.getCanonicalName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.ac_home);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

//		final View view = View.inflate(this, R.layout.splash, null);
//		setContentView(view);
//		// 渐变展示启动�?
//		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
//		aa.setDuration(5000);
//		view.startAnimation(aa);
//		aa.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				finish();
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//		});
//
//		testComparatorButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
//		if (testComparatorButton == arg0) {
//			Log.i("", "");
//			UIBroadcast.sentEvent(mContext, 1001, 1002, "");
//		}
	}
}
