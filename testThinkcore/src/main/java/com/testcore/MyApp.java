package com.testcore;

import com.thinkcore.TApplication;
import com.thinkcore.crash.HttpReportSenderFactory;

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
    }
}
