package p.kirke.weatherapp.pushnotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import p.kirke.weatherapp.MainActivity;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.util.Const;

public class NotificationHandler {

    private static NotificationHandler notificationHandler;

    public static NotificationHandler getInstance() {
        if (notificationHandler == null) {
            notificationHandler = new NotificationHandler();
        }
        return notificationHandler;
    }

    public void scheduleNotification(Context context) {
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(Const.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(Const.NOTIFICATION, getNotification(context));
        if (!alreadyHasTheNotificationScheduled(context, notificationIntent)) {
            setUpAlarmManager(context, notificationIntent);
        }
    }

    private boolean alreadyHasTheNotificationScheduled(Context context, Intent notificationIntent) {
        return PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private void setUpAlarmManager(Context context, Intent notificationIntent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private Notification getNotification(Context context) {
        return new NotificationCompat.Builder(context, Const.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(getExecuteAppPendingIntent(context))
                .setChannelId(Const.NOTIFICATION_CHANNEL_ID)
                .build();
    }

    private PendingIntent getExecuteAppPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
