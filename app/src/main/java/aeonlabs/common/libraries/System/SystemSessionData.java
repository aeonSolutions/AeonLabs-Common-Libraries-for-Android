package aeonlabs.common.libraries.System;

import android.app.Activity;
import android.content.SharedPreferences;

import java.lang.reflect.AccessibleObject;


import static android.content.Context.MODE_PRIVATE;

public class SystemSessionData {
    public SystemSessionData(Activity act){
        this.activity= act;
    }
    private Activity activity;

    /******************** ERROR handling ****************************/
    public static String ERROR_MESSAGE="";
    public static Boolean isDebugEnabled= false;

    /******************** Package ****************************/
    public static String PACKAGE_NAME="aeonlabs.solutions.ioyo";

    /******************** Language ****************************/
    public static String DEFAULT_LANGUAGE="en";
    public static String CURRENT_LANGUAGE="en";

    public void setCurrentLanguage(String _lang){
        CURRENT_LANGUAGE=_lang;
        SharedPreferences settingsfile= this.activity.getSharedPreferences("settings",MODE_PRIVATE);
        SharedPreferences.Editor myeditor = settingsfile.edit();
        myeditor.putString("currentlanguage", _lang);
        myeditor.apply();
        myeditor.commit();
    }

    public String getCurrentLanguage(){
        SharedPreferences settings= this.activity.getSharedPreferences("settings", MODE_PRIVATE);
        CURRENT_LANGUAGE= settings.getString("currentlanguage", "en");
        return CURRENT_LANGUAGE;
    }

    /************************ DATE TIME ***********************************/
    public static String TODAY_DATE="";
    public static String SELECTED_DATE="";

    /************************ Device Serial ***********************************/
    public static  String DEVICE_SERIAL="-";
}
