package aeonlabs.common.libraries.Network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

import aeonlabs.common.libraries.Libraries.Utils.JSONutils;
import aeonlabs.common.libraries.activities.ActivityBase;
import aeonlabs.common.libraries.data.SessionData;
import aeonlabs.common.libraries.R;


public class ConnectAeonLabsServices {

    private Activity activity;
    public String date;
    public ObservableValue DelayedResult;

    public class ObservableValue extends Observable
    {
        private String responseMsg = "";
        public ObservableValue(String _responseMsg)
        {
            this.responseMsg = _responseMsg;
        }
        public void setValue(String _responseMsg)
        {
            this.responseMsg = _responseMsg;
            setChanged();
            notifyObservers();
        }
        public String getValue()
        {
            return responseMsg;
        }
    }

    public ConnectAeonLabsServices(){
        this.DelayedResult = new ObservableValue("");
    }

    @SuppressLint("StaticFieldLeak")
    public void load(Activity _activity){
        this.activity=_activity;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.date = sdf.format(new Date());


        final GetConnectAeonLabsServicesAsyncTask task = new GetConnectAeonLabsServicesAsyncTask(this.activity, SessionData.GeoLocation.latitude, SessionData.GeoLocation.longitude);
        task.execute();
    }

    public class GetConnectAeonLabsServicesAsyncTask extends AsyncTask<Void, Void, String> {

        private final Activity activity;
        private Double latitude=-1.0;
        private Double longitude=-1.0;


        public GetConnectAeonLabsServicesAsyncTask(final Activity _activity, Double latitude, Double longitude) {
            this.activity = _activity;
            this.latitude=latitude;
            this.longitude=longitude;

        }

        protected String doInBackground(Void[] params) {
            String response="";
            HttpRequestHandler requestHandler = new HttpRequestHandler();
            response = requestHandler.sendGetRequest( SessionData.Network.AeonLabsServerBaseUrl+"?s=csl&g=&"+ SessionData.GeoLocation.latitude+ SessionData.Network.DATA_SEPARATOR+ SessionData.GeoLocation.longitude, this.activity);
            return response;
        }

        protected void onPostExecute(String response) {
            JSONutils json = new JSONutils(activity);
            json.loadJSONstring(response);

            SessionData.Network.cloudServerBaseUrl=json.getJSONstringValue("b");
            SessionData.Network.APIconnectUrl=json.getJSONstringValue("a");
            SessionData.Network.HostFilesUrl=json.getJSONstringValue("f");
            if(!json.getErrorMsg().equals("")){
                //TODO
                response= "{'error':true,'message':'"+"Error Location"+"'}";
            }
            //TODO verify for valid urls
            DelayedResult.setValue(response);
        }
    }
}
