package aeonlabs.common.libraries.GeoLocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import aeonlabs.common.libraries.InternalMessages.MessagesLogger;
import aeonlabs.common.libraries.UserInterface.UI;
import aeonlabs.common.libraries.activities.ActivityBase;
import aeonlabs.common.libraries.data.SessionData;
import aeonlabs.common.libraries.R;

public class GeoLocationUtils {
    public static void checkLocation(Activity activity, Class startupActivity){
        if (SessionData.GeoLocation.isDisabled)
            return;

        GeoLocation gps = new GeoLocation();
        if (gps.getLocation(activity) == null) {
            ((ActivityBase) activity).eventMsg.raiseNewMessage(MessagesLogger.MESSAGE_DISPLAY_AS_TOAST,MessagesLogger.MESSAGE_TYPE_CRITICAL,activity.getResources().getString(R.string.error_location_disabled),"");
            Intent intent = new Intent(activity, startupActivity);
            activity.startActivity(intent);
        } else {
            // Check if GPS enabled
            if (gps.canGetLocation()) {
                double latitude =-1.0;
                double longitude =-1.0;
                double authRadius=-1.0;

                if (SessionData.GeoLocation.overrideGeoLocation){
                    latitude = SessionData.GeoLocation.overrideLatitude;
                    longitude = SessionData.GeoLocation.overrideLongitude;
                    authRadius = 5.0;
                }else{
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    authRadius=SessionData.GeoLocation.authRadius;
                }

                if (latitude == -1 && longitude == -1) {
                    ((ActivityBase) activity).eventMsg.raiseNewMessage(MessagesLogger.MESSAGE_DISPLAY_AS_TOAST,MessagesLogger.MESSAGE_TYPE_CRITICAL,activity.getResources().getString(R.string.error_unable_get_location),"");
                    Intent intent = new Intent(activity, startupActivity);
                    activity.startActivity(intent);
                } else {
                    Double distance = gps.distance(SessionData.GeoLocation.latitude, SessionData.GeoLocation.longitude, latitude, longitude);
                    if (distance < authRadius) {
                        //TODO
                    } else {
                        ((ActivityBase) activity).eventMsg.raiseNewMessage(MessagesLogger.MESSAGE_DISPLAY_NONE,MessagesLogger.MESSAGE_TYPE_WARNING,"Login onsite","");
                        UI.showSimpleProgressDialog(activity, activity.getResources().getString(R.string.error_title), "Login On site", false);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                UI.removeSimpleProgressDialog(activity);
                            }
                        }, 1500);
                        Intent intent = new Intent(activity, startupActivity);
                        activity.startActivity(intent);
                    }
                }
            } else {
                ((ActivityBase) activity).eventMsg.raiseNewMessage(MessagesLogger.MESSAGE_DISPLAY_AS_TOAST,MessagesLogger.MESSAGE_TYPE_CRITICAL,activity.getResources().getString(R.string.error_location_disabled),"");
                Intent intent = new Intent(activity,startupActivity);
                activity.startActivity(intent);
            }
        }
    }
}
