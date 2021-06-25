package aeonlabs.common.libraries.Network.database;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import aeonlabs.common.libraries.logger.CrashReports;


public class LocalDatabaseOperations {

    private static final String TAG = LocalDatabaseOperations.class.getName();
    private static LocalDatabase mDb;
    private static EntityQueue queueIn;
    private static EntityFields fieldsIn;
    private static EntityFiles filesIn;

    private static List<EntityQueue> queueOutList,queueInList;
    private static List<EntityFields> fieldsOutList, fieldsInList;
    private static List<EntityFiles> filesOutList, filesInList;
    public  static Boolean error;
    public  static String ErrMessage="";
    private static Activity activity;


    public LocalDatabaseOperations(Activity _activity){
        mDb=LocalDatabase.getAppDatabase(_activity);
        activity=_activity;
    }

    public String getErrMessage(){ return ErrMessage; }

    public Boolean getError(){ return error; }

    public static List<EntityQueue> getQueue() {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground( final Void ... params ) {
                    queueOutList = mDb.daoDatabase().getAllQueues();
                    return null;
                }
                @Override
                protected void onPostExecute( final Void result ) {
                    // continue what you are doing...
                }
            }.execute().get();
            error=false;
            return queueOutList;
        }catch (Exception e){
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
            error=true;
            return null;
        }
    }

    public static Boolean addQueue( EntityQueue _queue, List<EntityFields> _fields, List<EntityFiles> _files) {
        queueIn=_queue;
        fieldsInList=_fields;
        filesInList=_files;
        error=true;
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground( final Void ... params ) {
                    if (fieldsIn != null ){
                        mDb.daoDatabase().insertQueue(queueIn);
                        int code = mDb.daoDatabase().GetQueueCode(queueIn.getTitle(), queueIn.getDescription(), queueIn.getUrl());
                        for(int i=0; i<fieldsInList.size();i++){
                            fieldsInList.get(i).setCodQueue(code);
                            mDb.daoDatabase().insertField(fieldsInList.get(i));
                        }
                        if(filesIn != null){
                            for(int i=0; i<filesInList.size();i++){
                                filesInList.get(i).setCodQueue(code);
                                mDb.daoDatabase().insertFile(filesInList.get(i));
                            }
                        }
                        error=false;
                    }else{
                        ErrMessage="Fields not found (add queue)";
                    }
                    return null;
                }
                @Override
                protected void onPostExecute( final Void result ) {
                    // continue what you are doing...
                }
            }.execute().get();
            return !error;
        }catch (Exception e){
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
            return false;
        }
    }

    public static Boolean delQueue(EntityQueue _queue) {
        queueIn=_queue;
        error=true;
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground( final Void ... params ) {
                    mDb.daoDatabase().deleteQueue(queueIn.getCodQueue());
                    mDb.daoDatabase().deleteFieldByQueue(queueIn.getCodQueue());
                    mDb.daoDatabase().deleteFileByQueue(queueIn.getCodQueue());
                    error=false;
                    return null;
                }
                @Override
                protected void onPostExecute( final Void result ) {
                    // continue what you are doing...
                }
            }.execute().get();
            return !error;
        }catch (Exception e){
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
            error=true;
            return null;
        }
    }
// FIELDS

    public static List<EntityFields> getFields(EntityQueue _queue) {
        queueIn= _queue;
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground( final Void ... params ) {
                    fieldsOutList = mDb.daoDatabase().getAllFields(queueIn.getCodQueue());
                    return null;
                }
                @Override
                protected void onPostExecute( final Void result ) {
                    // continue what you are doing...
                }
            }.execute().get();
            error=false;
            return fieldsOutList;
        }catch (Exception e){
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
            error=true;
            return null;
        }
    }

    // FILES
    public static List<EntityFiles> getFiles(EntityQueue _queue) {
        queueIn= _queue;
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground( final Void ... params ) {
                    filesOutList = mDb.daoDatabase().getAllFiles(queueIn.getCodQueue());
                    return null;
                }
                @Override
                protected void onPostExecute( final Void result ) {
                    // continue what you are doing...
                }
            }.execute().get();
            error=false;
            return filesOutList;
        }catch (Exception e){
            CrashReports crashReports = new CrashReports();
            crashReports.SaveCrash(e, activity);
            error=true;
            return null;
        }
    }
}
