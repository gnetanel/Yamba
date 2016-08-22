package yamba.com.example.netanel.yamba;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiConfiguration;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class YambaWidget extends AppWidgetProvider {
    private static final String TAG = YambaWidget.class.getName().toString();
//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {
//
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yamba_widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.i(TAG, "onUpdate");
        Cursor cursor = context.getContentResolver().query(StatusProvider.URI,null, null, null, null);
        cursor.moveToFirst();
        CharSequence user = cursor.getString(cursor.getColumnIndex(StatusData.C_USER));
        CharSequence message = cursor.getString(cursor.getColumnIndex(StatusData.C_TEXT));
        CharSequence createdAt = DateUtils.getRelativeTimeSpanString(context, cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT)));

//        Log.i(TAG, "Succesfully read from contnet provider, user = " + user + ", message = " + message + ", createdAt = " + createdAt);
        Log.i(TAG, String.format("Successfully read from content provider, user = %s, message=%s, create at=%s", user, message, createdAt).toString());
        cursor.getColumnIndex(StatusData.C_USER);
        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yamba_widget);
            views.setTextViewText(R.id.textUser, user);
            views.setTextViewText(R.id.textText, message);
            views.setTextViewText(R.id.textCreatedAt, createdAt);
            views.setOnClickPendingIntent(R.id.yamba_icon,
                            PendingIntent.getActivity(context, 0, new Intent(context,TimelineActivity.class),
                            0));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) { //
        super.onReceive(context, intent);
        if (intent.getAction().equals(ServiceUpdate.UPDATE_INTENT)) { //
            Log.d(TAG, "onReceived detected new status update");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context); //
            this.onUpdate(context, appWidgetManager, appWidgetManager
                    .getAppWidgetIds(new ComponentName(context, YambaWidget.class))); //
        }
    }
}



