package com.example.thiendn.watermarkphoto;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by thiendn on 13:34 4/28/19
 */
public class SharePref {

    private final static String PREF_NAME = "My_Pref";
    private final static String KEY_URL_WATERMARK = "url_watermark";

    public static void saveWaterMarkUrl(String url, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_URL_WATERMARK, url);
        editor.apply();
    }

    public static String getWaterMarkUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_URL_WATERMARK, null);
    }
}
