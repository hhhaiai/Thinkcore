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
package com.testcore.crash.sent;

import com.thinkcore.crash.*;
import com.thinkcore.crash.TIReportSender;
import com.thinkcore.crash.data.CrashReportData;
import com.thinkcore.crash.data.ReportField;
import com.thinkcore.crash.exception.ReportSenderException;

import android.content.Context;
import android.content.Intent;

/**
 * Send reports through an email intent. The user will be asked to chose his
 * preferred email client. Included report fields can be defined using
 * {@link com.treecore.crash.core.ReportsCrashes#customReportContent()}. Crash
 * receiving mailbox has to be defined with {@link ReportsCrashes#mailTo()}.
 */
public class EmailIntentSender implements TIReportSender {

	private final Context mContext;

	public EmailIntentSender(Context ctx) {
		mContext = ctx;
	}

	@Override
	public void send(CrashReportData errorContent) throws ReportSenderException {

		final String subject = mContext.getPackageName() + " Crash Report";
		final String body = buildBody(errorContent);

		final Intent emailIntent = new Intent(
				Intent.ACTION_SEND);
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, body);
		emailIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "banketree@qq.com" });
		mContext.startActivity(emailIntent);
	}

	private String buildBody(CrashReportData errorContent) {
		final StringBuilder builder = new StringBuilder();
		for (ReportField field : TCrash.getInstance().getReportFields()) {
			builder.append(field.toString()).append("=");
			builder.append(errorContent.get(field));
			builder.append('\n');
		}
		return builder.toString();
	}
}
