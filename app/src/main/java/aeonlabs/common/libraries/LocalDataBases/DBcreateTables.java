package aeonlabs.common.libraries.LocalDataBases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBcreateTables extends SQLiteOpenHelper {
    private String[] DB_TABLES;

    public DBcreateTables(Context context, String DB_NAME, int DB_VERSION, String[] DB_TABLES) {
        super(context, DB_NAME, null, DB_VERSION);
        this.DB_TABLES=DB_TABLES;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < this.DB_TABLES.length; i++) {
            db.execSQL(this.DB_TABLES[i]);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * This will be needed only when table's columns have changed by version
         * and this is up to DATABASE_VERSION code
         */
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_LISTING);
        // onCreate(db);
    }
}

