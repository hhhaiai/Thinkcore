package com.thinkcore.utils.log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.thinkcore.TApplication;

//日志打印类
public class TLog {
	private static final String Tag = TLog.class.getSimpleName();

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
	private static HashMap<String, ILogger> mLoggerHashMap = new HashMap<String, ILogger>();
	private static ILogger mDefaultLogger = new PrintToLogCatLogger();
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

	public static void addLogger(ILogger logger) {
		String loggerName = logger.getClass().getName();
		String defaultLoggerName = mDefaultLogger.getClass().getName();
		if (!mLoggerHashMap.containsKey(loggerName)
				&& !defaultLoggerName.equalsIgnoreCase(loggerName)) {
			logger.open();
			mLoggerHashMap.put(loggerName, logger);
		}
	}

	public static void removeLogger(ILogger logger) {
		String loggerName = logger.getClass().getName();
		if (mLoggerHashMap.containsKey(loggerName)) {
			logger.close();
			mLoggerHashMap.remove(loggerName);
		}
	}

	public static void d(Object object, String message, Object... args) {
		printLoger(DEBUG, object == null ? "" : object, message, args);
	}

	public static void d(Object object, Throwable e, Object... args) {
		printLoger(DEBUG, object == null ? "" : object,
				e == null ? "" : e.getMessage(), args);
	}

	public static void e(Object object, Throwable e) {
		printLoger(ERROR, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void e(Object object, String message, Object... args) {
		printLoger(ERROR, object == null ? "" : object, message, args);
	}

	public static void e(Object object, String message, Throwable e) {
		printLoger(ERROR, object == null ? "" : object, message + " "
				+ (e == null ? "" : e.getMessage()));
	}

	public static void i(Object object, String message, Object... args) {
		printLoger(INFO, object == null ? "" : object, message, args);
	}

	public static void i(Object object, Throwable e) {
		printLoger(INFO, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void v(Object object, String message, Object... args) {
		printLoger(VERBOSE, object == null ? "" : object, message, args);
	}

	public static void v(Object object, Throwable e) {
		printLoger(VERBOSE, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void w(Object object, String message, Object... args) {
		printLoger(WARN, object == null ? "" : object, message, args);
	}

	public static void w(Object object, Throwable e) {
		printLoger(WARN, object == null ? "" : object,
				e == null ? "" : e.getMessage());
	}

	public static void d(String tag, String message, Object... args) {
		printLoger(DEBUG, tag, message, args);
	}

	public static void d(String tag, Throwable e) {
		printLoger(DEBUG, tag, e == null ? "" : e.getMessage());
	}

	public static void e(String tag, String message, Object... args) {
		printLoger(ERROR, tag, message, args);
	}

	public static void e(String tag, Throwable e) {
		printLoger(ERROR, tag, e == null ? "" : e.getMessage());
	}

	public static void e(String tag, String message, Throwable e) {
		printLoger(ERROR, tag, message + " "
				+ (e == null ? "" : e.getMessage()));
	}

	public static void i(String tag, String message, Object... args) {
		printLoger(INFO, tag, message, args);
	}

	public static void i(String tag, Throwable e) {
		printLoger(INFO, tag, e == null ? "" : e.getMessage());
	}

	public static void v(String tag, String message, Object... args) {
		printLoger(VERBOSE, tag, message, args);
	}

	public static void v(String tag, Throwable e) {
		printLoger(VERBOSE, tag, e == null ? "" : e.getMessage());
	}

	public static void w(String tag, String message, Object... args) {
		printLoger(WARN, tag, message, args);
	}

	public static void w(String tag, Throwable e) {
		printLoger(WARN, tag, e == null ? "" : e.getMessage());
	}

	public static void println(int priority, String tag, String message,
			Object... args) {
		printLoger(priority, tag, message, args);
	}

	private static void printLoger(int priority, Object object, String message,
			Object... args) {
		String tag = Tag;
		try {
			Class<?> cls = object.getClass();
			tag = cls.getName();
			String arrays[] = tag.split("\\.");
			tag = arrays[arrays.length - 1];
		} catch (Exception e) {
			tag = Tag;
		}

		printLoger(priority, tag, String.format(message, args));
	}

	private static void printLoger(int priority, String tag, String message) {
			printLoger(mDefaultLogger, priority, tag, message);
			Iterator<Entry<String, ILogger>> iter = mLoggerHashMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, ILogger> entry = iter.next();
				ILogger logger = entry.getValue();
				if (logger != null) {
					printLoger(logger, priority, tag, message);
				}
			}
	}

	private static void printLoger(ILogger logger, int priority, String tag,
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
