package yamba.com.example.netanel.yamba;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;


public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = YambaApplication.class.getName().toString();
    private Twitter twitter;
    private SharedPreferences prefs;
    private boolean serviceRunning = false;
    private StatusData statusData;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        statusData = new StatusData(this);
    }

    public StatusData getStatusData() {
        return statusData;
    }

    public synchronized int fetchStatusUpdates() {
        int count = 0;
        List<Status> timeline = getTwitter().getFriendsTimeline();
        ContentValues values = new ContentValues();

        long latestTime = statusData.getLatestStatusCreatedAtTime();
        try {
            for (Status status : timeline) {
                // Loop over the timeline and print it out
                values.clear();
                values.put(StatusData.C_ID, status.id.toString());
                values.put(StatusData.C_CREATED_AT, status.getCreatedAt().getTime());
                values.put(StatusData.C_SOURCE, status.getSource());
                values.put(StatusData.C_USER, status.getUser().name);
                values.put(StatusData.C_TEXT, status.getText());
                long createdAt = status.getCreatedAt().getTime();
                if (createdAt > latestTime) {
                    count++;
                }
                getStatusData().insertOrIgnore(values);
                Log.i(TAG, String.format("%s %s", status.user.name, status.text));
            }
            Log.d(TAG, "New messages = " + count);
        } catch (RuntimeException e) {
            Log.e(TAG, "Fail to fetch updates");
        }
        return count;
    }

    public synchronized Twitter getTwitter(){
        if (twitter == null){
            //String username = prefs.getString("username", "");
            //String username = "student";
            //String password = prefs.getString("password", "");
            //String password = "password";
            //String apiRoot  = prefs.getString("apiRoot", "http://yamba.newcircle.com/api");
            String apiRoot  = "http://yamba.newcircle.com/api";
            String readUsername = prefs.getString("username", "");
            String readPassword = prefs.getString("password","");
            //String readApiRoot = prefs.getString("apiRoot","");
            Log.i(TAG, "readUsername = " + readUsername);
            Log.i(TAG, "readPassword = " + readPassword);
            // Log.i(TAG, "readApiRoot = " + readApiRoot);

            twitter = new Twitter(readUsername, readPassword);
            twitter.setAPIRootUrl(apiRoot);
        }
        return twitter;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        twitter = null;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }

}
