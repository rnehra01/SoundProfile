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
import android.widget.Toast;

public class time_slot_db extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;



    public time_slot_db(Context context) {
        super(context, TableInfo.DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database operations", "Database created");
    }
    // oncreate function- to initialize the database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TableInfo.TABLE_NAME+ "("+ TableInfo.DAY + " TEXT, "
                +TableInfo.START_TIME_REQ_ID+" INT, "
                +TableInfo.END_TIME_REQ_ID+" INT, "
                +TableInfo.START_TIME+" TEXT, "
                +TableInfo.END_TIME+" TEXT); ";
        db.execSQL(query);
        Log.d("Database operations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

        // To add new time slot to the existing list ; input is day, start time ,end time and their respective ID's
    public boolean addSlot(time_slot_db dop,String day,int start_time_req_id,int end_time_req_id,String start_time,String end_time){

        if (Exists(dop, day, start_time, end_time)){
            return false;
        }else {
            SQLiteDatabase SQ=dop.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TableInfo.DAY, day);
            cv.put(TableInfo.START_TIME,start_time);
            cv.put(TableInfo.END_TIME,end_time);
            cv.put(TableInfo.START_TIME_REQ_ID,start_time_req_id);
            cv.put(TableInfo.END_TIME_REQ_ID,end_time_req_id);
            long k=SQ.insert(TableInfo.TABLE_NAME, null, cv);
            Log.d("Database operations","one Row inserted"+day+start_time+end_time);
            return true;
        }

    }

        // to get information about all the entries present in the list
    public Cursor getInformation (time_slot_db dop){
        SQLiteDatabase SQ=dop.getReadableDatabase();
        String [] columns={TableInfo.DAY,TableInfo.START_TIME,TableInfo.END_TIME,TableInfo.START_TIME_REQ_ID,TableInfo.END_TIME_REQ_ID};
        Cursor CR=SQ.query(TableInfo.TABLE_NAME, columns, null, null, null, null, null);
        return CR;

    }
        // function to delete the selected timeslot from the list
    public void deleteSlot(time_slot_db dop, String day,String start_time,String end_time){
        String selection = TableInfo.DAY+ " LIKE ?" + " AND " + TableInfo.START_TIME + " LIKE ?" + " AND " + TableInfo.END_TIME + " LIKE ?";
        //Log.e("err", selection+"-"+day+"-"+start_time+"-"+end_time);
        String args[] = {day, start_time, end_time};
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.delete(TableInfo.TABLE_NAME, selection, args);
    }
        // To check whether a time slot exists in the list or not to avoid duplicate entry in list
    public boolean Exists(time_slot_db dop, String day, String start_time,String end_time){
        String columns [] ={TableInfo.START_TIME, TableInfo.END_TIME};
        String selection = TableInfo.START_TIME + " LIKE ? AND " + TableInfo.END_TIME + " LIKE ?";
        String args [] = {start_time, end_time};
        String limit = "1";

        SQLiteDatabase SQ = dop.getWritableDatabase();
        Cursor cursor = SQ.query(TableInfo.TABLE_NAME, columns, selection, args, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        Log.e("DBEXISTS", ""+exists);
        return exists;
    }

}