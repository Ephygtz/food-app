package com.wrx.quickeats.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mobulous8 on 26/10/16.
 */
public class SharedPreferenceWriter {

  private static Context context = null;
  private static SharedPreferenceWriter sharePref = null;
  private static SharedPreferences mPrefs;
  private static SharedPreferences.Editor prefEditor = null;

  public SharedPreferenceWriter() {
    // TODO Auto-generated constructor stub
  }

  public static SharedPreferenceWriter getInstance(Context c) {
    context = c;
    if (null == sharePref) {
      sharePref = new SharedPreferenceWriter();
      mPrefs = context.getSharedPreferences("restaurant", Context.MODE_PRIVATE);
      prefEditor = mPrefs.edit();
    }
    return sharePref;
  }

  public void writeStringValue(String key, String value) {
    if (prefEditor != null) {
      prefEditor.putString(key, value);
      prefEditor.commit();
    }

  }



  public void writeIntValue(String key, int value) {
    if (prefEditor != null) {
      prefEditor.putInt(key, value);
      prefEditor.commit();
    }
  }

  public void writeBooleanValue(String key, boolean value) {
    if (prefEditor != null) {
      prefEditor.putBoolean(key, value);
      prefEditor.commit();
    }
  }

  public void writeLongValue(String key, long value) {
    if (prefEditor != null) {
      prefEditor.putLong(key, value);
      prefEditor.commit();
    }
  }

  public void clearPreferenceValue(String key, String value) {
    if (prefEditor != null) {
      prefEditor.putString(key, "");
      prefEditor.commit();
    }

  }

  public String getString(String key, String defaultvalue) {
    if (mPrefs != null) {
      return mPrefs.getString(key, defaultvalue);
    }
    return "";

  }


  public String getString(String key) {
    if (mPrefs != null) {
      return mPrefs.getString(key, "");
    }
    return "";

  }

  public int getInt(String key) {
    if (mPrefs != null) {
      return mPrefs.getInt(key, 0);
    }
    return 0;
  }

  public boolean getBoolean(String key, boolean value) {
    if (mPrefs != null) {
      return mPrefs.getBoolean(key, value);
    }
    return false;
  }

  public long getLong(String key) {
    if (mPrefs != null) {
      return mPrefs.getLong(key, (long) 0.0);
    } else {
      return (long) 0.0;
    }
  }

  public void clearPreferenceValues() {
    if (prefEditor != null) {
      prefEditor.clear();
      prefEditor.commit();
    }


  }
}

