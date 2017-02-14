package io.mon.noticationbox.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.mon.noticationbox.R;
import io.mon.noticationbox.objects.Items_app;
import io.mon.noticationbox.sqlite.DatabaseHelper;

/**
 * Created by son-tu on 7/10/2016.
 */
public class AppInstalledAdapter extends ArrayAdapter<Items_app> {
    private Context context;

    public AppInstalledAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AppInstalledAdapter(Context context, int resource, List<Items_app> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_app_installed, null);
        }

        final Items_app p = getItem(position);

        if (p != null) {
            ImageView img = (ImageView) v.findViewById(R.id.imgIcon);
            if (img != null) {
                Drawable d = null;
                try {
                    d = getContext().getPackageManager().getApplicationIcon(p.getPackage());
                    img.setBackgroundDrawable(d);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            TextView appname = (TextView) v.findViewById(R.id.tvApkName);
            if (appname != null) {

                final PackageManager pm = getContext().getPackageManager();
                ApplicationInfo ai;
                try {
                    ai = pm.getApplicationInfo(p.getPackage(), 0);
                } catch (final PackageManager.NameNotFoundException e) {
                    ai = null;
                }
                final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : p.getPackage());
                appname.setText(applicationName);
            }
            ImageView imgStatus = (ImageView) v.findViewById(R.id.imStatus);
            DatabaseHelper myDbHelper = new DatabaseHelper(getContext());
            try {
                myDbHelper.openDataBase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            Cursor c = myDbHelper.query("SELECT * FROM appblocknoti WHERE package like '%" + p.getPackage() + "%'   LIMIT 0, 50");
            if (c.getCount() > 0) {
                imgStatus.setImageResource(R.drawable.ic_on);
            } else {
                imgStatus.setImageResource(R.drawable.ic_result_off);
            }
            myDbHelper.close();
        }

        return v;
    }
}