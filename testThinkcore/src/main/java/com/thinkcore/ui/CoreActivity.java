package com.thinkcore.ui;

import android.os.Bundle;
import com.thinkcore.activity.TActivity;
import com.thinkcore.event.TEvent;
import com.thinkcore.eventbus.TEventBus;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


//界面
public abstract class CoreActivity extends TActivity {
    private String TAG = CoreActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TEventBus.getDefault().register(this);
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        TEventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void processEvent(TEvent event) {
    }
}
