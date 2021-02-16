package com.apps.trollino.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.apps.trollino.R;
import com.apps.trollino.ui.main_group.ActivityInPostActivity;
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.user.UpdatePushToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String NOTIFICATION = "NOTIFICATION";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG_LOG, "!!!!!!!!!!!!!!!! From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!!!!!! Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG_LOG, "!!!!!!!!!!!! Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }
    }


    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, ActivityInPostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
}

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d(TAG_LOG, "!!!!!!!!!!!!! New token: " + s);
        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(s);
        super.onNewToken(s);
    }


    public void getFireBaseToken(Context context, PrefUtils prefUtils) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Log.d(TAG_LOG, "Fetching FCM registration token failed " + task.getException());
                        return;
                    }
                    String token = task.getResult();
                    new Thread(() -> UpdatePushToken.sendRegistrationToServer(context, prefUtils, token)).start();
                    Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!!!!!! token " + token);
                });
    }

    public void onDeletedFireBaseToken(Context context, PrefUtils prefUtils) {
        // TODO: Implement this method to remove firebase token
        UpdatePushToken.sendRegistrationToServer(context, prefUtils, "");
//        FirebaseInstanceId.getInstance().deleteInstanceId();
    }
}

