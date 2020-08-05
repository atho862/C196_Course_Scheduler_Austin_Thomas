package com.example.c196_course_scheduler_austin_thomas.Receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.example.c196_course_scheduler_austin_thomas.Activities.CourseListActivity;
import com.example.c196_course_scheduler_austin_thomas.Activities.HomeActivity;
import com.example.c196_course_scheduler_austin_thomas.Activities.TermListActivity;
import com.example.c196_course_scheduler_austin_thomas.R;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String EXTRA_NOTIFICATION_TITLE =
            "com.example.c196_course_scheduler_austin_thomas.Receivers.EXTRA_NOTIFICATION_TITLE";
    public static final String EXTRA_NOTIFICATION_DATA =
            "com.example.c196_course_scheduler_austin_thomas.Receivers.EXTRA_NOTIFICATION_DATA";
    public static final String EXTRA_NOTIFICATION_TYPE =
            "com.example.c196_course_scheduler_austin_thomas.Receivers.EXTRA_NOTIFICATION_TYPE";
    private static final String CHANNEL_ID = "Course Scheduler";
    public static final String CHANNEL_DESCRIPTION = "Degree and Course progress tracker";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
        String notificationData = intent.getStringExtra(EXTRA_NOTIFICATION_DATA);
        String notificationType = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE);
        Intent resultIntent;

        switch (notificationType) {
            case "COURSE":
            case "ASSESSMENT":
                resultIntent = new Intent(context, CourseListActivity.class);
                break;
            case "TERM":
                resultIntent = new Intent(context, TermListActivity.class);
                break;
            default:
                resultIntent = new Intent(context, HomeActivity.class);
        }

        TaskStackBuilder builder = TaskStackBuilder.create(context);
        builder.addParentStack(HomeActivity.class);
        builder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = builder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle(notificationTitle);
        notificationBuilder.setContentText(notificationData);
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DESCRIPTION, NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
