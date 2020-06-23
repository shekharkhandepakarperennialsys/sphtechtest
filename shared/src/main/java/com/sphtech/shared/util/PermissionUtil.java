package com.sphtech.shared.util;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {
    public static boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
