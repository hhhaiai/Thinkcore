package com.treecore.utils.log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.treecore.TApplication;

//日志打印类
public class TLog {
	private static boolean mIgnoreAll = false;
	private static boolean mIgnoreInfo = false;
	private static boolean mIgnoreDebug = false;
	private static boolean mIgnoreWarn = false;
	private static boolean mIgnoreError = false;

	/**
	 * Priority constant for the println method; use TLog.v.
	 */
	public static final int VERBOSE = 2;

	/**
	 * Priority constant for the println method; use TLog.d.
	 */
	public static final int DEBUG = 3;

	/**
	 * Priority constant for the println method; use TLog.i.
	 */
	public static final int INFO = 4;

	/**
	 * Priority constant for the println method; use TLog.w.
	 */
	public static final int WARN = 5;

	/**
	 * Priority constant for the println method; use TLog.e.
	 */
	public static final int ERROR = 6;
	/**
	 * Priority constant for the println method.
	 */
	public static final int ASSERT = 7;
	private static HashMap<String, TILogger> mLoggerHashMap = new HashMap<String, TILogger>();
	private static TILogger mDefaultLogger = new PrintToLogCatLogger();
	private static HashMap<String, Boolean> mIgnoreTagHashMap = new HashMap<String, Boolean>();

	public static void enablePrintToFileLogger(boolean enable) {
		mDefaultLogger = null;
		if (enable) {
			mDefaultLogger = new PrintToFileLogger();
		} else {
			mDefaultLogger = new PrintToLogCatLogger();
		}
	}

	public static void release() {
		mDefaultLogger.close();
	}

	public static void addLogger(TILogger logger) {
		String loggerName = logger.getClass().getName();
		String defaultLoggerName = mDefaultLogger.getClass().getName();
		if (!mLoggerHashMap.containsKey(loggerName)
				&& !defaultLoggerName.equalsIgnoreCase(loggerName)) {
			logger.open();
			mLoggerHashMap.put(loggerName, logger);
		}
	}

	public static void removeLogger(TILogger logger) {
		String loggerName = logger.getClass().getName();
		if (mLoggerHashMap.containsKey(loggerName)) {
			logger.close();
			mLoggerHashMap.remove(loggerName);
		}
	}

	public static void d(Object object, String message) {
		printLoger(DEBUG, object == null ? "" : object, message);
	}

	public static void d(Object object, Throwable e) {
		printLoger(DEBUG, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void e(Object object, Throwable e) {
		printLoger(ERROR, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void e(Object object, String message) {
		printLoger(ERROR, object == null ? "" : object, message);
	}

	public static void e(Object object, String message, Throwable e) {
		printLoger(ERROR, object == null ? "" : object, message + " "
				+ (e == null ? "" : e.getMessage()));
	}

	public static void i(Object object, String message) {
		printLoger(INFO, object == null ? "" : object, message);
	}

	public static void i(Object object, Throwable e) {
		printLoger(INFO, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void v(Object object, String message) {
		printLoger(VERBOSE, object == null ? "" : object, message);
	}

	public static void v(Object object, Throwable e) {
		printLoger(VERBOSE, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void w(Object object, String message) {
		printLoger(WARN, object == null ? "" : object, message);
	}

	public static void w(Object object, Throwable e) {
		printLoger(WARN, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void d(String tag, String message) {
		printLoger(DEBUG, tag, message);
	}

	public static void d(String tag, Throwable e) {
		printLoger(DEBUG, tag, e == null ? "" : e.getMessage());
	}

	public static void e(String tag, String message) {
		printLoger(ERROR, tag, message);
	}

	public static void e(String tag, Throwable e) {
		printLoger(ERROR, tag, e == null ? "" : e.getMessage());
	}

	public static void e(String tag, String message, Throwable e) {
		printLoger(ERROR, tag, message + " " + (e == null ? "" : e.getMessage()));
	}

	public static void i(String tag, String message) {
		printLoger(INFO, tag, message);
	}

	public static void i(String tag, Throwable e) {
		printLoger(INFO, tag, e == null ? "" : e.getMessage());
	}

	public static void v(String tag, String message) {
		printLoger(VERBOSE, tag, message);
	}

	public static void v(String tag, Throwable e) {
		printLoger(VERBOSE, tag, e == null ? "" : e.getMessage());
	}

	public static void w(String tag, String message) {
		printLoger(WARN, tag, message);
	}

	public static void w(String tag, Throwable e) {
		printLoger(WARN, tag, e == null ? "" : e.getMessage());
	}

	public static void println(int priority, String tag, String message) {
		printLoger(priority, tag, message);
	}

	private static void printLoger(int priority, Object object, String message) {
		Class<?> cls = object.getClass();
		String tag = cls.getName();
		String arrays[] = tag.split("\\.");
		tag = arrays[arrays.length - 1];
		printLoger(priority, tag, message);
	}

	private static void printLoger(int priority, String tag, String message) {
		if (!TApplication.isRelease()) {
			printLoger(mDefaultLogger, priority, tag, message);
			Iterator<Entry<String, TILogger>> iter = mLoggerHashMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, TILogger> entry = iter.next();
				TILogger logger = entry.getValue();
				if (logger != null) {
					printLoger(logger, priority, tag, message);
				}
			}
		}
	}

	private static void printLoger(TILogger logger, int priority, String tag,
			String message) {
		if (!mIgnoreTagHashMap.isEmpty() && mIgnoreTagHashMap.containsKey(tag)) {
			return;
		}

		switch (priority) {
		case VERBOSE:
			if (!mIgnoreAll)
				logger.v(tag, message);
			break;
		case DEBUG:
			if (!mIgnoreDebug)
				logger.d(tag, message);
			break;
		case INFO:
			if (!mIgnoreInfo)
				logger.i(tag, message);
			break;
		case WARN:
			if (!mIgnoreWarn)
				logger.w(tag, message);
			break;
		case ERROR:
			if (!mIgnoreError)
				logger.e(tag, message);
			break;
		default:
			break;
		}
	}

	public static void ignoreAll(boolean enable) {
		mIgnoreAll = enable;
	}

	public static void ignoreInfo(boolean enable) {
		mIgnoreInfo = enable;
	}

	public static void ignoreDebug(boolean enable) {
		mIgnoreDebug = enable;
	}

	public static void ignoreWarn(boolean enable) {
		mIgnoreWarn = enable;
	}

	public static void ignoreError(boolean enable) {
		mIgnoreError = enable;
	}

	public static void clearIgnoreTag() {
		mIgnoreTagHashMap.clear();
	}

	public static void addIgnoreTag(String... tags) {
		for (String tag : tags) {
			mIgnoreTagHashMap.put(tag, true);
		}
	}
}
