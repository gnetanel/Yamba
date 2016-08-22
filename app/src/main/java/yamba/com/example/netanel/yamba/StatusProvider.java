package yamba.com.example.netanel.yamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.net.URI;

public class StatusProvider extends ContentProvider {
    private static final String TAG = StatusProvider.class.getName().toString();
    StatusData statusData;
    public static final Uri URI = Uri.parse("content://yamba.com.example.netanel.yamba.statusprovider/status");

    static final String SINGLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/yamba.com.example.netanel.yamba.status";
    static final String MULTIPLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/yamba.com.example.netanel.yamba.status";


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete");
        SQLiteDatabase db = statusData.dbHelper.getWritableDatabase();
        try {
            long id = getId(uri);
            if (id < 0) {
                return db.delete(StatusData.TABLE, selection, selectionArgs);
            } else {
                // delete specific entry
                return db.delete(StatusData.TABLE, StatusData.C_ID + "=" + StatusData.C_ID, null);
            }
        } finally {
            db.close();
        }
    }

    @Override
    public String getType(Uri uri) {
        if (getId(uri) < 0){
            return MULTIPLE_RECORD_MIME_TYPE;
        } else {
            return SINGLE_RECORD_MIME_TYPE;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "insert");
        SQLiteDatabase db = statusData.dbHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(StatusData.TABLE, null, values);
            if (id < 0) {
                Log.e(TAG, "Error inserting " + values + " to database");
                throw new RuntimeException(String.format("%s Fail to insert values %s into table %s for unknown reason", TAG, values, StatusData.TABLE).toString());
            } else {
                return ContentUris.withAppendedId(uri, id);
            }
        } finally {
            db.close();
        }
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        statusData = new StatusData(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query");
        SQLiteDatabase db = statusData.dbHelper.getReadableDatabase();
        long id = getId(uri);
        if (id < 0) {
            return db.query(StatusData.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        } else {
            return db.query(StatusData.TABLE, projection, StatusData.C_ID + "=" + id, null, null, null, null);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.i(TAG, "update");
        SQLiteDatabase db = statusData.dbHelper.getWritableDatabase();
        try {
            long id = getId(uri);
            if (id < 0) {
                return db.update(StatusData.TABLE, values, selection, selectionArgs);
            } else {
                return db.update(StatusData.TABLE, values, StatusData.C_ID + "=" + id, null);
            }
        } finally {
            db.close();
        }
    }

    private long getId(Uri uri){
        String idStr = uri.getLastPathSegment();
        if (idStr != null){
            try {
                return  Long.parseLong(idStr);
            } catch (Exception e){
            }
        }
        return -1;
    }
}

