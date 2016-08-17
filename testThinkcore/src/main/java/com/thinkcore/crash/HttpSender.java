/*
 *  Copyright 2010 Kevin Gaudin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.thinkcore.crash;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testcore.ui.TestTActivity;
import com.thinkcore.utils.TActivityUtils;
import com.thinkcore.utils.TToastUtils;
import com.thinkcore.utils.log.TLog;

import junit.framework.Test;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.sender.ReportSenderFactory;
import org.acra.util.HttpRequest;
import org.acra.collections.ImmutableSet;
import org.acra.util.JSONReportBuilder.JSONReportException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static org.acra.ACRA.LOG_TAG;


public class HttpSender implements ReportSender {
    protected  String mUrlString = "";
    protected org.acra.sender.HttpSender.Method mMethod;

    public HttpSender(String url,@NonNull org.acra.sender.HttpSender.Method method){
        mUrlString = url;
        mMethod = method;
    }

    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData report) throws ReportSenderException {
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder().add("content", report.toJSON().toString()).build();
            Request request = new Request.Builder().url(mUrlString).post(formBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    // 注：该回调是子线程，非主线程
                    TLog.i("","callback thread id is "+Thread.currentThread().getId());
                }
            });

            Thread.sleep(2000);
        }catch (Exception e)
        {
        }

        TActivityUtils.jumpToNewTopActivity(context, TestTActivity.class);
    }
}