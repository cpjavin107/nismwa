package com.javinindia.nismwa.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.javinindia.nismwa.R;
import com.javinindia.nismwa.activity.MainActivity;
import com.javinindia.nismwa.preference.SharedPreferencesManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (!TextUtils.isEmpty(remoteMessage.getData().get("profile"))) {
            String profile, banner, message, title, num;
            if (!TextUtils.isEmpty(remoteMessage.getData().get("profile"))) {
                profile = remoteMessage.getData().get("profile");
            } else {
                profile = " ";
            }
            if (!TextUtils.isEmpty(remoteMessage.getData().get("banner"))) {
                banner = remoteMessage.getData().get("banner");
            } else {
                banner = " ";
            }
            if (!TextUtils.isEmpty(remoteMessage.getData().get("message"))) {
                message = remoteMessage.getData().get("message");
            } else {
                message = " ";
            }
            if (!TextUtils.isEmpty(remoteMessage.getData().get("title"))) {
                title = remoteMessage.getData().get("title");
            } else {
                title = " ";
            }
            if (!TextUtils.isEmpty(remoteMessage.getData().get("number"))) {
                num = remoteMessage.getData().get("number");
            } else {
                num = " ";
            }
            String body = remoteMessage.getNotification().getBody();
            sendNotification(profile, banner, message, title, body, num);
        } else {
            //remoteMessage.getNotification().getTitle();
            SharedPreferencesManager.setCount(getApplicationContext(), 1);
            createNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }

        if (remoteMessage.getNotification() != null) {
        }

    }


    private void sendNotification(String pro, String ban, String msg, String title, String body, String num) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("num", num);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (!TextUtils.isEmpty(pro)) {
            notificationBuilder.setLargeIcon(getBitmapfromUrl(pro));
        }
        if (!TextUtils.isEmpty(ban) && !TextUtils.isEmpty(msg)) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapfromUrl(ban)).setSummaryText(msg));
        } else if (!TextUtils.isEmpty(ban) && TextUtils.isEmpty(msg)) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapfromUrl(ban)));
        } else if (TextUtils.isEmpty(ban) && !TextUtils.isEmpty(msg)) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().setSummaryText(msg));
        } else {

        }
        if (!TextUtils.isEmpty(title)) {
            notificationBuilder.setContentTitle(title);
        } else {
            notificationBuilder.setContentTitle(body);
        }
        notificationBuilder.setSmallIcon(R.mipmap.logo);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void createNotification(String messageBody, String tit) {
        String head = "NISMWA";
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        SharedPreferencesManager.setCount(getApplicationContext(), 1);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(largeIcon);
        s.setSummaryText(messageBody);

        if (!TextUtils.isEmpty(tit)) {
            head = tit;
        }
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotiSmallIcon())
                .setContentTitle(head)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());

        Intent intent1 = new Intent("MyData");
        intent1.putExtra("count", "1");
        broadcaster.sendBroadcast(intent1);
    }

    public int getNotiSmallIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return R.drawable.logo_noti;
        } else {
            return R.mipmap.logo;
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}

