package io.mon.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;

import es.dmoral.prefs.Prefs;
import io.mon.sqlite.DatabaseHelper;
import io.mon.util.NotificationB;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLService extends NotificationListenerService {
    private ArrayList<String> notiarray = new ArrayList<String>();
    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    static ArrayList<PendingIntent> arrayList = new ArrayList<PendingIntent>();

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service Running");
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName() + ".NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (Prefs.with(getApplicationContext()).readBoolean("enable_notibox", false)) {
            if (!notiarray.contains(String.valueOf(sbn.getId())) && sbn.getId() != 0 && sbn.getId() > 0) {
                notiarray.add(String.valueOf(sbn.getId()));
            }
            Intent i = new Intent(getPackageName() + ".NOTIFICATION_LISTENER_EXAMPLE");
            i.putExtra("notification_event", "onNotificationPosted :" + sbn.getPackageName() + "\n");
            sendBroadcast(i);
            DatabaseHelper myDbHelper = new DatabaseHelper(getApplicationContext());
            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            Cursor c = myDbHelper.query("SELECT * FROM appblocknoti WHERE package like '%" + sbn.getPackageName() + "%'   LIMIT 0, 50");
            if (c.getCount() > 0) {
            } else {
                if (!sbn.getPackageName().equals(getPackageName())) {
                    try {
                        myDbHelper.ExcuseData("insert into notimanager values('" + sbn.getId() + "','" + sbn.getPackageName() + "','" + sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString() + "','" + sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString() + "','" + sbn.getPostTime() + "')");
                        arrayList.add(sbn.getNotification().contentIntent);
                        NLService.this.cancelNotification(sbn.getKey());
                        NotificationB.createNotification(getApplicationContext());
                    } catch (Exception e) {
                    }
                }
            }
            try {
                System.out.println("ahii " + sbn.getNotification().contentIntent.getIntentSender());
            } catch (Exception e) {
                System.out.println("null");
            }
            myDbHelper.close();
        } else {
            cancelNotification();
        }
    }

    public void cancelNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(6868686); // Notification ID to cancel
    }

    public static ArrayList<PendingIntent> getArray() {
        return arrayList;
    }

    public static void removeArray(int Position) {
        arrayList.remove(Position);
    }

    public static void removeAllItem() {
        arrayList.clear();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "********** onNOtificationRemoved");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new Intent(getPackageName() + ".NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event", "onNotificationRemoved :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("clearall")) {
                NLService.this.cancelAllNotifications();
            } else if (intent.getStringExtra("command").equals("list")) {
                Intent i1 = new Intent(getPackageName() + ".NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event", "=====================");
                sendBroadcast(i1);
                int i = 1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new Intent(getPackageName() + ".NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event", i + " " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new Intent(getPackageName() + ".NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event", "===== Notification List ====");
                sendBroadcast(i3);

            }

        }
    }

}
