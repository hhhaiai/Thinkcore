package com.testcore.ui;

import com.thinkcore.activity.TActivity;
import com.thinkcore.activity.annotation.TInjectView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;

public class TestTActivity extends TActivity implements OnClickListener {
	private String TAG = TestTActivity.class.getCanonicalName();
//	@TInjectView(id = R.id.Button_test)
//	private Button testComparatorButton;
//	@TInjectView(id = R.id.TextView_test)
//	private TextView showViewTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = new View(this);
		setContentView(view);
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
