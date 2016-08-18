package com.testcore.ui;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.golshadi.majid.core.DownloadManagerPro;
import com.golshadi.majid.report.listener.DownloadManagerListener;
import com.testcore.R;
import com.thinkcore.ui.CoreAppActivity;

public class DownloadActivity extends CoreAppActivity implements View.OnClickListener,DownloadManagerListener {
    DownloadManagerPro mDownloadManagerPro;


    private final static String DATABASE_NAME = "com.banketree.downloadManagerPro";
    private final static int DATABASE_VERSION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        findViewById(R.id.Button_start_1).setOnClickListener(this);
        findViewById(R.id.Button_start_2).setOnClickListener(this);
        findViewById(R.id.Button_del_1).setOnClickListener(this);
        findViewById(R.id.Button_del_2).setOnClickListener(this);
        findViewById(R.id.Button_pause_1).setOnClickListener(this);
        findViewById(R.id.Button_pause_2).setOnClickListener(this);
        findViewById(R.id.Button_stop_1).setOnClickListener(this);
        findViewById(R.id.Button_stop_2).setOnClickListener(this);

        mDownloadManagerPro = new DownloadManagerPro(this,DATABASE_NAME,DATABASE_VERSION);
        String path = Environment.getExternalStorageDirectory().getPath();
        mDownloadManagerPro.init(path, 12, this);
    }

    @Override
    protected void onDestroy() {
        mDownloadManagerPro.dispose();
        mDownloadManagerPro = null;
        super.onDestroy();
    }

    // public ReportStructure singleDownloadStatus(int token);
//    public List<ReportStructure> downloadTasksInSameState(int state);
//    public List<ReportStructure> lastCompletedDownloads();
//    public boolean unNotifiedChecked();
//    public boolean delete(int token, boolean deleteTaskFile);

    private int mTask1 = 0, mTask2 = 0;

    @Override
    public void onClick(View v) {
        try{
            if (v.getId() == R.id.Button_start_1) {
                if(mTask1==0)
                    mTask1 = mDownloadManagerPro.addTask(System.currentTimeMillis()+"", "http://down11.zol.com.cn/suyan/qqdownloader6.7.1g.apk", false, false);
                mDownloadManagerPro.startDownload(mTask1);
            } else if (v.getId() == R.id.Button_start_2) {
                if(mTask2==0)
                    mTask2 = mDownloadManagerPro.addTask(System.currentTimeMillis()+"", "http://down11.zol.com.cn/suyan/qqdownloader6.7.1g.apk", false, false);
                mDownloadManagerPro.startDownload(mTask2);
            } else if (v.getId() == R.id.Button_del_1) {
                mDownloadManagerPro.delete(mTask1,true);
                mTask1  = 0;
            } else if (v.getId() == R.id.Button_del_2) {
                mDownloadManagerPro.delete(mTask2,true);
                mTask2 = 0;
            } else if (v.getId() == R.id.Button_pause_1) {
                mDownloadManagerPro.pauseDownload(mTask1);
            } else if (v.getId() == R.id.Button_pause_2) {
                mDownloadManagerPro.pauseDownload(mTask2);
            } else if (v.getId() == R.id.Button_stop_1) {
                mTask1  = 0;
                mDownloadManagerPro.delete(mTask1,false);
            } else if (v.getId() == R.id.Button_stop_2) {
                mTask2  = 0;
                mDownloadManagerPro.delete(mTask2,false);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void OnDownloadStarted(long taskId) {
        Log.i("download",taskId+":OnDownloadStarted");
    }

    @Override
    public void OnDownloadPaused(long taskId) {
        Log.i("download",taskId+":OnDownloadPaused");
    }

    @Override
    public void onDownloadProcess(final long taskId,final double percent,final long downloadedLength) {
        Log.i("download",taskId+":onDownloadProcess" +" percent:"+percent + " downloadedLength:"+downloadedLength);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DownloadActivity.this,taskId+":onDownloadProcess" +" percent:"+percent + " downloadedLength:"+downloadedLength,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnDownloadFinished(long taskId) {
        Log.i("download",taskId+":OnDownloadFinished");
    }

    @Override
    public void OnDownloadRebuildStart(long taskId) {
        Log.i("download",taskId+":OnDownloadRebuildStart");
    }

    @Override
    public void OnDownloadRebuildFinished(long taskId) {
        Log.i("download",taskId+":OnDownloadRebuildFinished");
    }

    @Override
    public void OnDownloadCompleted(long taskId) {
        Log.i("download",taskId+":OnDownloadCompleted");
    }

    @Override
    public void connectionLost(long taskId) {
        Log.i("download",taskId+":connectionLost");
    }
}
