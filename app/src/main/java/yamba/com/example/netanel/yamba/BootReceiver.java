package yamba.com.example.netanel.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getName().toString();
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "OnReceive");
        YambaApplication yambaApplication = (YambaApplication) context.getApplicationContext();
        int interval = yambaApplication.prefs.getInt("updateInterval", 0);
        //context.startService(new Intent(context, ServiceUpdate.class));
        if (interval == 0){
            return;
        }
        Intent intent1 = new Intent(context, ServiceUpdate.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "About to register updateService using alarm manager...");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(), interval, pendingIntent);
        Log.d(TAG, "Successfully update alarm manager to wake update service every " + intent + " ms");
    }
}
