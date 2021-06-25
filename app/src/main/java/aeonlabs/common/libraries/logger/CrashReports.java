package aeonlabs.common.libraries.logger;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;

public class CrashReports {

    public CrashReports(){

    }

    public void SaveCrash(Throwable e, Activity activity){
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString() + "\n\n";
        report += "--------- Stack trace ---------\n\n";
        report+= "----------"+System.nanoTime()+"----------";
        report+= "----------OS version:"+android.os.Build.VERSION.SDK_INT+"----------";

        for (int i = 0; i < arr.length; i++) {
            report += "    " + arr[i].toString() + "\n";
        }
        report += "-------------------------------\n\n";
        report += "--------- Message ---------\n\n";
        report += e.getMessage()+"\n\n";
        report += "-------------------------------\n\n";
        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if (cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                report += "    " + arr[i].toString() + "\n";
            }
        }
        report += "-------------------------------\n\n";

        String filename = activity.getFilesDir() + "/crash.log";
        File file = new File(filename);

        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(report.getBytes());
            stream.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
