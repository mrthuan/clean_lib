package io.mon.noticationbox.activity;

import android.app.Activity;
import android.os.Bundle;

import io.mon.noticationbox.R;

public class ViewNotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);
    }
}
