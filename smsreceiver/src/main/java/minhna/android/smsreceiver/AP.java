package minhna.android.smsreceiver;

/**
 * Created by minhnguyen on 11/13/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class AP {
    private static SharedPreferences mAppPreferences;
    private static SharedPreferences.Editor mEditor;

    private AP() {}

    private static void initPrefs(Context context) {
        if (mAppPreferences == null) {
            mAppPreferences = context.getSharedPreferences("smsforwarder",
                    Context.MODE_PRIVATE);
        }
    }

    public static void clearPrefs(Context context) {
        initPrefs(context);
        mAppPreferences.edit().clear().apply();
    }

    public static void saveData(Context context, String key, boolean value) {
        initPrefs(context);
        mEditor = mAppPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public static boolean getData(Context context, String key) {
        initPrefs(context);
        return mAppPreferences.getBoolean(key, false);
    }

    public static boolean getBooleanData(Context context, String key, boolean def) {
        initPrefs(context);
        return mAppPreferences.getBoolean(key, def);
    }

    public static void saveData(Context context, String key, String value) {
        initPrefs(context);
        mEditor = mAppPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public static String getStringData(Context context, String key) {
        initPrefs(context);
        return mAppPreferences.getString(key, null);
    }
    public static String getStringDataWithDefault(Context context, String key) {
        initPrefs(context);
        return mAppPreferences.getString(key, "");
    }

    public static String getStringDataWithDefault(Context context, String key, String defaultString) {
        initPrefs(context);
        return mAppPreferences.getString(key, defaultString);
    }

    public static void saveData(Context context, String key, long value) {
        initPrefs(context);
        mEditor = mAppPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    public void saveData(Context context, String key, int value) {
        initPrefs(context);
        mEditor = mAppPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public long getLongData(Context context, String key) {
        initPrefs(context);
        return mAppPreferences.getLong(key, -1);
    }

    public long getLongDataWithDefault(Context context, String key) {
        initPrefs(context);
        return mAppPreferences.getLong(key, 0);
    }

    public int getIntData(Context context, String key) {
        initPrefs(context);
        return mAppPreferences.getInt(key, -1);
    }

    public int getIntDataWithDefaultValue(Context context, String key, int defaultValue) {
        initPrefs(context);
        return mAppPreferences.getInt(key, defaultValue);
    }


    public void saveData(Context context, String key, float value) {
        initPrefs(context);
        mEditor = mAppPreferences.edit();
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public float getFloatData(Context context, String key) {
        initPrefs(context);
        try {
            return mAppPreferences.getFloat(key, 0);
        } catch (Exception e) {
            return 0.0f;
        }
    }
}
