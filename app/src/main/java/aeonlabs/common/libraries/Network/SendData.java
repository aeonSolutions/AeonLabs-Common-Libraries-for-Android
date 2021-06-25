package aeonlabs.common.libraries.Network;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import aeonlabs.common.libraries.Encryption.Encryption;
import aeonlabs.common.libraries.Network.DataRequest.DataQueue;
import aeonlabs.common.libraries.Network.database.EntityFields;
import aeonlabs.common.libraries.Network.database.EntityFiles;
import aeonlabs.common.libraries.Network.database.EntityQueue;
import aeonlabs.common.libraries.Network.database.LocalDatabaseOperations;
import aeonlabs.common.libraries.UserInterface.UI;
import aeonlabs.common.libraries.activities.ActivityBase;
import aeonlabs.common.libraries.logger.CrashReports;
import aeonlabs.common.libraries.R;
import aeonlabs.common.libraries.data.SessionData;
import static aeonlabs.common.libraries.Network.NetworkSessionData.HTTP_RESPONSE_MESSAGE_KEY;

public class SendData  {
    public static String MESSAGE_TYPE_ALERTBOX="alertBox";
    public static String MESSAGE_TYPE_TOAST="toast";
    public static String MESSAGE_TYPE_SILENT="silent";
    public static String MESSAGE_TYPE_REQUEST200="request 200";

    public static int REQUEST_TYPE_200=200; // request completed OK
    public static int REQUEST_TYPE_401=401;// auth required
    public static int REQUEST_TYPE_499=499; // token required is missing

    public static int ERROR_TYPE_DECRIPT_RESPONSE=1001;
    public static int ERROR_TYPE_QUEUE_ERROR=1002;
    public static int ERROR_TYPE_NO_NETWORK=1003;
    public static int ERROR_TYPE_ENCRYPT_RESPONSE=1004;
    public static int ERROR_TYPE_DECRYPT_IV_ERROR_RESPONSE=1005;

    private int errorType=-1;
    public int getErrorType(){ return errorType;}

    public static String MESSAGE_SILENT="silent";
    public static String MESSAGE_SERVER_RESPONSE="response";
    public static String MESSAGE_REGULAR_MESSAGE="message";
    public static String MESSAGE_DEBUG="debug";

    public static String REQUEST_TYPE_BACKGROUND="silent_background";

    public static Boolean WAIT_FOR_CODE_ON=true;
    public static Boolean WAIT_FOR_CODE_OFF=false;
    public static Boolean LOAD_MAIN_PAGE_ON=true;
    public static Boolean LOAD_MAIN_PAGE_OFF=false;
    public static Boolean ENABLE_QUEUE_ON=true;
    public static Boolean ENABLE_QUEUE_OFF=false;


    private ActivityBase activity=null;
    private FragmentActivity fragmentActivity=null;

    private EntityQueue queue=null;
    private List<EntityFields> fieldsInList=null;
    private List<EntityFiles> filesInList=null;

    private Boolean encrypt;
    private String encryptionType="";
    private String encryptionKey="";

    private Boolean waitForCode=false;
    private Boolean loadMainPage=false;
    private Boolean enableQueue=true;
    private Integer retryCount;
    private String requestType;

    private static Boolean error;
    private String responseMsg="";
    private String responseMsgRaw="";

    private int serverResponseCode=-1;

    public DataQueue setQueue = new DataQueue();

    public ObservableValue DelayedResult;

    public SendData (ActivityBase _activity, FragmentActivity _fragmentActivity){
        this.activity=_activity;
        this.fragmentActivity=_fragmentActivity;
        this.DelayedResult = new ObservableValue(getResponse());
        this.retryCount=0;
        this.fieldsInList=  new ArrayList<>();
        this.filesInList= new ArrayList<>();

        //initialize queue
        this.setQueue.setUrl("");
    }

    public int getServerResponseCode(){ return serverResponseCode;}
    public void setRequestType(String _type){ requestType=_type;}

    public void setWaitForCode(Boolean _wait){ waitForCode=_wait;}
    public void setloadMainPage(Boolean _load){ loadMainPage=_load;}
    public void setEnableQueue(Boolean _queue){ enableQueue=_queue; }
    public void setRunModes(Boolean waitForCode, Boolean loadMainPage, Boolean enableQueue){
        this.waitForCode=waitForCode;
        this.loadMainPage=loadMainPage;
        this.enableQueue=enableQueue;
    }

    public String getResponse(){ return responseMsg; }
    public String getResponseRaw(){ return responseMsgRaw; }
    public Boolean hasError(){ return error; }


    public void addQueue(EntityQueue _queue){ queue=_queue;}

    public void addFields(List<EntityFields> _fields){ fieldsInList=_fields;}

    public void addfFiles(List<EntityFiles> _files){ filesInList=_files;}

    public Boolean isSuccess(){
        return NetworkJSON.isSuccess(responseMsg);
    }
    public String getErrorMessage(){
        return NetworkJSON.getErrorMessage(activity,SendData.this);
    }
// ***********************************************************************************************
    public void initializeDefaults(){
        setRunModes(WAIT_FOR_CODE_ON,LOAD_MAIN_PAGE_OFF,ENABLE_QUEUE_OFF);
        setQueue.setMessages(MESSAGE_TYPE_SILENT, MESSAGE_SERVER_RESPONSE, MESSAGE_SERVER_RESPONSE);
        //TODO
        setQueue.setTitle( activity.getResources().getString(R.string.commServer_connect_msg));
        setQueue.setDescription( activity.getResources().getString(R.string.commServer_connect_msg));
    }

    public void setDefaultFields(String taskId){

        String data= taskId + SessionData.Network.DATA_SEPARATOR;
        data+= SessionData.PlugIns.Authentication.UID + SessionData.Network.DATA_SEPARATOR;
        data+= SessionData.System.CURRENT_LANGUAGE+ SessionData.Network.DATA_SEPARATOR;
        data+= SessionData.Network.API_KEY + SessionData.Network.DATA_SEPARATOR;
        data+= SessionData.System.DEVICE_SERIAL;
        setField("a",data);
    }
// ***********************************************************************************************
    public void setField(String requestVar, String requestValue){
        EntityFields field = new EntityFields();
        field.setRequestVar(requestVar);
        field.setValue(requestValue);
        fieldsInList.add(field);
    }

    public void setFile(String filename, Boolean appendCode, String title){
        EntityFiles file = new EntityFiles();
        file.setFilename(filename);
        file.setAppendCode(appendCode);
        file.setTitle(title);
        filesInList.add(file);
    }
// ***********************************************************************************************

    public void setEncryption(Boolean _encrypt, String _type, String encryptionKey){
        encryptionType= _type;
        encrypt=_encrypt;
        this.encryptionKey=encryptionKey;
    }


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

    public boolean send(){
        queue=setQueue.getQueue();

        if (queue.getUrl()==null){
            String test= SessionData.Network.APIconnectUrl;
            queue.setUrl(SessionData.Network.APIconnectUrl);
        } else if(queue.getUrl()!=null){
            if (queue.getUrl().equals("")){
                String test= SessionData.Network.APIconnectUrl;
                queue.setUrl(SessionData.Network.APIconnectUrl);
            }
        }

        if(encryptionType != null && encrypt !=null && encryptionKey != null) {
            if (encryptionType.equals("") || encrypt.equals("") || encryptionKey.equals("")) {
                encryptionType = SessionData.Network.ENCRYPTION_DEFAULT_ALGORITHM;
                encrypt = SessionData.Network.encryption_enabled;
                encryptionKey = SessionData.Network.cloudServerEncryptionKey;
            }
        }
        error=false;
        if (waitForCode) {
            try{
                SendDataAsync sendDataAsync =new SendDataAsync();
                sendDataAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                error=false;
                return true;
            }catch(Exception e){
                CrashReports crashReports = new CrashReports();
                crashReports.SaveCrash(e, activity);
                error=true;
                return false;
            }
        }else{
            SendDataAsync sendDataAsync =new SendDataAsync();
            sendDataAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            error=false;
            return true;
        }
    }

    private class SendDataAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            error=true;
            if (queue == null) {
                responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'message':'" + activity.getResources().getString(R.string.sendData_missing_queue) + "'}";
            } else if (fieldsInList == null) {
                responseMsg = "{'error':true,'message':'" + activity.getResources().getString(R.string.sendData_missing_fields) + "'}";
            } else if (SessionData.Network.getNetworkStatus()) { // send immediately
                //Json format var="{'error':true,'message':'some message'}";
                String Data2Send = "{";
                String temp = "";

                JSONObject jObjectData = new JSONObject();
                List<String> FileNamesList= new ArrayList<>();
                FileNamesList.clear();
                Data2Send = "";

                try {
                    for (int i = 0; i < fieldsInList.size(); i++) {
                        jObjectData.put(fieldsInList.get(i).getRequestVar(), fieldsInList.get(i).getValue().replace("'", "\'").replace('"', '\"'));
                    }

                    if (filesInList != null) {
                        if (filesInList.size()>0){
                            temp = "";
                            String temp2 = "";
                            for (int i = 0; i < filesInList.size(); i++) {
                                FileNamesList.add(filesInList.get(i).getFilename());
                                temp2 += filesInList.get(i).getAppendCode() ? "1" : "0";
                                temp += i == 0 ? temp2 : "," + temp2;
                            }
                            jObjectData.put("filecode", temp);
                        }
                    }
                } catch (Exception e) {
                    CrashReports crashReports = new CrashReports();
                    crashReports.SaveCrash(e, activity);
                }
                Data2Send = jObjectData.toString();

                Encryption encryption = new Encryption(activity);
                encryption.setSecretKey(encryptionKey);
                if (encryption.encryptString(Data2Send)) {
                    HttpComm request = new HttpComm(activity);
                    request.setType("post");
                    request.setCharSet("UTF-8");

                    if(requestType!=REQUEST_TYPE_BACKGROUND){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                UI.showSimpleProgressDialog(activity, activity.getResources().getString(R.string.commServer_connect_title_msg), activity.getResources().getString(R.string.commServer_connect_msg),false);
                            }
                        });
                    }

                    String urlEncoded="";
                    try{
                        urlEncoded=URLEncoder.encode(encryption.getEncryptedString(), "UTF-8");
                    }catch (Exception e){
                        urlEncoded=encryption.getEncryptedString();
                    }
                    error = !request.send(queue.getUrl(), urlEncoded, FileNamesList);

                    if(requestType!=REQUEST_TYPE_BACKGROUND){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                UI.removeSimpleProgressDialog(activity);
                            }
                        });
                    }
                    serverResponseCode=request.getServerResponseCode();
                    //TODO check type of error 400 500 ...
                    if (queue.getMsgType()== MESSAGE_TYPE_REQUEST200 & serverResponseCode==REQUEST_TYPE_200){
                        return "";
                    }
                    responseMsgRaw=request.getResponse();
                    if(queue.getMsgError().equals("debug")) {
                        encryption.decryptString(request.getResponse());
                        responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'DEBUG: " + encryption.getDecryptedString() + "'}";
                    }else if(error && retryCount< 6){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                new CountDownTimer(20000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        activity.setTitle(R.string.commServer_retry+" "+millisUntilFinished / 1000);
                                    }
                                    public void onFinish() {
                                        retryCount++;
                                        send();
                                    }
                                }.start();
                            }
                        });
                        return "retrying";
                    }else if (error && enableQueue) {
                        LocalDatabaseOperations localDatabaseOperations = new LocalDatabaseOperations(activity);
                        error = LocalDatabaseOperations.addQueue(queue, fieldsInList, filesInList);
                        if (error) {
                            responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + request.getMessage() + System.getProperty("line.separator") + localDatabaseOperations.getErrMessage() + "'}";
                        } else {
                            responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':0,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + activity.getResources().getString(R.string.sendData_added_queue) + "'}";
                        }
                    } else if (error && !enableQueue) {
                        responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + request.getMessage() + "'}";
                    } else { // success sending request
                        if (encryption.decryptString(request.getResponse())) {
                            if (encryption.getEncryptedVector().equals(encryption.getDecryptedVector())) {
                                responseMsg = encryption.getDecryptedString(); // success on reteiving data from server
                            } else {
                                String ss=encryption.getEncryptedVector();
                                String sss=encryption.getDecryptedVector();
                                String s=encryption.getDecryptedString();
                                responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + activity.getResources().getString(R.string.sendData_error_ivector) + "'}";
                                errorType=ERROR_TYPE_DECRYPT_IV_ERROR_RESPONSE;
                            }
                        } else { // error decrypting string
                            errorType=ERROR_TYPE_DECRIPT_RESPONSE;
                            responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + activity.getResources().getString(R.string.sendData_error_decrypt_data) + "'}";
                            String response= request.getResponse();
                            String str= "Message:"+encryption.getErrMessage()+"  >>>>Encrypt:"+ encryption.getEncryptedVector()+"  >>>>Dencrypt:"+ encryption.getDecryptedVector();
                            Exception e= new Exception(str);
                            CrashReports crashReports = new CrashReports();
                            crashReports.SaveCrash(e, activity);
                        }
                    }
                } else { // error encrypting string
                    errorType=ERROR_TYPE_ENCRYPT_RESPONSE;
                    responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + activity.getResources().getString(R.string.sendData_error_encrypt_data) + "'}";
                }
            }else if(!enableQueue){
                errorType=ERROR_TYPE_NO_NETWORK;
                responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + activity.getResources().getString(R.string.error_no_network)  + "'}";
            } else { // send to queue
                LocalDatabaseOperations localDatabaseOperations = new LocalDatabaseOperations(activity);
                error = LocalDatabaseOperations.addQueue(queue, fieldsInList, filesInList);
                if (error) {
                    errorType=ERROR_TYPE_QUEUE_ERROR;
                    responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':1,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + localDatabaseOperations.getErrMessage() + "'}";
                } else {
                    responseMsg = "{'"+HTTP_RESPONSE_MESSAGE_KEY+"':0,'"+HTTP_RESPONSE_MESSAGE_KEY+"':'" + activity.getResources().getString(R.string.sendData_added_queue) + "'}";
                }
            }
            return responseMsg;
        }

        @Override
        protected void onPostExecute(String response) {
            if(response.equals("retrying")){
                return;
            }

            DelayedResult.setValue(response);

            if (queue.getMsgType()== MESSAGE_TYPE_REQUEST200 & response.equals("")){
                return;
            }
            if (queue.getMsgType().equals(MESSAGE_TYPE_TOAST)) {
                if (NetworkJSON.isSuccess(response)) {
                    if (queue.getMsgSuccess().equals("") || queue.getMsgSuccess().equals(MESSAGE_SERVER_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, NetworkJSON.getErrorMessage(activity, SendData.this), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (!queue.getMsgError().equals(MESSAGE_SILENT)){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, queue.getMsgSuccess(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    if (queue.getMsgError().equals("") || queue.getMsgError().equals(MESSAGE_SERVER_RESPONSE)) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, NetworkJSON.getErrorMessage(activity, SendData.this), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (!queue.getMsgError().equals(MESSAGE_SILENT)){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, queue.getMsgError(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } else if (queue.getMsgType().equals(MESSAGE_TYPE_ALERTBOX)) { // alertbox
                if (NetworkJSON.isSuccess(response)) {
                    if (queue.getMsgSuccess().equals("") || queue.getMsgSuccess().equals(MESSAGE_SERVER_RESPONSE)) {activity.runOnUiThread(new Runnable() {
                        public void run() {
                            UI.alertbox(activity.getResources().getString(R.string.commServer_submit_ok), NetworkJSON.getErrorMessage(activity, SendData.this), activity);
                        }
                    });

                    } else if (!queue.getMsgError().equals(MESSAGE_SILENT)){
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                UI.alertbox(activity.getResources().getString(R.string.commServer_submit_ok), queue.getMsgSuccess(), activity);
                            }
                        });
                    }
                } else {
                    if (queue.getMsgError().equals("") || queue.getMsgError().equals(MESSAGE_SERVER_RESPONSE) || queue.getMsgError().equals(MESSAGE_DEBUG)) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                UI.alertbox(activity.getResources().getString(R.string.commServer_submit_error), NetworkJSON.getErrorMessage(activity, SendData.this), activity);
                            }
                        });
                    } else if (!queue.getMsgError().equals(MESSAGE_SILENT)) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                UI.alertbox(activity.getResources().getString(R.string.commServer_submit_error), queue.getMsgError(), activity);
                            }
                        });
                    }
                }
            }
            // Show messageBox messages

            if (loadMainPage && !error) {
                FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.content_frame, new FragmentJournal());
                fragmentTransaction.commit();
            }
        }
    }
}
