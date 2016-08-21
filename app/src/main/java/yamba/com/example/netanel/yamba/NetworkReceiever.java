package yamba.com.example.netanel.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkReceiever extends BroadcastReceiver {
    public NetworkReceiever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean networkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (networkDown == true){
            context.stopService(new Intent(context, ServiceUpdate.class));
        } else {
            context.startService(new Intent(context, ServiceUpdate.class));
        }
    }
}
