package com.thinkcore.utils.log;

import com.thinkcore.utils.TStringUtils;

import android.util.Log;

//打印到LogCat上面的日志类
public class PrintToLogCatLogger implements TILogger {
	@Override
	public void d(String tag, String message) {
		if (TStringUtils.isEmpty(tag) || TStringUtils.isEmpty(message))
			return;
		Log.d(tag, message);
	}

	@Override
	public void e(String tag, String message) {
		if (TStringUtils.isEmpty(tag) || TStringUtils.isEmpty(message))
			return;
		Log.e(tag, message);
	}

	@Override
	public void i(String tag, String message) {
		if (TStringUtils.isEmpty(tag) || TStringUtils.isEmpty(message))
			return;
		Log.i(tag, message);
	}

	@Override
	public void v(String tag, String message) {
		if (TStringUtils.isEmpty(tag) || TStringUtils.isEmpty(message))
			return;
		Log.v(tag, message);
	}

	@Override
	public void w(String tag, String message) {
		if (TStringUtils.isEmpty(tag) || TStringUtils.isEmpty(message))
			return;
		Log.w(tag, message);
	}

	@Override
	public void println(int priority, String tag, String message) {
		if (TStringUtils.isEmpty(tag) || TStringUtils.isEmpty(message))
			return;
		Log.println(priority, tag, message);
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}
}
