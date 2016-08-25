package yamba.com.example.netanel.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import java.util.List;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;

public class ServiceUpdate extends IntentService {
    private static final String TAG = ServiceUpdate.class.getName().toString();
    private boolean runFlag = false;
//    private Updater updater = null;
    public static final String UPDATE_INTENT = "yamba.com.example.netanel.yamba.UPDATE_INTENT";
    public static final String UPDATE_INTENT_COUNT ="updateCount";
    private NotificationManager notificationManager;
    private Notification notification;

    public ServiceUpdate() {
        super(TAG);
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

    @Override
    protected void onHandleIntent(Intent inIntent) {
        YambaApplication yambaApplication = (YambaApplication) getApplication();
        int i = yambaApplication.fetchStatusUpdates();
        if (i > 0) {
            Log.d(TAG, "Received" + i + " new messages");
            Log.d(TAG, "Broadcasting the intent for action " + UPDATE_INTENT);
            Intent intent = new Intent(UPDATE_INTENT);
            intent.putExtra(UPDATE_INTENT_COUNT, i);
            //sendBroadcast(intent);
            sendBroadcast(intent, "yamba.com.example.netanel.yamba.RECEIEVE_TIMELINE_PERMISSION");

            // notify the topbar.
            notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notification = new Notification(android.R.drawable.stat_notify_chat,"", 0);
            sendTimelineNotification(i);
        } else {
            Log.d(TAG, "No new messages Received");
        }
    }

    private void sendTimelineNotification(int newMessages) {

        Log.d(TAG, "sendTimelineNotification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1,
                new Intent(this, TimelineActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.when = System.currentTimeMillis();
        CharSequence notificationTitle = getText(R.string.msgNotificationTitle);
        CharSequence notificationSummary = getString(R.string.msgNotificationMessage, newMessages);

        /* The below is required but not compiled, need to check alternative way to do that
        this.notification.setLatestEventInfo(this, notificationTitle,
                notificationSummary, pendingIntent); //
        */
        notificationManager.notify(0, this.notification);
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //updater = new Updater();
//        yambaApplication = (YambaApplication)getApplication();
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        updater.interrupt();
//        updater = null;
//        yambaApplication.setServiceRunning(false);
//        Log.d(TAG, "onDestroy");
//    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        Log.d(TAG, "OnStartCommand");
//        runFlag = true;
//        yambaApplication.setServiceRunning(true);
//        updater.start();
//        return START_STICKY;
//    }

//    private class Updater extends Thread{
//        List<Status> timeline;
//
//        public Updater() {
//            super("ServiceUpdate-Updater");
//        }
//        @Override
//        public void run() {
//            super.run();
//
//            ServiceUpdate serviceUpdate = ServiceUpdate.this;
//            while (serviceUpdate.runFlag){
//                Log.d(TAG, "Updater running");
//                try {
//                    int i = yambaApplication.fetchStatusUpdates();
//                    int sleepTime = 30;
//                    if (i > 0) {
//                        Log.d(TAG, "Received" + i + " new messages" );
//                        Log.d(TAG, "Broadcasting the intent for action " + UPDATE_INTENT );
//                        Intent intent = new Intent(UPDATE_INTENT);
//                        intent.putExtra(UPDATE_INTENT_COUNT, i);
//                        //sendBroadcast(intent);
//                        sendBroadcast(intent,"yamba.com.example.netanel.yamba.RECEIEVE_TIMELINE_PERMISSION");
//                    } else {
//                        Log.d(TAG, "No new messages Received");
//                    }
//                    Log.d(TAG, "Going to sleep for " + sleepTime + " seconds");
//                    Thread.sleep(1000 * sleepTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    Log.d(TAG, "Got InterruptedException, stop loopoing...");
//                    serviceUpdate.runFlag = false;
//                }
//            }
//        }
//    }
}
