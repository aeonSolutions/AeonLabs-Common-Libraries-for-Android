 package aeonlabs.common.libraries.activities;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import com.yayandroid.databasemanager.DatabaseManager;

import aeonlabs.common.libraries.Helper.FragmentManagement;
import aeonlabs.common.libraries.InternalMessages.MessagesLogger;
import aeonlabs.common.libraries.logger.Log;
import aeonlabs.common.libraries.logger.LogWrapper;
import aeonlabs.common.libraries.data.SessionData;

public class ActivityBase extends AppCompatActivity implements LifecycleObserver {

        public static final String TAG="AeonLabsApp";
        private ProgressDialog progressDialog;
        private final boolean PROGRESS_CANCELABLE = false;
        public SessionData sessionData= new SessionData(this);
        public MessagesLogger eventMsg= new MessagesLogger(this);
        public static FragmentManagement fragmentManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        initializeLogging();


    }

    public DatabaseManager getDatabaseManager() {
        return ((aeonlabs.common.libraries.activities.ApplicationBase) getApplication()).getDatabaseManager();
    }

    protected void setUseReflection(boolean enabled) {
        ((aeonlabs.common.libraries.activities.ApplicationBase) getApplication()).setUseReflection(enabled);
    }

    public boolean getUseReflection() {
        return ((aeonlabs.common.libraries.activities.ApplicationBase) getApplication()).getUseReflection();
    }


    public void displayProgress(String title,  String msg,  boolean isCancelable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setCanceledOnTouchOutside(isCancelable);

        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /** Set up targets to receive log data */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);
        Log.i(TAG, "Ready");
    }

}
