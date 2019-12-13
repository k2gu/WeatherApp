package p.kirke.weatherapp.pushnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import p.kirke.weatherapp.util.Const;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(Const.NOTIFICATION);
        int id = intent.getIntExtra(Const.NOTIFICATION_ID, 0);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(Const.NOTIFICATION_CHANNEL_ID, Const.NOTIFICATION_CHANNEL_NAME, importance);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(id, notification);
        }
    }
}
