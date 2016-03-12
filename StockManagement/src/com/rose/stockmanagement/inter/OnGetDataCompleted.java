package com.rose.stockmanagement.inter;

import java.sql.ResultSet;
import java.util.List;

import android.content.Context;

public interface OnGetDataCompleted {
	void onGetDataCompleted(List listResult, String strQueryType, Context context);
}