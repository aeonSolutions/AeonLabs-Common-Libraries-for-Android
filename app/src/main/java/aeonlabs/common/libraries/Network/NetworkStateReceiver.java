package aeonlabs.common.libraries.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import aeonlabs.common.libraries.data.SessionData;


public class NetworkStateReceiver extends BroadcastReceiver
{
    private final static String TAG = "NetworkStateReceiver";

    public void onReceive(Context context, Intent intent)
    {
        Log.d(TAG, "Network connectivity change");
        if (intent.getExtras() != null)
        {
            ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED)
            {
                //Network becomes available
                Log.i(TAG, "Network " + ni.getTypeName() + " connected");
                SessionData.Network.setNetworkStatus(true);
            }
            else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE))
            {
                Log.d(TAG, "There's no network connectivity");
                SessionData.Network.setNetworkStatus(false);
            }
        }
    }

}