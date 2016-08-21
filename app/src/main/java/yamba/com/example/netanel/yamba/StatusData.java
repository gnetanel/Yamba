package yamba.com.example.netanel.yamba;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusData {
    static final String TAG = DbHelper.class.getName().toString();
    static final String DB_NAME = "timeline.db"; //
    static final int    DB_VERSION = 1; //
    static final String TABLE = "timeline"; //
    static final String C_ID = BaseColumns._ID;
    static final String C_CREATED_AT = "created_at";
    static final String C_SOURCE = "source";
    static final String C_TEXT = "txt";
    static final String C_USER = "user";
    static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";
    static final String[] MAX_CREATED_AT_COLUMNS = { "max("
            + StatusData.C_CREATED_AT + ")" };
    static final String[] DB_ARRAY_COLUMN = {C_TEXT};


    final DbHelper dbHelper;

    public StatusData(Context context){
        dbHelper = new DbHelper(context);
    }

    public void insertOrIgnore(ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        } finally{
            db.close();
        }
    }

    public Cursor getStatusUpdates(){
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        return db.query(TABLE, null, null, null, null, null, GET_ALL_ORDER_BY);
    }

    public long getLatestStatusCreatedAtTime() { //
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null, null,
                    null, null);
            try {
                return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
            } finally {
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    public String getStatusTextById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE, DB_ARRAY_COLUMN, C_ID + "=" + id, null, null, null, null);
        return cursor.moveToNext() ? cursor.getString(0): null;
    }

    public class DbHelper extends SQLiteOpenHelper {
        Context context;

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "CREATE TABLE " + TABLE + " (" +
                    (C_ID + " int primary key, " +
                            C_CREATED_AT + " int, " +
                            C_SOURCE + " text, " +
                            C_USER + " int ," +
                            C_TEXT + " text")
                    + ")";
            Log.i(TAG, "About to create database, sql command = " + sql);
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.d(TAG, "OnUpgrade");
            Log.i(TAG, "Delete previous table, and re-creating it by calling onCreate manually.");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}

