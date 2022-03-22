package com.doubleangels.nextdnsmanagement;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.sentry.Sentry;

public class FirebaseNotifications extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            final String FCM_CHANNEL_ID = "fcm";
            NotificationChannel fcm_channel = new NotificationChannel(FCM_CHANNEL_ID, getString(R.string.fcm_channel_name), NotificationManager.IMPORTANCE_HIGH);
            fcm_channel.setDescription(getString(R.string.fcm_channel_description));
            getSystemService(NotificationManager.class)
                    .createNotificationChannel(fcm_channel);

            Notification.Builder notification =
                    new Notification.Builder(this, FCM_CHANNEL_ID)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1, notification.build());
            super.onMessageReceived(remoteMessage);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Sentry.captureException(e);
        }
    }

    @Override
    public void onNewToken(String token) {
        try {
            super.onNewToken(token);
            getSharedPreferences("fcm", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
            Log.e("Set FCM token: ", token);
            FirebaseCrashlytics.getInstance().log("Set FCM token: " + token);
            Sentry.addBreadcrumb("Set FCM token: " + token);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Sentry.captureException(e);
        }
    }
}