package aeonlabs.common.libraries.UserInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import aeonlabs.common.libraries.logger.CrashReports;
import aeonlabs.common.libraries.R;


public class UI {
    /****************************************************************************************/

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager =(InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /****************************************************************************************/
    public static ProgressDialog mProgressDialog;

    public static void removeSimpleProgressDialog(Activity activity) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                    }
                } catch (Exception e) {
                    CrashReports crashReports = new CrashReports();
                    crashReports.SaveCrash(e, activity);
                }

            }
        });

    }

    /**************************************************************************************************/
    public static void showSimpleProgressDialog(final Activity activity, final String title, final String msg, final boolean isCancelable) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null) {
                        mProgressDialog= new ProgressDialog(activity);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                        mProgressDialog = ProgressDialog.show(activity, title, msg);
                        mProgressDialog.setCancelable(isCancelable);
                    }

                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }

                } catch (Exception e) {
                    CrashReports crashReports = new CrashReports();
                    crashReports.SaveCrash(e, activity);
                }
            }
        });
    }

/**************************************************************************************************/
    public static void alertbox(String title, String mymessage, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mymessage)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton(context.getResources().getString(R.string.alertbox_continue),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
