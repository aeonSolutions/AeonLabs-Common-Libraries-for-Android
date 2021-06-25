package aeonlabs.common.libraries.System;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Method;

public class GetDeviceSerialNumber {
    private Activity activity;

    public GetDeviceSerialNumber(Activity activity){
        this.activity=activity;
    }

    public String get(){
        String serialNumber="";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber="";
            if (Build.VERSION.SDK_INT >= 26) {
                if (activity.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    serialNumber = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }
        return  serialNumber;
    }
}
