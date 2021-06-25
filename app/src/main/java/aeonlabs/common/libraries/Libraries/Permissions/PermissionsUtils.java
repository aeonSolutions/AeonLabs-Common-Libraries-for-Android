package aeonlabs.common.libraries.Libraries.Permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import aeonlabs.common.libraries.logger.CrashReports;

public class PermissionsUtils {
    public static boolean checkPermissionForReadExtertalStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        try {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        } catch (Exception e) {
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
        }
        return false;
    }
}
