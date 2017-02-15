package io.mon.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.mon.noticationbox.R;
import io.mon.objects.Items_notification;

/**
 * Created by son-tu on 7/10/2016.
 */
public class NoticationManagerAdapter extends ArrayAdapter<Items_notification> {
    private Context context;

    public NoticationManagerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public NoticationManagerAdapter(Context context, int resource, List<Items_notification> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_noticationmanager, null);
        }

        final Items_notification p = getItem(position);

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
            TextView tvAppname = (TextView) v.findViewById(R.id.tvAppname);
            if (tvAppname != null) {
                final PackageManager pm = getContext().getPackageManager();
                ApplicationInfo ai;
                try {
                    ai = pm.getApplicationInfo(p.getPackage(), 0);
                } catch (final PackageManager.NameNotFoundException e) {
                    ai = null;
                }
                final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : p.getPackage());
                tvAppname.setText(applicationName);
            }
            TextView tvContent = (TextView) v.findViewById(R.id.tvContent);
            tvContent.setText(p.getContent());
            TextView tvTime = (TextView) v.findViewById(R.id.tvTime);

            tvTime.setText(getlongtoago(Long.parseLong(p.getTime())));

            TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvTitle.setText(p.getTitle());

        }
        number.add(p.getId());
        return v;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getlongtoago(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
        Date date = null;
        date = new Date(createdAt);
        String crdate1 = dateFormatNeeded.format(date);

        // Date Calculation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        crdate1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + " day ago ";
            } else {
                time = diffDays + " days ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + " hr ago";
                } else {
                    time = diffHours + " hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + " min ago";
                    } else {
                        time = diffMinutes + " mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + " secs ago";
                    }
                }

            }

        }
        return time;
    }
    ArrayList<String> number = new ArrayList<String>();

    public ArrayList<String> item() {
        return number;
    }
}