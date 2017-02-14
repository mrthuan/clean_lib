package io.mon.noticationbox.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.mon.noticationbox.service.NLService;
import io.mon.noticationbox.util.ServiceUtil;

/**
 * Created by trungkientn on 10/28/16.
 */

public class ReBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceUtil.startService(context, NLService.class);
    }
}
