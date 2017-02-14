package io.mon.noticationbox;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.mon.noticationbox.adapter.AppInstalledAdapter;
import io.mon.noticationbox.adapter.NoticationManagerAdapter;
import io.mon.noticationbox.interface_mon.inNotificationBox;
import io.mon.noticationbox.objects.Items_app;
import io.mon.noticationbox.objects.Items_notification;
import io.mon.noticationbox.service.NotificationListenerService;
import io.mon.noticationbox.sqlite.DatabaseHelper;
import io.mon.noticationbox.util.NotificationB;

/**
 * Created by Son on 1/14/2017.
 */

public class NotificationBox extends RelativeLayout implements inNotificationBox {
    private LayoutInflater mInflater;
    Context context;
    View views;
    String title;
    private TextView tvTitle;
    private Switch switchBar;
    private LinearLayout swPlace;
    String IDKey;
    private LinearLayout notibox;
    private GridView gr_data;
    private LinearLayout layout_content;
    private ImageView btnDelete;
    private ImageView btnNoti;
    private ImageView btnAppNoti;
    private ArrayList<Items_app> arrayapp;
    ;

    public NotificationBox(Context context) {
        super(context);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public NotificationBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    public NotificationBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        DatabaseHelper myDbHelper = new DatabaseHelper(getContext());
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.noticationbox);
        boolean showbackpress = attr.getBoolean(R.styleable.noticationbox_ShowBackPress, false);
        views = mInflater.inflate(R.layout.layout_notication_box, this, true);
        gr_data = (GridView) views.findViewById(R.id.gr_data);
        btnDelete = (ImageView) views.findViewById(R.id.btnDelete);
        btnNoti = (ImageView) views.findViewById(R.id.btnNoti);
        btnAppNoti = (ImageView) views.findViewById(R.id.btnAppNoti);
        layout_content = (LinearLayout) views.findViewById(R.id.layout_content);
        notibox = (LinearLayout) views.findViewById(R.id.notibox);
        btnDelete.setVisibility(View.GONE);
        btnNoti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                notibox.setVisibility(VISIBLE);
                getNotiUnRead();
            }
        });
        btnAppNoti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getApp();
                notibox.setVisibility(GONE);
            }
        });
        gr_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseHelper myDbHelper = new DatabaseHelper(getContext());
                try {
                    myDbHelper.openDataBase();
                    Cursor c = myDbHelper.query("SELECT * FROM appblocknoti WHERE package like '%" + arrayapp.get(i).getPackage() + "%'   LIMIT 0, 50");
                    if (c.getCount() <= 0) {
                        myDbHelper.ExcuseData("insert into appblocknoti values('" + arrayapp.get(i).getPackage().toString() + "')");
                    } else {
                        myDbHelper.ExcuseData("delete from appblocknoti where package='" + arrayapp.get(i).getPackage().toString() + "'");
                    }
                    myDbHelper.close();
                    requestData();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        });
    }

    private void getApp() {
        requestData();
    }

    AppInstalledAdapter appInstalledAdapter;

    private void requestData() {
        arrayapp = new ArrayList<Items_app>();
        final PackageManager pm = getContext().getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : apps) {
            if (pm.getLaunchIntentForPackage(app.packageName) != null) {
                // apps with launcher intent
                if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                } else {
                    if (!getContext().getPackageName().equals(app.packageName)) {
                        Items_app items_app = new Items_app(app.packageName, null, null);
                        arrayapp.add(items_app);
                    }
                }
            }
        }
        appInstalledAdapter = new AppInstalledAdapter(getContext(), R.layout.layout_app_installed, arrayapp);
        gr_data.setAdapter(appInstalledAdapter);

    }

    private void getNotiUnRead() {
        getData();
    }

    ArrayList<Items_notification> arrayList;
    DatabaseHelper myDbHelper;

    public void getData() {
        myDbHelper = new DatabaseHelper(context);
        arrayList = new ArrayList<Items_notification>();
        try {
            myDbHelper.openDataBase();
            Cursor c = myDbHelper.query("SELECT * FROM notimanager ");
            if (c.moveToFirst()) {
                do {
                    Items_notification items_notification = new Items_notification(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                    arrayList.add(items_notification);
                    NotificationB.cancelNotification(context);
                } while (c.moveToNext());
                notibox.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                NotificationB.cancelNotification(context);
                btnDelete.setVisibility(View.GONE);
                notibox.setVisibility(View.VISIBLE);

            }
            final NoticationManagerAdapter adapter = new NoticationManagerAdapter(getContext(), R.layout.layout_noticationmanager, arrayList);
            gr_data.setAdapter(adapter);
            gr_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<PendingIntent> p = NotificationListenerService.getArray();
                    try {
                        PendingIntent pIntent = p.get(i);
                        if (pIntent != null) {
                            try {
                                pIntent.send(getContext(), 0, new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                DatabaseHelper myDbHelper = new DatabaseHelper(getContext());
                                try {
                                    myDbHelper.openDataBase();
                                    myDbHelper.ExcuseData("delete from notimanager where id='" + arrayList.get(i).getId() + "'");
                                    myDbHelper.close();
                                    adapter.remove(adapter.getItem(i));
                                    NotificationListenerService.removeArray(i);
                                    adapter.notifyDataSetChanged();
                                    NotificationB.createNotification(getContext());
                                    if (adapter.getCount() == 0) {
                                        btnDelete.setVisibility(View.GONE);
                                        notibox.setVisibility(View.VISIBLE);
                                    }
                                } catch (SQLException sqle) {
                                    throw sqle;
                                }
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        } else {
                            adapter.remove(adapter.getItem(i));
                            NotificationListenerService.removeArray(i);
                            adapter.notifyDataSetChanged();
                            if (adapter.getCount() == 0) {
                                NotificationB.cancelNotification(context);
                                btnDelete.setVisibility(View.GONE);
                                notibox.setVisibility(View.VISIBLE);
                            }
                            NotificationB.createNotification(getContext());
                        }
                    } catch (Exception e) {
                        DatabaseHelper myDbHelper = new DatabaseHelper(getContext());
                        myDbHelper.openDataBase();
                        myDbHelper.ExcuseData("delete from notimanager where id='" + arrayList.get(i).getId() + "'");
                        myDbHelper.close();
                        adapter.remove(adapter.getItem(i));

                        try {
                            NotificationListenerService.removeArray(i);
                        } catch (Exception ex) {
                        }
                        NotificationB.createNotification(getContext());
                        adapter.notifyDataSetChanged();
                        if (adapter.getCount() == 0) {
                            NotificationB.cancelNotification(context);
                            btnDelete.setVisibility(View.GONE);
                            notibox.setVisibility(View.VISIBLE);
                        }
                    }

                }
            });
        } catch (SQLException sqle) {
            throw sqle;
        }

    }

}
