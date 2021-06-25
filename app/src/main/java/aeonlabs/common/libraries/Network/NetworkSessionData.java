package aeonlabs.common.libraries.Network;

import android.app.Activity;
import android.content.SharedPreferences;



import static android.content.Context.MODE_PRIVATE;

public class NetworkSessionData {
    public NetworkSessionData(Activity _act){
        this.activity=_act;
    }
    private Activity activity;
    /*************************** AeonLabs API *****************************************************/
    static public String AeonLabsURL="http://www.aeonlabs.solutions";
    static public String AeonLabsTermsOfUseURL= AeonLabsURL + "/terms/terms.html";
    static public String AeonLabsPrivacyPoliciesURL= AeonLabsURL + "/privacy/privacy.html";
    /*************************** AeonLabs API *****************************************************/
    static public String AeonLabsServerBaseUrl="http://www.aeonlabs.solutions/services/api/index.php";
    static public String AeonLabsEncryptionKey="26kozQaKwRuNJ24t";
    static public String AEONLABS_ENCRYPTION_ALGORITHM="AES128";

    /****************************** crash reports and diagnostics data ****************************/
    static public String crashDataAPIUrl= AeonLabsServerBaseUrl + "/index.php";

    /*************************** APP API *****************************************************/
    static public String DATA_SEPARATOR = "▄";  // String.valueOf((char)220); //  char 220
    static public String API_KEY="";

    static public String cloudServerBaseUrl="http://aeonlabs.solutions/aeonpay.online/api";
    static public String APIconnectUrl="http://aeonlabs.solutions/aeonpay.online/api/index.php";
    static public String HostFilesUrl="http://aeonlabs.solutions/aeonpay.online/files";

    //TODO how to do change of key from time to time
    static public String cloudServerEncryptionKey="26kozQaKwRuNJ24t";

    /*************************** encryption configuration *****************************************/
    static public String ENCRYPTION_DEFAULT_ALGORITHM="AES128";
    static public boolean encryption_enabled=true;

    /********************************* API REQUEST CODES ******************************************/
    static public APIrequestCodes APIrequestCodes= new APIrequestCodes();

    /***************************   PREFERED NETWORK P2P OR CLOUD *********************************/
    static public String PREFERED_NETWORK_P2P = "P2P";
    static public String PREFERED_NETWORK_CLOUD = "CLOUD";

    static public String PREFERED_NETWORK = PREFERED_NETWORK_CLOUD;

    /******************************* NETWORK STATE ************************************************/
    static public Boolean networkConnectionState = false;

    /***********************************************************************************************/
    static public String HTTP_RESPONSE_ERROR_KEY="e";
    static public String HTTP_RESPONSE_MESSAGE_KEY="m";

    /***********************************************************************************************/

    public void setNetworkStatus(Boolean _state){
        networkConnectionState=_state;
        SharedPreferences settingsfile= this.activity.getSharedPreferences("session",MODE_PRIVATE);
        SharedPreferences.Editor myeditor = settingsfile.edit();
        myeditor.putBoolean("NetworkStatus", _state);
        myeditor.apply();
        myeditor.commit();
    }

    public Boolean getNetworkStatus(){
        SharedPreferences settings= this.activity.getSharedPreferences("session", MODE_PRIVATE);
        networkConnectionState= settings.getBoolean("NetworkStatus", true);
        return networkConnectionState;
    }
}
