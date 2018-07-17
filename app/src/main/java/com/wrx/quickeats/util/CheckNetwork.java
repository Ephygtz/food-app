package com.wrx.quickeats.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {

	public static boolean isInternetAvailable(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}
		return false;
	}

	class PREF_VAR {
		public static final String ZURI_SHER_PREF = "zuri_shared_prefrances";

		public static final String APP_USE_COUNT = "app_use_count";
		public static final String APP_RATE = "app_rate";
	}

	public static void appOpened(Context context) {
		int curVal = getAppUseCount(context);
		curVal = curVal + 1;
		Editor editor = context.getSharedPreferences(PREF_VAR.ZURI_SHER_PREF,
				Context.MODE_PRIVATE).edit();
		editor.putInt(PREF_VAR.APP_USE_COUNT, curVal);
		editor.commit();
	}

	public static int getAppUseCount(Context context) {
		SharedPreferences spref = context.getSharedPreferences(
				PREF_VAR.ZURI_SHER_PREF, Context.MODE_PRIVATE);
		return spref.getInt(PREF_VAR.APP_USE_COUNT, 0);
	}

	public static void saveAppRate(Context context, boolean value) {
		Editor editor = context.getSharedPreferences(PREF_VAR.ZURI_SHER_PREF,
				Context.MODE_PRIVATE).edit();
		editor.putBoolean(PREF_VAR.APP_RATE, value);
		editor.commit();
	}

	public static boolean getAppRate(Context context) {
		SharedPreferences spref = context.getSharedPreferences(
				PREF_VAR.ZURI_SHER_PREF, Context.MODE_PRIVATE);
		return spref.getBoolean(PREF_VAR.APP_RATE, false);
	}
}
