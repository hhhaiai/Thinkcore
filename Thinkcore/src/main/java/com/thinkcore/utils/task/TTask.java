package com.thinkcore.utils.task;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.thinkcore.utils.TAndroidVersionUtils;

import android.os.AsyncTask;
import android.util.Log;

public class TTask extends AsyncTask {
    private final static String TAG = TTask.class.getCanonicalName();
    private static ExecutorService mExecutorService;

    private static int Update = 1;

    static {
        // SINGLE_TASK_EXECUTOR = (ExecutorService) Executors
        // .newSingleThreadExecutor();
        mExecutorService = (ExecutorService) Executors.newFixedThreadPool(30);// 30个线程
        // mExecutorService = (ExecutorService) Executors.newCachedThreadPool();
    }

    ;


    public TTask() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    public void execute(Objects... params) {
        if (TAndroidVersionUtils.hasHoneycomb()) {
            super.executeOnExecutor(mExecutorService, params);
        } else {
            super.execute(params);
        }
    }
}
