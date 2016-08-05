package com.testcore.crash.sent;

import com.thinkcore.crash.TCrash;
import com.thinkcore.crash.TIReportSender;
import com.thinkcore.crash.data.CrashReportData;
import com.thinkcore.crash.exception.ReportSenderException;

import android.content.Context;

public class CrashReportSender implements TIReportSender {
	private final Context mContext;
	public final static String formUri = "http://10.32.202.155:9876";

	public CrashReportSender(Context mContext) {
		super();
		this.mContext = mContext;
		TCrash.getInstance().getErrorReporter()
				.putCustomData("PLATFORM", "ANDROID");
		TCrash.getInstance().getErrorReporter()
				.putCustomData("BUILD_ID", android.os.Build.ID);
		TCrash.getInstance().getErrorReporter()
				.putCustomData("DEVICE_NAME", android.os.Build.PRODUCT);

	}

	@Override
	public void send(CrashReportData arg0) throws ReportSenderException {
		// // 邮件发送
		EmailIntentSender emailSender = new EmailIntentSender(mContext);
		emailSender.send(arg0);
		// // 自定义发送
		// HttpPostSender httpSender = new HttpPostSender(formUri, null);
		// httpSender.send(arg0);
		// // 发往谷歌
		// GoogleFormSender googleSender = new GoogleFormSender();
		// googleSender.send(arg0);
	}
}
