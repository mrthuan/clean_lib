package mon.io.lib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import es.dmoral.prefs.Prefs;
import io.mon.noticationbox.NotificationBox;

public class MainActivity extends AppCompatActivity {

    private NotificationBox notificationBox;
    private RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Prefs.with(getApplicationContext()).writeBoolean("enable_notibox", true);
        notificationBox.Build();
        notificationBox.OnbackPress(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here
            }
        });
    }

    private void initView() {
        notificationBox = (NotificationBox) findViewById(R.id.notificationBox);
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
    }
}
