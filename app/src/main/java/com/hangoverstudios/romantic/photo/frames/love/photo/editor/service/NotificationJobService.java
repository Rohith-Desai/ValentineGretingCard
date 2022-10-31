package com.hangoverstudios.romantic.photo.frames.love.photo.editor.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PersistableBundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SplashActivity;

import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {

    private static final int JOB_ID = 0;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        String firstTime = jobParameters.getExtras().getString("first");

            //Schedule notifications for every 24 hours.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                JobScheduler mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                ComponentName serviceName = new ComponentName(getPackageName(),
                        NotificationJobService.class.getName());
                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName);
                PersistableBundle bundle = new PersistableBundle();
                bundle.putString("first", "false");
                JobInfo myJobInfo = builder
//                        .setMinimumLatency(3600000)
                        .setMinimumLatency(1000*60*60*24)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(bundle)
                        .build();
                mScheduler.schedule(myJobInfo);

                //Send Notification
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Random random = new Random();
                int randomID = random.nextInt(9999 - 1000) + 1000;
                String channelId = "channel-109";
                String GROUP_KEY_WORK_EMAIL = "com.hangoverstudios.romantic.photo.frames.love.photo.editor";
                String channelName = "Romantic frames";
                int importance = NotificationManager.IMPORTANCE_HIGH;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(
                            channelId, channelName, importance);
                    notificationManager.createNotificationChannel(mChannel);
                }

                SharedPreferences prefs = getSharedPreferences("Notifications_Cal_Romant", MODE_PRIVATE);
                NotificationCompat.Builder mBuilder = null;

                mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Romantic frames")
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        .setContentText("Beautify your image with frames");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addNextIntent(intent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                mBuilder.setContentIntent(resultPendingIntent);
                notificationManager.notify(random.nextInt(9999 - 1000) + 1000, mBuilder.build());
            }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
