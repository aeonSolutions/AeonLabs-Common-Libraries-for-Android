package aeonlabs.common.libraries.data;

import android.app.Activity;
import aeonlabs.common.libraries.GeoLocation.GeoLocationSessionData;
import aeonlabs.common.libraries.Libraries.Tasks.LogoutTimerSessionData;
import aeonlabs.common.libraries.NFC.NFCsessionData;
import aeonlabs.common.libraries.Network.NetworkSessionData;
import aeonlabs.common.libraries.PlugIns.PlugInsSessionData;
import aeonlabs.common.libraries.System.SystemSessionData;
import aeonlabs.common.libraries.UserInterface.UIsessionData;

import static android.content.Context.MODE_PRIVATE;

public class SessionData {
    public SessionData(Activity _act){
        this.activity=_act;
        this.System = new SystemSessionData(this.activity);
        this.UserInterface= new UIsessionData(this.activity);
        this.Network = new NetworkSessionData(this.activity);
        this.PlugIns = new PlugInsSessionData(this.activity);
    }
    public Activity activity;

    /****************** NFC **************/
    static public NFCsessionData NFC = new NFCsessionData();

    /****************** Tasks **************/
    static public LogoutTimerSessionData IdleLogOut = new LogoutTimerSessionData();

    /****************** Geo Location **************/
    static public GeoLocationSessionData GeoLocation = new GeoLocationSessionData();

    /****************** custom plugIns **************/


    /****************** PlugIns ****************/
    static public PlugInsSessionData PlugIns = new PlugInsSessionData(null);

    /****************** SYSTEM ****************/
    static public SystemSessionData System = new SystemSessionData(null);

    /****************** UI user Interface ****************/
    static public UIsessionData UserInterface= new UIsessionData(null);

    /****************** HTTP CONFIGURATION ****************/
    public static NetworkSessionData Network = new NetworkSessionData(null);
}
