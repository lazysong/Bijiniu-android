package com.lazysong.bjn.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lazysong on 2017/3/14.
 */
public class PreferenceUtils {
    //将登录状态写入loginpref文件中
    public static void setLoginPref(Context context, String userId, String userKey, boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences("loginpref", android.app.Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (isLogin && !TextUtils.isEmpty(userId)) {
            editor.putBoolean("logined", true);
            editor.putString("userId", userId);
            editor.putString("userKey", userKey);
        }
        else {
            editor.putBoolean("logined", false);
            editor.putString("userId", "unknown");
            editor.putString("userKey", "-1");
        }
        editor.commit();
    }
}
