package com.burnandtremble.escalator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * @author Hermann Loose (hermannloose@gmail.com)
 */
public class C2DMReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
      handleRegistration(context, intent);
    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
      handleMessage(context, intent);
    }
  }

  private void handleMessage(Context context, Intent intent) {
    Intent issue = new Intent(context, IssueActivity.class);
    // TODO(hermannloose): Should maybe perform some sanity checks here?
    issue.putExtras(intent.getExtras());

    PendingIntent notificationIntent =
        PendingIntent.getActivity(context, 0, issue, Intent.FLAG_ACTIVITY_NEW_TASK);

    String ns = Context.NOTIFICATION_SERVICE;
    NotificationManager notificationManager = (NotificationManager) context.getSystemService(ns);

    String tickerText = "New Issue";
    long when = System.currentTimeMillis();
    Notification notification = new Notification(R.drawable.icon, tickerText, when);

    Context applicationContext = context.getApplicationContext();
    String notificationTitle = "New Issue";
    String notificationContent = "You've been assigned a new issue!";

    notification.setLatestEventInfo(
        applicationContext,
        notificationTitle,
        notificationContent,
        notificationIntent);

    notificationManager.notify(1, notification);
  }

  private void handleRegistration(Context context, Intent intent) {
    String registration = intent.getStringExtra("registration_id");
    if (intent.getStringExtra("error") != null) {

    } else if (intent.getStringExtra("unregistered") != null) {
      SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
      SharedPreferences.Editor editor = settings.edit();

      editor.putBoolean("registered", false);
      editor.remove("registration_id");

      editor.commit();

      Toast.makeText(context, "C2DM unregistration successful.", 2).show();
    } else if (registration != null) {
      SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
      SharedPreferences.Editor editor = settings.edit();

      editor.putBoolean("registered", true);
      editor.putString("registration_id", registration);

      editor.commit();

      Toast.makeText(context, "C2DM registration successful.", 2).show();
    }
  }
}
