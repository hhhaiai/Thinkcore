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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.testcore.crash.utils.HttpRequest;
import com.thinkcore.crash.*;
import com.thinkcore.crash.data.CrashReportData;
import com.thinkcore.crash.data.ReportField;
import com.thinkcore.crash.exception.ReportSenderException;

import android.net.Uri;
import android.util.Log;

/**
 * <p>
 * The {@link ReportSender} used by ACRA when {@link ReportsCrashes#formUri()}
 * has been defined in order to post crash data to a custom server-side data
 * collection script. It sends all data in a POST request with parameters named
 * with easy to understand names (basically a string conversion of
 * {@link ReportField} enum values) or based on your own conversion Map from
 * {@link ReportField} values to String.
 * </p>
 * 
 * <p>
 * To use specific POST parameter names, you can provide your own report fields
 * mapping scheme:
 * </p>
 * 
 * <pre>
 * &#64;ReportsCrashes(...)
 * public class myApplication extends Application {
 * 
 *     public void onCreate() {
 *         ACRA.init(this);
 *         Map&lt;ReportField, String&gt; mapping = new HashMap&lt;ReportField, String&gt;();
 *         mapping.put(ReportField.APP_VERSION_CODE, &quot;myAppVerCode'); 
 *         mapping.put(ReportField.APP_VERSION_NAME, &quot;myAppVerName');
 *         //... 
 *         mapping.put(ReportField.USER_EMAIL, &quot;userEmail');
 *         // remove any default report sender
 *         ErrorReporter.getInstance().removeAllReportSenders();
 *         // create your own instance with your specific mapping
 *         ErrorReporter.getInstance().addReportSender(new ReportSender(&quot;http://my.domain.com/reports/receiver.py&quot;, mapping));
 *         
 *         
 *         super.onCreate();
 *     }
 * }
 * </pre>
 * 
 * @author Kevin Gaudin
 * 
 */
public class HttpPostSender implements TIReportSender {

	private final Uri mFormUri;
	private final Map<ReportField, String> mMapping;

	/**
	 * <p>
	 * Create a new HttpPostSender instance with its destination taken from
	 * {@link ACRA#getConfig()} dynamically. Configuration changes to the
	 * formUri are applied automatically.
	 * </p>
	 * 
	 * @param mapping
	 *            If null, POST parameters will be named with
	 *            {@link ReportField} values converted to String with
	 *            .toString(). If not null, POST parameters will be named with
	 *            the result of mapping.get(ReportField.SOME_FIELD);
	 */
	public HttpPostSender(Map<ReportField, String> mapping) {
		mFormUri = null;
		mMapping = mapping;
	}

	/**
	 * <p>
	 * Create a new HttpPostSender instance with a fixed destination provided as
	 * a parameter. Configuration changes to the formUri are not applied.
	 * </p>
	 * 
	 * @param formUri
	 *            The URL of your server-side crash report collection script.
	 * @param mapping
	 *            If null, POST parameters will be named with
	 *            {@link ReportField} values converted to String with
	 *            .toString(). If not null, POST parameters will be named with
	 *            the result of mapping.get(ReportField.SOME_FIELD);
	 */
	public HttpPostSender(String formUri, Map<ReportField, String> mapping) {
		mFormUri = Uri.parse(formUri);
		mMapping = mapping;
	}

	@Override
	public void send(CrashReportData report) throws ReportSenderException {

		// try {
		// final Map<String, String> finalReport = remap(report);
		// final URL reportUrl = mFormUri == null ? new URL(TAcra.getConfig()
		// .formUri()) : new URL(mFormUri.toString());
		// Log.d("", "Connect to " + reportUrl.toString());
		//
		// final String login = isNull(TAcra.getConfig()
		// .formUriBasicAuthLogin()) ? null : TAcra.getConfig()
		// .formUriBasicAuthLogin();
		// final String password = isNull(TAcra.getConfig()
		// .formUriBasicAuthPassword()) ? null : TAcra.getConfig()
		// .formUriBasicAuthPassword();
		//
		// final HttpRequest request = new HttpRequest();
		// request.setConnectionTimeOut(TAcra.getConfig().connectionTimeout());
		// request.setSocketTimeOut(TAcra.getConfig().socketTimeout());
		// request.setMaxNrRetries(TAcra.getConfig()
		// .maxNumberOfRequestRetries());
		// request.setLogin(login);
		// request.setPassword(password);
		// request.sendPost(reportUrl, finalReport);

		// } catch (IOException e) {
		// throw new ReportSenderException(
		// "Error while sending report to Http Post Form.", e);
		// }
	}
	//
	// private static boolean isNull(String aString) {
	// return aString == null || TAcraConstants.NULL_VALUE.equals(aString);
	// }
	//
	// private Map<String, String> remap(Map<ReportField, String> report) {
	//
	// ReportField[] fields = TAcra.getConfig().customReportContent();
	// if (fields.length == 0) {
	// fields = TAcra.DEFAULT_REPORT_FIELDS;
	// }
	//
	// final Map<String, String> finalReport = new HashMap<String, String>(
	// report.size());
	// for (ReportField field : fields) {
	// if (mMapping == null || mMapping.get(field) == null) {
	// finalReport.put(field.toString(), report.get(field));
	// } else {
	// finalReport.put(mMapping.get(field), report.get(field));
	// }
	// }
	// return finalReport;
	// }
}
