package com.yd.cfssbusinessdemo.adapter;

import android.util.SparseArray;
import android.view.View;

public class VHtools {

	@SuppressWarnings("unchecked")
	public static <T> T get(View view, int id) {
		SparseArray viewHolder = (SparseArray) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray();
			view.setTag(viewHolder);
		}
		View childView = (View) viewHolder.get(id);
		if (childView == null) {
			childView =

			view.findViewById(id);
			viewHolder.put(id,

			childView);
		}
		return (T) childView;
	}
}