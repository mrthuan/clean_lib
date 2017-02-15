package io.mon.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by trungkientn on 10/28/16.
 */

public class ServiceUtil {
    public static boolean isRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Context context, Class<?> serviceclass){
        if(ServiceUtil.isRunning(context, serviceclass)){
            System.out.println(serviceclass.getName() + " is Running!");
            return;
        }
        System.out.println("Start Service: " + serviceclass.getName());
        try {
            Intent service = new Intent(context, serviceclass);
            context.startService(service);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
