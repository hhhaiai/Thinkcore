package com.testcore;

import android.content.Context;

import com.morgoo.droidplugin.PluginHelper;
import com.morgoo.droidplugin.pm.PluginManager;
import com.thinkcore.TApplication;
import com.thinkcore.crash.HttpReportSenderFactory;
import com.thinkcore.storage.TFilePath;
import com.thinkcore.utils.log.TLog;

import org.acra.*;
import org.acra.annotation.*;
import org.acra.builder.ReportExecutor;
import org.acra.sender.HttpSender;

@ReportsCrashes(
     formUri = "http://api.eotu.com:9090/app/addCollapse",
     httpMethod = HttpSender.Method.POST,
     reportSenderFactoryClasses = HttpReportSenderFactory.class
    //mailTo = "banketree@qq.com"
)
public class MyApp extends TApplication {
    private static final String TAG = MyApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());

        TLog.enablePrintToFileLogger(true);
        TFilePath filePath = new TFilePath();
        String path = filePath.getInterAppDir();
        TLog.i(this,path);
        path = filePath.getExternalAppDir();
        TLog.i(this,path);
        path = filePath.getInterImageDir();
        TLog.i(this,path);
        path = filePath.getExternalImageDir();
        TLog.i(this,path);
        path = filePath.getInterAudioDir();
        TLog.i(this,path);
        path = filePath.getExternalAudioDir();
        TLog.i(this,path);
        path = filePath.getInterCacheDir();
        TLog.i(this,path);
        path = filePath.getExternalCacheDir();
        TLog.i(this,path);
        path = filePath.getInterDownloadDir();
        TLog.i(this,path);
        path = filePath.getExternalDownloadDir();
        TLog.i(this,path);
    }


    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }
}
