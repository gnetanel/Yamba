package yamba.com.example.netanel.yamba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getName().toString();
    YambaApplication yambaApplication;
    StatusData.DbHelper dbHelper;
    StatusData statusData;
    SQLiteDatabase readableDatabase;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yambaApplication = (YambaApplication) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //
        inflater.inflate(R.menu.menu, menu); //
        return true;
    }

    // Called when an options item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"Option selected, value = " + item.getItemId());
        switch (item.getItemId()) { //
            case R.id.itemPrefs:
                Log.d(TAG,"Selected item is itemPrefs, launching activity...");
                startActivity(new Intent(this, PrefsActivity.class)); //
                break;
            case R.id.itemServiceStart:
                Log.d(TAG,"Start service from option menu");
                startService(new Intent(this,ServiceUpdate.class));
                break;
            case R.id.itemServiceStop:
                Log.d(TAG,"Stop service from option menu");
                stopService(new Intent(this,ServiceUpdate.class));
                break;
            case R.id.itemGoTotimeline:
                Log.d(TAG,"Open timeline activity");
                startActivity(new Intent(this, TimelineActivity.class));
                break;
            default:
                Log.d(TAG,"Unknown option");
                break;
        }
        return true; //
    }
}

/**
 yamba.com.example.netanel.yamba_preferences.xml
 root@generic_x86:/data/data/yamba.com.example.netanel.yamba/shared_prefs #
 cat yamba.com.example.netanel.yamba_preferences.xml
 <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
 <map>
 <string name="username">student</string>
 <string name="password">password</string>
 <string name="apiRoot">rrrrrr</string>
 </map>
 */
