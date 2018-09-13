package com.android.zakaria.classmateinfo.permissions;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.zakaria.classmateinfo.R;

public class PermissionUtils {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PermissionUtils(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.permission_preference_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void updatePermissionPreference(String permission) {
        switch (permission) {
            case "phone_call":
                editor.putBoolean(context.getString(R.string.permission_phone_call), true);
                editor.apply();
                break;
        }
    }

    public boolean checkPermissionPreference(String permission) {
        boolean isChecked = false;

        switch (permission) {
            case "phone_call":
                isChecked = sharedPreferences.getBoolean(context.getString(R.string.permission_phone_call), false);  //herer false is a default value
                break;
        }

        return isChecked;
    }

}
