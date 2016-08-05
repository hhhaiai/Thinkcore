package com.testcore;

import com.thinkcore.TApplication;
import com.thinkcore.utils.log.TLog;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(
        mailTo = "banketree@163.com",
        mode = ReportingInteractionMode.DIALOG,
        customReportContent = {ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.BRAND, ReportField.STACK_TRACE, ReportField.LOGCAT, ReportField.USER_COMMENT},
        resToastText = R.string.app_test, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.app_test,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.app_test, // optional. default is your application name
        resDialogCommentPrompt = R.string.app_test, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.app_test // optional. displays a Toast message when the user accepts to send a report.
)
public class MyApp extends TApplication {
    private static final String TAG = MyApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        // 奔溃处理
        ACRA.init(this);
    }

    @Override
    public void onAppCrash(String crashFile) {

        TLog.d(TAG, "Creating Dialog for " + crashFile);
//		Intent dialogIntent = new Intent(this, TestCrash.class);
//		dialogIntent.putExtra(TCrash.EXTRA_REPORT_FILE_NAME, crashFile);
//		dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(dialogIntent);
    }
}
