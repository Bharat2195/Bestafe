package com.toranado.bestafe.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.WindowManager;

import com.toranado.bestafe.R;

public class StringUtils {

	public static String getDeviceId(Context context){
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}
	public static boolean isNotBlank(String str) {
		if (str == null || str.trim().equalsIgnoreCase(""))
			return false;
		return true;
	}

	public static boolean isBlank(String str) {
		if (str == null || str.trim().equalsIgnoreCase(""))
			return true;
		return false;
	}

	public static String getReverseString(String msg) {
		if (msg.equalsIgnoreCase("0")) {
			return "1";
		} else if (msg.equalsIgnoreCase("1")) {
			return "0";
		} else if (msg.equalsIgnoreCase("")) {
			return "1";
		} else {
			return "";
		}
	}

	public static String getColoredString(String text, int color) {
		String str = "";
		str = "<font color='" +color+ "'>" + text + "</font>";
		return str;
	}

	public static ProgressDialog createProgressDialog(Context mContext) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		try {
			dialog.show();
		} catch (WindowManager.BadTokenException e) {

		}
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.progressdialog);
		// dialog.setMessage(Message);
		return dialog;
	}
	

}
