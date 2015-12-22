package scot.wildcamping.wildscotland;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.provider.BaseColumns;

public final class Database extends Service {

    private static String DB_NAME ="WildScotlandDB"; // Database name

    public Database() {
    }

    public static abstract class User implements BaseColumns{
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_IDUSERS_ID = "idUsers";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUM_NAME_IDCOLLECTION = "idCollection";
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
