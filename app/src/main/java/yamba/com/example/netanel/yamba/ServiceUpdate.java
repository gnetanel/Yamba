package yamba.com.example.netanel.yamba;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import java.util.List;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;

public class ServiceUpdate extends Service {
    private static final String TAG = ServiceUpdate.class.getName().toString();
    private boolean runFlag = false;
    private Updater updater = null;
    private YambaApplication yambaApplication;



    public ServiceUpdate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updater = new Updater();
        yambaApplication = (YambaApplication)getApplication();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updater.interrupt();
        updater = null;
        yambaApplication.setServiceRunning(false);
        Log.d(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "OnStartCommand");
        runFlag = true;
        yambaApplication.setServiceRunning(true);
        updater.start();
        return START_STICKY;
    }

    private class Updater extends Thread{
        List<Status> timeline;

        public Updater() {
            super("ServiceUpdate-Updater");
        }
        @Override
        public void run() {
            super.run();

            ServiceUpdate serviceUpdate = ServiceUpdate.this;
            while (serviceUpdate.runFlag){
                Log.d(TAG, "Updater running");
                try {
                    int i = yambaApplication.fetchStatusUpdates();
                    int sleepTime = 30;
                    if (i > 0) {
                        Log.d(TAG, "Received" + i + " new messages" );
                    } else {
                        Log.d(TAG, "No new messages Received");
                    }
                    Log.d(TAG, "Going to sleep for " + sleepTime + " seconds");
                    Thread.sleep(1000 * sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Got InterruptedException, stop loopoing...");
                    serviceUpdate.runFlag = false;
                }
            }
        }
    }
}
