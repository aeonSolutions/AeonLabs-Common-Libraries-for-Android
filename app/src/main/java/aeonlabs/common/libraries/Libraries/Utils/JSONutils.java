package aeonlabs.common.libraries.Libraries.Utils;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONutils {
    private Activity activity;

    private JSONObject jsonObject;
    private JSONArray dataArray;
    private JSONObject dataobjFromArray;

    private String errorMsg="";
    private Exception exception;
    private int dataArrayLenght=0;

    public JSONutils(Activity activity){
        this.activity=activity;
        this.errorMsg="";
    }

    public Boolean hasError(){
        if(this.errorMsg.equals("")){
            return false;
        }else{
            return true;
        }
    }
    public Exception getExeption(){
        return this.exception;
    }
    public String getErrorMsg(){
        return this.errorMsg;
    }
    public int getDataArrayLenght() {return this.dataArrayLenght; }

    public Boolean loadJSONstring(String jsonStr){
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (Exception e) {
            exception=e;
            errorMsg= e.getMessage();
            return false;
        }
        return false;
    }

    public String getJSONstringValue(String key){
        try {
            return jsonObject.getString(key);
        } catch (Exception e) {
            exception=e;
            errorMsg= e.getMessage();
            return null;
        }
    }

    public Boolean loadJSONarray(String jsonArrayStr){
        try {
            dataArray = jsonObject.getJSONArray(jsonArrayStr);
            dataArrayLenght=dataArray.length();
        } catch (Exception e) {
            exception=e;
            errorMsg= e.getMessage();
            return false;
        }
        return false;
    }

    public String getJSONstringValueFromArray(String key, Integer index){
        try {
            JSONObject dataobjFromArray = dataArray.getJSONObject(index);
            return dataobjFromArray.getString(key);
        } catch (Exception e) {
            exception=e;
            errorMsg= e.getMessage();
            return null;
        }
    }

    public String getJSONstringValueFromArray(String key){
        try {
            return dataobjFromArray.getString(key);
        } catch (Exception e) {
            exception=e;
            errorMsg= e.getMessage();
            return null;
        }
    }

    public Boolean selectIndexOnJSONarray(Integer index){
        try {
            dataobjFromArray = dataArray.getJSONObject(index);
            return true;
        } catch (Exception e) {
            exception=e;
            errorMsg= e.getMessage();
            return null;
        }
    }
}
