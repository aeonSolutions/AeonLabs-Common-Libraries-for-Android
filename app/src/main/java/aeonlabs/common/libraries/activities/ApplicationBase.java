 package aeonlabs.common.libraries.activities;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.yayandroid.databasemanager.DatabaseManager;
import com.yayandroid.databasemanager.model.Database;

import java.io.File;

import aeonlabs.common.libraries.Libraries.Utils.FileUtils;
import aeonlabs.common.libraries.LocalDataBases.DBcreateTables;
import aeonlabs.common.libraries.data.SessionData;

public class ApplicationBase extends Application {
    private boolean useReflection = true;
    private DatabaseManager dbManager;
    public String DATABASE_PATH = "/data/data/"+ SessionData.System.PACKAGE_NAME +"/config/";

    private SQLiteDatabase sqlDB;

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DatabaseManager();
    }

    public void initializeDatabase(String DB_NAME, int DB_VERSION, String[] DB_TABLES){
        if (DB_TABLES==null)
            throw new RuntimeException("DB_TABLES need to be configured on the data/database folder");

        File dir = new File(DATABASE_PATH);
        if (!(dir.exists() && dir.isDirectory())) {
            if (!dir.mkdirs()) {
               //TODO
                SessionData.System.ERROR_MESSAGE = "Error creating local directories";
                throw new RuntimeException("Error creating local directories");
            }
        }

        if(fileFailChecks(DB_NAME))
            throw new RuntimeException("DB_TABLES: "+ SessionData.System.ERROR_MESSAGE);

        File file = new File(DATABASE_PATH+ DB_NAME+".s3db");
        if(file.exists()) {
            // load database from local
            dbManager.addDatabase(new Database.Builder(Database.LOCAL, DB_NAME)
                    .path(DATABASE_PATH+ DB_NAME+".s3db")
                    .openFlags(SQLiteDatabase.OPEN_READWRITE)
                    .build());
        }else{
            // create local database to manage
            dbManager.addDatabase(new Database.Builder(Database.LOCAL, DB_NAME)
                        .path(DATABASE_PATH+ DB_NAME+".s3db")
                        .openWith(new DBcreateTables(getApplicationContext(), DB_NAME, DB_VERSION, DB_TABLES))
                        .build());


        }
    }

    private Boolean fileFailChecks(String filename) {
        File dir = new File(DATABASE_PATH);
        if (!dir.isDirectory()) {
            if (!dir.mkdirs()) {
                //TODO
                SessionData.System.ERROR_MESSAGE = "Error creating local directories";
                throw new RuntimeException("Error creating local directories");
                //return true;
            }
        }
       /*
        long spaceAvail = FileUtils.getAvailableInternalMemorySize();
        if (spaceAvail < 200000000) { //200MB
            //TODO
            String stre = String.valueOf(spaceAvail);
            SessionData.System.ERROR_MESSAGE = "Not enough storage space on device to run the App.";
            return true;
        }
        */

        return false;
    }

    public DatabaseManager getDatabaseManager() {
        return dbManager;
    }

    public void setUseReflection(boolean enabled) {
        this.useReflection = enabled;
    }

    public boolean getUseReflection() {
        return useReflection;
    }
}


