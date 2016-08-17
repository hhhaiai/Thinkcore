package com.testcore.ui;

import com.testcore.R;
import com.thinkcore.activity.TAppActivity;
import com.thinkcore.ui.CoreAppActivity;
import com.thinkcore.utils.TActivityUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TestTActivity extends CoreAppActivity implements OnClickListener {
	private String TAG = TestTActivity.class.getCanonicalName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_test);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		findViewById(R.id.Button_crash).setOnClickListener(this);
		findViewById(R.id.Button_http).setOnClickListener(this);


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
		if(arg0.getId() == R.id.Button_crash){
			int test = 10/0;
		}else if(arg0.getId() == R.id.Button_http){
			TActivityUtils.jumpToActivity(this,HttpActivity.class);
		}
	}
}
