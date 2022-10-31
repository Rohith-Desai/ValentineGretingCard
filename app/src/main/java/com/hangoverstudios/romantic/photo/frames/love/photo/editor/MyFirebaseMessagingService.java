package com.hangoverstudios.romantic.photo.frames.love.photo.editor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String CHANNEL_ID = "romant_stories";
    String CHANNEL_NAME = "romatic_frames";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Here we can handle the pushed notification
        //message will have the content that is pushed by the firebase

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.getNotification()
                        .getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setChannelId(CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}