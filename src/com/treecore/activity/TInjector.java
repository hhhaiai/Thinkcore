package com.treecore.activity;

import java.lang.reflect.Field;

import com.treecore.activity.annotation.TInject;
import com.treecore.activity.annotation.TInjectResource;
import com.treecore.activity.annotation.TInjectView;

import android.app.Activity;
import android.content.res.Resources;

//注入类
public class TInjector {
	private static TInjector instance;

	private TInjector() {

	}

	// 获取实例
	public static TInjector getInstance() {
		if (instance == null) {
			instance = new TInjector();
		}
		return instance;
	}

	// 注入activity的所有
	public void inJectAll(Activity activity) {
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(TInjectView.class)) {
					injectView(activity, field);
				} else if (field.isAnnotationPresent(TInjectResource.class)) {
					injectResource(activity, field);
				} else if (field.isAnnotationPresent(TInject.class)) {
					inject(activity, field);
				}
			}
		}
	}

	// 注入
	private void inject(Activity activity, Field field) {
		try {
			field.setAccessible(true);
			field.set(activity, field.getType().newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 注入视图
	private void injectView(Activity activity, Field field) {
		if (field.isAnnotationPresent(TInjectView.class)) {
			TInjectView viewInject = field.getAnnotation(TInjectView.class);
			int viewId = viewInject.id();
			try {
				field.setAccessible(true);
				field.set(activity, activity.findViewById(viewId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 注入资源
	private void injectResource(Activity activity, Field field) {
		if (field.isAnnotationPresent(TInjectResource.class)) {
			TInjectResource resourceJect = field
					.getAnnotation(TInjectResource.class);
			int resourceID = resourceJect.id();
			try {
				field.setAccessible(true);
				Resources resources = activity.getResources();
				String type = resources.getResourceTypeName(resourceID);
				if (type.equalsIgnoreCase("string")) {
					field.set(activity,
							activity.getResources().getString(resourceID));
				} else if (type.equalsIgnoreCase("drawable")) {
					field.set(activity,
							activity.getResources().getDrawable(resourceID));
				} else if (type.equalsIgnoreCase("layout")) {
					field.set(activity,
							activity.getResources().getLayout(resourceID));
				} else if (type.equalsIgnoreCase("array")) {
					if (field.getType().equals(int[].class)) {
						field.set(activity, activity.getResources()
								.getIntArray(resourceID));
					} else if (field.getType().equals(String[].class)) {
						field.set(activity, activity.getResources()
								.getStringArray(resourceID));
					} else {
						field.set(activity, activity.getResources()
								.getStringArray(resourceID));
					}

				} else if (type.equalsIgnoreCase("color")) {
					if (field.getType().equals(Integer.TYPE)) {
						field.set(activity,
								activity.getResources().getColor(resourceID));
					} else {
						field.set(activity, activity.getResources()
								.getColorStateList(resourceID));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 注入activity
	public void inject(Activity activity) {
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(TInject.class)) {
					inject(activity, field);
				}
			}
		}
	}

	// 注入视图
	public void injectView(Activity activity) {
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(TInjectView.class)) {
					injectView(activity, field);
				}
			}
		}
	}

	// 注入资源
	public void injectResource(Activity activity) {
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(TInjectResource.class)) {
					injectResource(activity, field);
				}
			}
		}
	}

}
