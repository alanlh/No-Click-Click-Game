package edu.illinois.finalproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Alan Hu on 12/12/2017.
 */

public class ShowNotification extends Service {
  
  public static final int NOTIFICATION_CODE = 0;
  private final String NOTIFICATION_TITLE = "Click again!";
  private final String NOTIFICATION_MESSAGE = "Thirty minutes is up!";
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    
    // From https://developer.android.com/training/notify-user/build-notification.html
    // https://developer.android.com/guide/topics/ui/notifiers/notifications.html#Design
  
    Intent mainActivityIntent = new Intent(this, MainActivity.class);
    PendingIntent resultPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_CODE,
      mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  
    //https://stackoverflow.com/questions/15809399/android-notification-sound
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
  
    NotificationCompat.Builder mDelayedNotification =
      new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(NOTIFICATION_MESSAGE)
        .setContentIntent(resultPendingIntent)
        .setAutoCancel(true)
        .setSound(alarmSound);
    
    NotificationManager mNotificationManager =
      (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    
    mNotificationManager.notify(NOTIFICATION_CODE, mDelayedNotification.build());
    
    return super.onStartCommand(intent, flags, startId);
  }
  
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
