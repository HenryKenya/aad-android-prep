package com.example.notekeeper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NoteReminderNotification {
    public static final String CHANNEL_ID = "my_channel_01";

    private static final String NOTIFICATION_TAG = "NoteReminder";

    public static void notify(final Context context, final String noteTitle,
                              final String noteText) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.logo);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "01")

                .setDefaults(Notification.DEFAULT_ALL)

                .setSmallIcon(R.drawable.ic_stat_note_reminder)
                .setContentTitle("Review note")
                .setContentText(noteText)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .setLargeIcon(picture)

                .setTicker("Review note")

                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(noteText)
                        .setBigContentTitle(noteTitle)
                        .setSummaryText("Review note"))

                // .setNumber(number)

                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                // handle oreo
                .setChannelId(CHANNEL_ID)

                .setAutoCancel(true);

        notify(context, builder.build());
    }

    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            int notifyID = 1;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, NOTIFICATION_TAG, importance);
            nm.createNotificationChannel(channel);
            nm.notify(notifyID, notification);

        }
    }

    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
