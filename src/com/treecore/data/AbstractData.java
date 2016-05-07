package com.treecore.data;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

//抽象类
public abstract class AbstractData {

	// 数据库操作
	public abstract ContentValues getContentValue(boolean primary); // 把数据封装成数据库格式

	public abstract void setCursor(Cursor cursor); // 本地数据库

	// Json操作
	public abstract JSONObject getJson(); // 获取联系人的json格式

	public abstract void setJson(JSONObject jsonObject);
}
