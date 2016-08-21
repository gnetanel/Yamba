package yamba.com.example.netanel.yamba;

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
        context.startService(new Intent(context, ServiceUpdate.class));
    }
}
