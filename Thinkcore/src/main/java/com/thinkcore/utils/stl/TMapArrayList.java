package com.thinkcore.utils.stl;

import java.util.ArrayList;

public class TMapArrayList<T extends Object> extends ArrayList<THashMap<T>> {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(THashMap<T> taHashMap) {
		if (taHashMap != null) {
			return super.add(taHashMap);
		} else {
			return false;
		}
	}
}
