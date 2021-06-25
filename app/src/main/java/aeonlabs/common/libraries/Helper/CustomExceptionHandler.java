package aeonlabs.common.libraries.Helper;

import android.app.Activity;

import java.lang.Thread.UncaughtExceptionHandler;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

    private final UncaughtExceptionHandler defaultUEH;

    Activity activity;

    public CustomExceptionHandler(Activity activity) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.activity = activity;
    }

    public void uncaughtException(Thread t, Throwable e) {

        //Functions.SaveCrash(e, this.activity);
        defaultUEH.uncaughtException(t, e);
    }
}