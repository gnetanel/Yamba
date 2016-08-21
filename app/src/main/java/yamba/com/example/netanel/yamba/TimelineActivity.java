package yamba.com.example.netanel.yamba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineActivity extends BaseActivity {
    ListView listView;
    SQLiteDatabase db;
    StatusData.DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        listView = (ListView)findViewById(R.id.listView);
        dbHelper = yambaApplication.getStatusData().dbHelper;
        db= dbHelper.getReadableDatabase();

        SharedPreferences sharedPref = yambaApplication.prefs;
        String user = sharedPref.getString("username", null);
        if (user == null){
            startActivity(new Intent(this, PrefsActivity.class));
            Toast.makeText(this,"You must define credential to use service", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor curser = db.query(StatusData.TABLE, null, null, null, null, null,StatusData.C_CREATED_AT + " DESC");
        startManagingCursor(curser);

        String[] FROM = {StatusData.C_USER, StatusData.C_CREATED_AT, StatusData.C_TEXT};
        int[] TO = {R.id.textUser, R.id.textCreatedAt, R.id.textText};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.row, curser, FROM, TO);
        //listView.setAdapter(simpleCursorAdapter);

//        TimelineAdaptor timelineAdaptor = new TimelineAdaptor(this, curser);
//         listView.setAdapter(timelineAdaptor);
        //SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, curser, )
//        while(curser.moveToNext()){
//            String user =  curser.getString(curser.getColumnIndex(StatusData.C_USER));
//            String text      =  curser.getString(curser.getColumnIndex(StatusData.C_TEXT));
//
//            String output = String.format("%s: %s\n", user, text); //
//            textView.append(output);
//        }

        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (view.getId() != R.id.textCreatedAt){
                    return false;
                } else {
                    long createdAt = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
                    ((TextView) view).setText(DateUtils.getRelativeTimeSpanString(createdAt));
                    return true;
                }
            }
        };
        simpleCursorAdapter.setViewBinder(viewBinder);
        listView.setAdapter(simpleCursorAdapter);
    }
}
