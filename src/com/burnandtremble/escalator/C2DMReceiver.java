package com.burnandtremble.escalator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
    // TODO(hermannloose): Implement C2DMReceiver#handleMessage.
  }

  private void handleRegistration(Context context, Intent intent) {
    String registration = intent.getStringExtra("registration_id");
    if (intent.getStringExtra("error") != null) {

    } else if (intent.getStringExtra("unregistered") != null) {
      SharedPreferences settings = context.getSharedPreferences("escalatorPush", 0);
      SharedPreferences.Editor editor = settings.edit();

      editor.putBoolean("registered", false);
      editor.remove("registration_id");

      editor.commit();
    } else if (registration != null) {
      SharedPreferences settings = context.getSharedPreferences("escalatorPush", 0);
      SharedPreferences.Editor editor = settings.edit();

      editor.putBoolean("registered", true);
      editor.putString("registration_id", registration);

      editor.commit();
    }
  }
}
