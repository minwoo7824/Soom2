package com.kmw.soom2.Common;

import android.provider.BaseColumns;

public class AlarmDataBase {



    public static final class CreateDB implements BaseColumns {
        public static final String USERID = "drugAlarm";
        public static final String SELECT_DAY = "selectDay";
        public static final String SELECT_TIME = "selectTime";
        public static final String PUSH_CHECK = "pushCheck";
        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +USERID+" text not null , "
                +SELECT_DAY+" text not null , "
                +SELECT_TIME+" integer not null , "
                +PUSH_CHECK+" integer not null );";

    }
}
