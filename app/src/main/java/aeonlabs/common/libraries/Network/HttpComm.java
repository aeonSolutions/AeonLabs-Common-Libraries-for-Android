package aeonlabs.common.libraries.Network;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

import aeonlabs.common.libraries.R;

public class HttpComm {
    private  static Boolean error;
    private static int errorCode;
    private  static String Message="";
    private  static String response="";
    private static Activity activity;
    private String type;
    private String charset="UTF-8";
    private int serverResponseCode=-1;

    public HttpComm(Activity _activity){
        activity=_activity;
    }

    public String getResponse(){
        return response;
    }

    public int getServerResponseCode(){ return serverResponseCode; }

    public String getMessage(){ return Message; }
    public Boolean getError(){ return error; }
    public int getErrorCode(){ return errorCode; }

    public void setType(String _type){ type= _type; }
    public void setCharSet(String _charset){ charset= _charset; }


    public Boolean send(String urlStr, String data2Send, List<String> files){
        HttpURLConnection conn = null;
        DataOutputStream dos = null;

        String lineEnd = System.getProperty("line.separator"); // "\r\n"
        String twoHyphens = "--";
        String boundary = "*****" + System.currentTimeMillis() + "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        URL url;
        FileInputStream fileInputStream;

        String serverResponseMessage="";
        Message="";

        try {
            //encode the URL
            url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
        }catch (Exception e){
            error=true;
            errorCode=-1;
            Message= activity.getResources().getString(R.string.error_http_url_invalid);
            return false;
        }

        // Open a HTTP  connection to  the URL
        try {
            conn = (HttpURLConnection) url.openConnection();
        }catch (Exception e){
            error=true;
            errorCode=-101;
            Message= activity.getResources().getString(R.string.error_http_open_connection);
            return false;
        }

        conn.setDoInput(true); // Allow Inputs
        conn.setDoOutput(true); // Allow Outputs
        conn.setUseCaches(false); // Don't use a Cached Copy

        try {
            conn.setRequestMethod(type.toUpperCase());
        }catch (Exception e){
            error=true;
            errorCode=-102;
            Message= activity.getResources().getString(R.string.error_http_request_type);
            return false;
        }

        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
        conn.setRequestProperty("User-Agent", "Aeon Labs");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                conn.setRequestProperty("file" + i, files.get(i));
            }
        }
        try {
            dos =  new DataOutputStream(conn.getOutputStream());
        }catch (Exception e){
            error=true;
            errorCode=-201;
            Message= activity.getResources().getString(R.string.error_http_preparing_data);
            return false;
        }


        try {
            // send multipart form data after file data...
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"d\"");
            dos.writeBytes(lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=" + charset);
            dos.writeBytes(lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(data2Send);
            dos.writeBytes(lineEnd);
            dos.flush();

            // repeat if more fields are needed
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"o\"");
            dos.writeBytes(lineEnd);
            dos.writeBytes("Content-Type: text/plain; charset=" + charset);
            dos.writeBytes(lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("m"); // m for mobile origin
            dos.flush();

        } catch (Exception e) {
            error = true;
            errorCode = -202;
            Message = activity.getResources().getString(R.string.error_http_preparing_data);
            return false;
        }

        if (files != null){
            for(int i=0;i<files.size();i++) {
                try {
                    fileInputStream = new FileInputStream(files.get(i));
                } catch (Exception e) {
                    error = true;
                    errorCode = -301;
                    Message = activity.getResources().getString(R.string.error_http_loading_file);
                    return false;
                }
                try {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file"+i+"\";filename=\"" + files.get(i).substring(files.get(i).lastIndexOf("/")+1) + "\"" + lineEnd); // missing remove path from filename
                    dos.writeBytes(lineEnd);

                    /*
                    For some reason i dont know yet this part corrupts the file  by adding 61 bytes
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    int index = files.get(i).lastIndexOf('.')+1;
                    String ext = files.get(i).substring(index).toLowerCase();
                    String type = mime.getMimeTypeFromExtension(ext);
                    type= (type.equals("")) ? "* /*" : type;
                    dos.writeBytes("Content-Type: " + type);
                    dos.writeBytes(lineEnd);

                    dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.flush();
                    */

                } catch (Exception e) {
                    error = true;
                    errorCode = -203;
                    Message = activity.getResources().getString(R.string.error_http_preparing_data);
                    return false;
                }

                try {
                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                } catch (Exception e) {
                    error = true;
                    errorCode = -302;
                    Message = activity.getResources().getString(R.string.error_http_loading_file);
                    return false;
                }

                while (bytesRead > 0) {
                    try {
                        dos.write(buffer, 0, bufferSize);
                    } catch (Exception e) {
                        error = true;
                        errorCode = -204;
                        Message = activity.getResources().getString(R.string.error_http_preparing_data);
                        return false;
                    }

                    try {
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    } catch (Exception e) {
                        error = true;
                        errorCode = -302;
                        Message = activity.getResources().getString(R.string.error_http_loading_file);
                        return false;
                    }
                }
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    error = true;
                    errorCode = -303;
                    Message = activity.getResources().getString(R.string.error_http_loading_file);
                    return false;
                }
                try {
                    dos.writeBytes(lineEnd);
                    dos.flush();
                } catch (Exception e) {
                    error = true;
                    errorCode = -205;
                    Message = activity.getResources().getString(R.string.error_http_preparing_data);
                    return false;
                }
            } // end for
        }


        // finnish the wrapper
        try{
            dos.writeBytes(lineEnd);
            dos.flush();
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.close();
        } catch (Exception e) {
            error = true;
            errorCode = -206;
            Message = activity.getResources().getString(R.string.error_http_preparing_data);
            return false;
        }

        serverResponseCode=HttpURLConnection.HTTP_BAD_REQUEST;
        try{
            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            serverResponseMessage = conn.getResponseMessage();
        } catch (Exception e) {
            error = true;
            errorCode = serverResponseCode;
            Message = activity.getResources().getString(R.string.error_http_preparing_data);
            return false;
        }

        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s);
            }
            bufferedReader.close();
            response=sb.toString();
        } catch (Exception e) {
            error = true;
            errorCode = -401;
            Message = activity.getResources().getString(R.string.error_http_loading_response);
            return false;
        }
        if(response.equals("")){
            error = true;
            errorCode = -403;
            Message = activity.getResources().getString(R.string.error_http_empty_response);
            return false;
        }
        conn.disconnect();
        return true;
    }
}
