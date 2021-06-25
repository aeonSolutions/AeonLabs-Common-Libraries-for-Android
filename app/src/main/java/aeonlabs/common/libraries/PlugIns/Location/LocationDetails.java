package aeonlabs.common.libraries.PlugIns.Location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

import aeonlabs.common.libraries.GeoLocation.GeoLocation;
import aeonlabs.common.libraries.Libraries.Utils.JSONutils;
import aeonlabs.common.libraries.Network.HttpRequestHandler;
import aeonlabs.common.libraries.data.SessionData;
import aeonlabs.common.libraries.R;

public class LocationDetails {

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

    public LocationDetails(){
        this.DelayedResult = new ObservableValue("");
    }

    @SuppressLint("StaticFieldLeak")
    public void load(Activity _activity){
        this.activity=_activity;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.date = sdf.format(new Date());

        final GetLocationDetailsAsyncTask task = new GetLocationDetailsAsyncTask(this.activity, SessionData.GeoLocation.latitude, SessionData.GeoLocation.longitude);
        task.execute();
        }

    public class GetLocationDetailsAsyncTask extends AsyncTask<Void, Void, String> {

        private final Activity activity;
        private Double latitude=-1.0;
        private Double longitude=-1.0;


        public GetLocationDetailsAsyncTask(final Activity _activity, Double latitude, Double longitude) {
            this.activity = _activity;
            this.latitude=latitude;
            this.longitude=longitude;

        }

        protected String doInBackground(Void[] params) {
            String response="";
            if(this.latitude !=-1.0 && this.longitude !=-1.0) {
                HttpRequestHandler requestHandler = new HttpRequestHandler();
                response = requestHandler.sendGetRequest(SessionData.PlugIns.locationDetails.HostLocationDetailsUrl+"&lat=" + SessionData.GeoLocation.latitude + "&lon=" + SessionData.GeoLocation.longitude, this.activity);
                return response;
            }else{
                //TODO
                response= "{'error':true,'message':'"+"error location"+"'}";
                return response;
            }
        }
        protected void onPostExecute(String response) {
            JSONutils json = new JSONutils(activity);
            json.loadJSONstring(response);
            SessionData.PlugIns.locationDetails.data.displayName=json.getJSONstringValue("display_name");
            json.loadJSONstring(json.getJSONstringValue("address"));
            SessionData.PlugIns.locationDetails.data.road=json.getJSONstringValue("road");
            SessionData.PlugIns.locationDetails.data.hamlet=json.getJSONstringValue("hamlet");
            SessionData.PlugIns.locationDetails.data.state=""+json.getJSONstringValue("state");
            SessionData.PlugIns.locationDetails.data.postCode=json.getJSONstringValue("postcode");
            if (json.getJSONstringValue("country") != null)
                SessionData.PlugIns.locationDetails.data.country = json.getJSONstringValue("country").toString().indexOf("/") >-1 ? json.getJSONstringValue("country").split("/")[0] : json.getJSONstringValue("country");

            SessionData.PlugIns.locationDetails.data.countryCode=json.getJSONstringValue("country_code");
            if(!json.getErrorMsg().equals("")){
                //TODO
                response= "{'error':true,'message':'"+"weather location error"+"'}";
            }
            DelayedResult.setValue(response);
        }
    }
}