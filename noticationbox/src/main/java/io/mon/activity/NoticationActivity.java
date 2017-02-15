package io.mon.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.mon.noticationbox.R;
import io.mon.service.NLService;

public class NoticationActivity extends Activity {
    Handler handler = new Handler();
    int i = 0;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    private LinearLayout activity_notication;
    private TextView btnActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notication);
        Intent intent = new Intent(getApplicationContext(), NLService.class);
        getApplicationContext().startService(intent);
        initView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                i = i + 1;
                switch (i) {
                    case 1:
                        imageView6.setVisibility(View.GONE);
                        handler.postDelayed(this, 500);
                        break;
                    case 2:
                        imageView5.setVisibility(View.GONE);
                        handler.postDelayed(this, 500);
                        break;
                    case 3:
                        imageView4.setVisibility(View.GONE);
                        handler.postDelayed(this, 500);
                        break;
                    case 4:
                        imageView3.setVisibility(View.GONE);
                        handler.postDelayed(this, 500);
                        break;
                    case 5:
                        imageView2.setImageResource(R.drawable.notification_box_card_notification);
                        break;
                }
            }
        }, 0);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        activity_notication = (LinearLayout) findViewById(R.id.activity_notication);
        btnActive = (TextView) findViewById(R.id.btnActive);
        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName cn = new ComponentName(getApplicationContext(), NLService.class);
                String flat = Settings.Secure.getString(getApplicationContext().getContentResolver(), "enabled_notification_listeners");
                final boolean enabled = flat != null && flat.contains(cn.flattenToString());
                if(!enabled)
                {
                    Intent intent=new  Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        ComponentName cn = new ComponentName(getApplicationContext(), NLService.class);
        String flat = Settings.Secure.getString(getApplicationContext().getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        if(enabled)
        {
            finish();
        }
    }
}
