package mass.knextgen1.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import mass.knextgen1.R;

/**
 * Created by Robert Tanniru on 12/14/2014.
 */
public class GlobalPreferenceManager {

    public static void setGlobalPreferenceInt(Context context, String key, int val) {
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.global_preferences_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public static void setGlobalPreferenceBoolean(Context context, String key, boolean val) {
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.global_preferences_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static void setGlobalPreferenceString(Context context, String key, String val) {
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.global_preferences_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static boolean getGlobalPreferenceBoolean(Context context, String key, boolean defaultBoolean) {
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.global_preferences_name), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultBoolean);
    }

    public static String getGlobalPreferenceString(Context context, String key, String defaultString) {
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.global_preferences_name), Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultString);
    }

    public static int getGlobalPreferenceInt(Context context, String key, int defaultString) {
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.global_preferences_name), Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultString);
    }
}
