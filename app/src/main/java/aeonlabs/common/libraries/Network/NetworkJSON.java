package aeonlabs.common.libraries.Network;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;


import aeonlabs.common.libraries.logger.CrashReports;
import aeonlabs.common.libraries.R;

import static aeonlabs.common.libraries.Network.NetworkSessionData.HTTP_RESPONSE_ERROR_KEY;
import static aeonlabs.common.libraries.Network.NetworkSessionData.HTTP_RESPONSE_MESSAGE_KEY;

public class NetworkJSON {
    public static boolean isSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return !jsonObject.optString(HTTP_RESPONSE_ERROR_KEY).equals("1");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getErrorMessage(Activity activity, SendData sendData) {
        if(sendData.getErrorType()==sendData.ERROR_TYPE_DECRIPT_RESPONSE){
            return sendData.getResponseRaw();
        }

        try {
            JSONObject jsonObject = new JSONObject(sendData.getResponse());
            return jsonObject.getString(HTTP_RESPONSE_MESSAGE_KEY);

        } catch (Exception e) {
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
        }

        return activity.getResources().getString(R.string.error_no_data);
    }
}
