package com.treecore.activity.layoutloader;

import java.lang.reflect.Field;

import com.treecore.TApplication;
import com.treecore.exception.TNoSuchNameLayoutException;
import com.treecore.utils.log.TLog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @Title 是获取系统资源加载器
 */
public class TLayoutLoader implements TILayoutLoader {
	private static String TAG = TLayoutLoader.class.getSimpleName();
	private static TLayoutLoader instance;
	private Context mContext;

	private TLayoutLoader(Context context) {
		this.mContext = context;
	}

	/**
	 * 获得系统资源类
	 * 
	 * @return TASystemResLoader对象
	 */

	public static TLayoutLoader getInstance() {
		if (instance == null) {
			instance = new TLayoutLoader(TApplication.getInstance()
					.getApplicationContext());
		}
		return instance;
	}

	/**
	 * 获得布局ID
	 * 
	 * @param context
	 * @param cls
	 * @param resIDName
	 * @return
	 * @throws NameNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws TAResIDNotFoundException
	 */
	public int getLayoutID(String resIDName) throws NameNotFoundException,
			ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, TNoSuchNameLayoutException {
		int resID = readResID("layout", resIDName);
		if (resID == 0) {
			throw new TNoSuchNameLayoutException();
		}
		return resID;
	}

	/**
	 * @param context
	 * @param resIDName
	 *            资源id名字
	 * @return
	 * @throws NameNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public int readResID(String type, String resIDName)
			throws NameNotFoundException, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException {
		String packageName;
		PackageManager pm = mContext.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
		packageName = pi.packageName;
		if (packageName == null || packageName.equalsIgnoreCase("")) {
			throw new NameNotFoundException("没有获取到系统包名！");
		}
		packageName = packageName + ".R";
		Class<?> clazz = Class.forName(packageName);
		Class<?> cls = readResClass(clazz, packageName + "$" + type);
		if (cls == null) {
			throw new NameNotFoundException("没发现资源包名！");
		}
		return readResID(cls, resIDName);

	}

	/**
	 * 返回资源在R文件中生成的类
	 * 
	 * @param cls
	 *            资源类名
	 * @param respackageName
	 *            资源的包名
	 * @return 返回资源在R文件中生成的类
	 */
	public Class<?> readResClass(Class<?> cls, String respackageName) {
		Class<?>[] classes = cls.getDeclaredClasses();
		for (int i = 0; i < classes.length; i++) {
			Class<?> tempClass = classes[i];
			TLog.v(TAG, tempClass.getName());
			if (tempClass.getName().equalsIgnoreCase(respackageName)) {
				return tempClass;
			}
		}
		return null;
	}

	/**
	 * 读取R资源文件
	 * 
	 * @param cls
	 * @param string
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public int readResID(Class<?> cls, String resIDName)
			throws IllegalArgumentException, IllegalAccessException {

		Field[] fields = cls.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getName().equalsIgnoreCase(resIDName)) {
				return fields[j].getInt(cls);
			}
		}
		return 0;
	}

}
