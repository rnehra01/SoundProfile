package com.example.csn254.soundprofile;

import android.provider.BaseColumns;

public class TableData {
    public TableData(){}

    public static abstract class TableInfo implements BaseColumns{
        public static final String DAY="slot_day";
        public static final String START_TIME="start_time";
        public static final String END_TIME="end_time";
        public static final String DATABASE_NAME="time_slots";
        public static final String TABLE_NAME="time_slots";
    }
}