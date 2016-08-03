package com.thinkcore.utils.stl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.thinkcore.utils.TStringUtils;

/**
 * @Title TArrayList NameValuePair数组
 */
public class TArrayList extends ArrayList<NameValuePair> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(NameValuePair nameValuePair) {
		if (!TStringUtils.isEmpty(nameValuePair.getValue())) {
			return super.add(nameValuePair);
		} else {
			return false;
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, String value) {
		return add(new BasicNameValuePair(key, value));
	}

}
