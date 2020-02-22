package com.haerul.manado.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.firebase.jobdispatcher.JobService;
import com.haerul.manado.R;
import com.haerul.manado.data.db.MasterDatabase;
import com.haerul.manado.data.db.repository.MasterRepository;
import com.haerul.manado.ui.MainActivity;

public class FirebaseJobService extends JobService {

    private NotificationManager mNotifyManager;
    private MasterRepository repository;
    private String status_diterima;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Util.getBooleanPreference(this, Constants.IS_LOGIN)) {
            Log.d("SERVICE" , "onCreate : " + Util.getTimestampNow());
            repository = new MasterRepository(MasterDatabase.getDatabase(getBaseContext()));
            status_diterima = repository.getRefByValue(Constants.STATUS_GANGGUAN, 1).ref_sid;
        } else {
            Util.stopJobService(this, "job");
        }
    }

    @Override
    public boolean onStartJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        if (Util.getBooleanPreference(this, Constants.IS_LOGIN)) {
            Log.w("TAG", "Job Started " + Util.getTimestampNow());
            
        } else {
            Util.stopJobService(this, "job");
        }
        return false;
    }

    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        Log.w("TAG", "Job Stoped " + Util.getTimestampNow());
        return false;
    }

    //===============================================
    
    
    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel(String channel) {

        // Create a notification manager object.
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (channel,
                            getString(R.string.notification_channel_name),
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    (getString(R.string.notification_channel_description));

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(String channel, String message, String desc, int id) {

        // Set up the pending intent that is delivered when the notification
        // is clicked.
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tag", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(desc);
        bigText.setBigContentTitle("SEULAWAH ⚡ PLN Aceh");
        bigText.setSummaryText(message);
        
        // Build the notification with all of the parameters.
        return new NotificationCompat
                .Builder(this, channel)
                .setContentTitle("SEULAWAH ⚡ PLN Aceh")
                .setContentText(message)
                .setContentInfo(desc)
                .setTicker(desc)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message + "\n" + desc))
                .setSmallIcon(R.drawable.notificationn_icon)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }

    public void cancelNotification(int id) {
        // Cancel the notification.
        mNotifyManager.cancel(id);
    }

    public void sendNotification(String channel, int id, String message, String desc) {
        createNotificationChannel(channel);
        // Build the notification with all of the parameters using helper
        // method.
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(channel, message, desc, id);
        mNotifyManager.notify(id, notifyBuilder.build());
        // Deliver the notification.
    }
}
