package com.thinkcore.crash;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

/**
 * Created by Administrator on 2016/8/16.
 */
public class HttpReportSenderFactory implements ReportSenderFactory {

    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration config) {
        return new HttpSender(config.formUri(),config.httpMethod());
    }
}
