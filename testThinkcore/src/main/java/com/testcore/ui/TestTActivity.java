package com.testcore.ui;

import com.testcore.R;
import com.testcore.ui.switchButton.MainActivity;
import com.testcore.utils.NdkJniUtils;
import com.testcore.utils.ThemeUtils;
import com.thinkcore.ui.CoreAppActivity;
import com.thinkcore.utils.TActivityUtils;
import com.thinkcore.utils.log.TLog;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TestTActivity extends CoreAppActivity implements OnClickListener {
	private String TAG = TestTActivity.class.getCanonicalName();
	private static boolean mTheme = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(mTheme ? R.style.AppThemeLight : R.style.AppThemeDark);

		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_test);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		findViewById(R.id.Button_crash).setOnClickListener(this);
		findViewById(R.id.Button_http).setOnClickListener(this);
		findViewById(R.id.Button_download).setOnClickListener(this);
		findViewById(R.id.Button_dialog).setOnClickListener(this);
		findViewById(R.id.Button_theme).setOnClickListener(this);
		findViewById(R.id.Button_ripple).setOnClickListener(this);
		findViewById(R.id.Button_BetterSpinner).setOnClickListener(this);
		findViewById(R.id.Button_Switch).setOnClickListener(this);
		findViewById(R.id.Button_Refresh).setOnClickListener(this);
		findViewById(R.id.Button_Snackbar).setOnClickListener(this);
		findViewById(R.id.Button_RippleView).setOnClickListener(this);
		findViewById(R.id.Button_reveallayout).setOnClickListener(this);
		findViewById(R.id.Button_Discreteseekbar).setOnClickListener(this);
		findViewById(R.id.Button_compent).setOnClickListener(this);
		findViewById(R.id.Button_fd).setOnClickListener(this);
		findViewById(R.id.Button_setting).setOnClickListener(this);
		findViewById(R.id.Button_rangerbar).setOnClickListener(this);
		findViewById(R.id.Button_scroolview).setOnClickListener(this);

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
		}else if(arg0.getId() == R.id.Button_download){
			TActivityUtils.jumpToActivity(this,DownloadActivity.class);
		}else if(arg0.getId() == R.id.Button_dialog){
			TActivityUtils.jumpToActivity(this,DialogActivity.class);
		}else if(arg0.getId() == R.id.Button_theme){
			mTheme = !mTheme;

			ThemeUtils.recreateActivity(this);
		}else if(arg0.getId() == R.id.Button_ripple){
			TActivityUtils.jumpToActivity(this,RippleActivity.class);
		}else if(arg0.getId() == R.id.Button_BetterSpinner){
			TActivityUtils.jumpToActivity(this,BetterSpinnerActivity.class);
		}else if(arg0.getId() == R.id.Button_Switch){
			TActivityUtils.jumpToActivity(this,MainActivity.class);
		}else if(arg0.getId() == R.id.Button_Refresh){
			TActivityUtils.jumpToActivity(this,SwipyRefreshActivity.class);
		}else if(arg0.getId() == R.id.Button_Snackbar){
			TActivityUtils.jumpToActivity(this,SnackbarActivity.class);
		}else if(arg0.getId() == R.id.Button_RippleView){
			TActivityUtils.jumpToActivity(this,RippleViewActivity.class);
		}else if(arg0.getId() == R.id.Button_reveallayout){
			TActivityUtils.jumpToActivity(this, com.testcore.ui.reveallayout.MainActivity.class);
		}else if(arg0.getId() == R.id.Button_Discreteseekbar){
			TActivityUtils.jumpToActivity(this, DiscreteseekbarActivity.class);
		}else if(arg0.getId() == R.id.Button_compent){
			TActivityUtils.jumpToActivity(this, com.testcore.ui.materialdesigndemo.MainActivity.class);
		}else if(arg0.getId() == R.id.Button_fd){
			TActivityUtils.jumpToActivity(this, com.testcore.ui.fd.MainActivity.class);
		}else if(arg0.getId() == R.id.Button_setting){
			TActivityUtils.jumpToActivity(this, com.testcore.ui.SettingsActivity.class);
		}else if(arg0.getId() == R.id.Button_rangerbar){
			TActivityUtils.jumpToActivity(this, com.testcore.ui.rangebarsample.MainActivity.class);
		}else if(arg0.getId() == R.id.Button_scroolview){
			TActivityUtils.jumpToActivity(this, com.testcore.ui.observablescroll.ViewPagerTabActivity.class);

			NdkJniUtils jni = new NdkJniUtils();
			String tet = jni.getCLanguageString();
			TLog.i(this,tet);
		}
	}
}
