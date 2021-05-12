package com.example.user.photogallery;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2019/10/7.
 */

public class PollService extends IntentService {

    private static final String TAG = "PollService";

    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

    public static final String ACTION_SHOW_NOTIFICATION = "com.example.user.photogallery" +
            ".SHOW_NOTIFICATION";

    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";
    //set inteval to 1
    // minute

    public static final String PERM_PRIVATE = "com.example.user.photogallery.PRIVATE";

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime
                    (), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        QueryPreferences.setAlarmOn(context, isOn);
    }

    public static boolean isServiceAlarm(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }


    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!isNetWorkAvailableAndConnected()) {
            return;
        } //check connection to network
        //Log.i(TAG, "Received an intent: " + intent);
        String query = QueryPreferences.getStoredQuery(this);
        String lastResultId = QueryPreferences.getLastResulted(this);
        List<GalleryItem> items;
        if (query == null) {
            items = new FlickrFetchr().fetchRecentPhotos();
        } else {
            items = new FlickrFetchr().searchPhotos(query);
        }
        if (items.size() == 0) {
            return;
        }
        String resultId = items.get(0).getId();
        if (resultId.equals(lastResultId)) {
            Log.i(TAG, "Got an old result: " + resultId);
        } else {
            Log.i(TAG, "Got a new result: " + resultId);
            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(this).setTicker(resources
                    .getString(R.string.new_pictures_title)).setSmallIcon(android.R.drawable
                    .ic_menu_report_image).setContentTitle(resources.getString(R.string
                    .new_pictures_title)).setContentText(resources.getString(R.string
                    .new_pictures_text)).setContentIntent(pi).setAutoCancel(true).build();

//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(0, notification);
            Log.i(TAG, "-----Notification start");

//            String CHANNEL_ID = "channel_1";
//            Resources resources = getResources();
//            Intent i = PhotoGalleryActivity.newIntent(this);
//            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//            Notification.Builder builder = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                builder = new Notification
//                        .Builder(this, CHANNEL_ID)
//                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
//                        .setContentTitle(resources.getString(R.string.new_pictures_title))
//                        .setContentText(resources.getString(R.string.new_pictures_text))
//                        .setContentIntent(pi)
//                        .setAutoCancel(true);
//                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
//                        getString(R.string.new_pictures_title), NotificationManager
//                        .IMPORTANCE_DEFAULT);
//                assert mNotificationManager != null;
//                mNotificationManager.createNotificationChannel(channel);
//
//            } else {
//                builder = new Notification
//                        .Builder(this)
//                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
//                        .setContentTitle(resources.getString(R.string.new_pictures_title))
//                        .setContentText(resources.getString(R.string.new_pictures_text))
//                        .setContentIntent(pi)
//                        .setAutoCancel(true);
//            }

            //   mNotificationManager.notify(0, builder.build());

            //sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE);
            showBackgroundNotification(0, notification);
            Log.i(TAG, "-----Notification finished");
        }
        QueryPreferences.setLastResultId(this, resultId);
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    private boolean isNetWorkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
