package com.example.csn254.soundprofile;

/**
 * Created by rnehra on 2/4/17.
 */
import com.example.csn254.soundprofile.TableData.TableInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.util.Log;

public class time_slot_db extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;



    public time_slot_db(Context context) {
        super(context, TableInfo.DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database operations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TableInfo.TABLE_NAME+ "("+ TableInfo.DAY + " TEXT, "+TableInfo.START_TIME+" TEXT, "
                +TableInfo.END_TIME+" TEXT); ";
        db.execSQL(query);
        Log.d("Database operations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addSlot(time_slot_db dop,String day,String start_time,String end_time){
        SQLiteDatabase SQ=dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.DAY, day);
        cv.put(TableInfo.START_TIME,start_time);
        cv.put(TableInfo.END_TIME,end_time);
        long k=SQ.insert(TableInfo.TABLE_NAME, null, cv);
        Log.d("Database operations","one Row inserted");
    }


    public Cursor getInformation (time_slot_db dop){
        SQLiteDatabase SQ=dop.getReadableDatabase();
        String [] columns={TableInfo.DAY,TableInfo.START_TIME,TableInfo.END_TIME};
        Cursor CR=SQ.query(TableInfo.TABLE_NAME, columns, null, null, null, null, null);
        return CR;

    }



}