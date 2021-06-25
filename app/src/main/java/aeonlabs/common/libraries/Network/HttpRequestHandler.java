package aeonlabs.common.libraries.Network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import aeonlabs.common.libraries.data.SessionData;
import aeonlabs.common.libraries.logger.CrashReports;
import aeonlabs.common.libraries.R;


public class HttpRequestHandler {

    public String uploadFile(String upLoadServerUri , String sourceFileUri, Activity activity) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        int serverResponseCode = 0;
        String serverResponseMessage="";
        String response="";

            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                //encode the URL
                URL url = new URL(upLoadServerUri);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name='file';filename='" + fileName + "'" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    sb.append(s + "\n");
                }
                response=sb.toString();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200 && response.equals("")){
                    response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" (200 OK null)'}";
                            //Toast.makeText(context.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                }else if(serverResponseCode != 200 ){
                    //TODO add boolean debug for hide technical message info
                    response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+"("+serverResponseCode+" "+serverResponseMessage+")'}";
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                CrashReports crashReports = new CrashReports();
                crashReports.SaveCrash(ex, activity);
                response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" ("+ex.getMessage()+")'}";
            } catch (Exception e) {
                CrashReports crashReports = new CrashReports();
                crashReports.SaveCrash(e, activity);
                response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" ("+e.getMessage()+")'}";
            }
            return response;

    }


    //Method to send httpPostRequest
    //This method is taking two arguments
    //First argument is the URL of the script to which we will send the request
    //Other is an HashMap with name value pairs containing the data to be send with the request
    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams, Activity activity) {
        //Creating a URL
        String response;
        int serverResponseCode = 0;
        String serverResponseMessage="";

        //StringBuilder object to store the message retrieved from the server
        StringBuilder sb = new StringBuilder();
        try {
            //encode the URL
            URL url = new URL(requestURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();

            //Creating an httmlurl connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Configuring connection properties
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            //Creating an output stream
            OutputStream os = conn.getOutputStream();

            //Writing parameters to the request
            //We are using a method getPostDataString which is defined below
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, (StandardCharsets.UTF_8)));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            serverResponseMessage = conn.getResponseMessage();

            if (serverResponseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                //Reading server response
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
            response= sb.toString();
            if(serverResponseCode == 200 && response.equals("")){
                response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" (200 OK null)'}";
                //Toast.makeText(context.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
            }else if(serverResponseCode != 200 ){
                response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+"("+serverResponseCode+" "+serverResponseMessage+")'}";
            }

        } catch (Exception e) {
            response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" ("+e.getMessage()+")'}";
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
        }
        return response;
    }

    public String sendGetRequest(String requestURL, Activity activity) {
        String response="";
        if (!SessionData.Network.getNetworkStatus()) {
            response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+")'}";
            return response;
        }
        StringBuilder sb = new StringBuilder();
        int serverResponseCode = 0;
        String serverResponseMessage="";
        HttpURLConnection conn=null;

        try {
            //encode the URL
            URL url = new URL(requestURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "PostmanRuntime/7.15.0");
            conn.setRequestProperty("Postman-Token", "c069e39b-7ff7-455e-837d-16d7cca0f3a3");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            serverResponseMessage = conn.getResponseMessage();

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }

            response= sb.toString();
            if(serverResponseCode == 200 && response.equals("")){
                response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" (200 OK null)'}";
                //Toast.makeText(context.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
            }else if(serverResponseCode != 200 ){
                response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+"(("+serverResponseCode+") "+serverResponseMessage+")'}";
            }
        } catch (Exception e) {
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
            response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" ("+e.getStackTrace().toString()+")'}";
        }finally {
            if (conn != null){
                conn.disconnect();
            }else{
                response= "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" (null conn)'}";
            }
        }
        return response;
    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public String getHttpFile(String requestURL, String filename, Activity activity) {
        File outputFile = null;

        try {
            URL url = new URL(requestURL);//Create Download URl
            HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
            c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
            c.connect();//connect the URL Connection

            //If Connection response is not OK then show Logs
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+"(("+c.getResponseCode()+") "+c.getResponseMessage()+")'}";
            }

            outputFile = new File(filename);//Create Output file in Main File

            //Create New File if not present
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location
            InputStream is = c.getInputStream();//Get InputStream for connection

            byte[] buffer = new byte[1024];//Set buffer type
            int len1 = 0;//init length
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);//Write new file
            }

            //Close all connection after doing task
            fos.close();
            is.close();

        } catch (Exception e) {
            //Read exception if something went wrong
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
            outputFile = null;
            return "{'error':true,'message':'"+activity.getResources().getString(R.string.commServer_check_internet)+" ("+e.getMessage()+")'}";
        }

        return "{'error':false,'message':'"+activity.getResources().getString(R.string.commServer_download_ok)+"'}";
    }


    public boolean isURLReachable(String requestURL, Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL(requestURL);
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);          // 10 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.wtf("Connection", "Success !");
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
}