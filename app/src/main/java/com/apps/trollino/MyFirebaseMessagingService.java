package com.apps.trollino;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.apps.trollino.ui.main_group.ActivityInPostActivity;
import com.apps.trollino.ui.main_group.TapeActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String NOTIFICATION = "NOTIFICATION";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String myCustomKey = data.get("my_custom_key");
        Log.d(TAG_LOG, "!!!!!!!!!!!!!!!! From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!!!!!! Message data payload: " + remoteMessage.getData());

            String extras = remoteMessage.getData().get("extra");
            Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!! extras " + extras);

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }
        }

        Intent intent = null;
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG_LOG, "!!!!!!!!!!!! Message Notification Body: " + remoteMessage.getNotification().getBody());
            intent.putExtra(NOTIFICATION, true);
            intent = new Intent(this, TapeActivity.class);
        } else { // open a link
            intent.putExtra(NOTIFICATION, false);
            intent = new Intent(this, TapeActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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


    public void getFireBaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Log.d(TAG_LOG, "Fetching FCM registration token failed " + task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!!!!!! token " + token);

                    // Log and toast
//                    String msg = getString(R.string.msg_token_fmt, token);
//                    Log.d(TAG, msg);
//                    showToast(msg);
                });
    }
}
