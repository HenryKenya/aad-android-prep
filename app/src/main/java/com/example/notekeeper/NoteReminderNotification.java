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
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NoteReminderNotification {
    public static final String CHANNEL_ID = "my_channel_01";

    private static final String NOTIFICATION_TAG = "NoteReminder";

    public static void notify(final Context context, final String noteTitle,
                              final String noteText, int noteId) {
        final Resources res = context.getResources();

        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.logo);

        Intent noteActivityIntent = new Intent(context, NoteActivity.class);
        noteActivityIntent.putExtra(NoteActivity.NOTE_ID, noteId);

        Intent noteListActivityIntent = new Intent(context, NoteListActivity.class);

        Intent backupServiceIntent = new Intent(context, NoteBackupService.class);
        backupServiceIntent.putExtra(NoteBackupService.EXTRA_COURSE_ID, NoteBackup.ALL_COURSES);

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
                                noteActivityIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(
                        0,
                        "View all notes",
                        PendingIntent.getActivity(
                                context,
                                0,
                                noteListActivityIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .addAction(
                        0,
                        "Backup notes",
                        PendingIntent.getService(
                                context,
                                0,
                                backupServiceIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT)
                )
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
