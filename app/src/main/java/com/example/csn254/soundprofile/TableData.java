package com.example.csn254.soundprofile;

import android.provider.BaseColumns;

public class TableData {
    public TableData(){}

    //  abstract class mapping various strings to their respective  variables

    public static abstract class TableInfo implements BaseColumns{
        public static final String DAY="slot_day";
        public static final String START_TIME_REQ_ID="start_time_req_id";
        public static final String END_TIME_REQ_ID="end_time_req_id";
        public static final String START_TIME="start_time";
        public static final String END_TIME="end_time";
        public static final String DATABASE_NAME="time_slots";
        public static final String TABLE_NAME="time_slots";
    }
}