package io.mon.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.mon.noticationbox.R;
import io.mon.activity.NoticationActivity;
import io.mon.activity.ViewNotificationActivity;
import io.mon.service.NLService;

/**
 * Created by Son on 2/10/2017.
 */

public class NotificationB {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void createnoti(Context context) {
        Intent intent = new Intent(context, NoticationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notibox)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText((context.getString(R.string.youhave)) + " " + String.valueOf(NLService.getArray().size()) + " " + (context.getString(R.string.str_notiboxx)))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(6868686, b.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void createNotification(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.layout_notication);
        Intent settingIntent = new Intent(context, ViewNotificationActivity.class);
        contentView.setOnClickPendingIntent(R.id.btnSettings, PendingIntent.getActivity(context, 4, settingIntent, 0));
        contentView.setTextViewText(R.id.tvCount, String.valueOf(NLService.getArray().size()));
        contentView.setTextViewText(R.id.tvTime, String.valueOf(currentDateandTime));
        contentView.setImageViewResource(R.id.imgIcon, R.drawable.ic_noticationbox);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContent(contentView);
        builder.setSmallIcon(R.drawable.ic_noticationbox);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;
        notification.audioStreamType = AudioManager.STREAM_NOTIFICATION;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(6868686, builder.build());
    }

    public static void cancelNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(6868686); // Notification ID to cancel
    }

}
