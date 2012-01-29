package com.burnandtremble.escalator;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EscalatorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences("escalatorPush", 0);
        if (settings.getBoolean("registered", false)) {
          Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
          registrationIntent.putExtra("app",
              PendingIntent.getBroadcast(this, 0, new Intent(), 0));

          registrationIntent.putExtra("sender", "escalator.test@gmail.com");
          startService(registrationIntent);
        }

        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_menu, menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
        case R.id.register_c2dm_item:
          Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
          registrationIntent.putExtra("app",
              PendingIntent.getBroadcast(this, 0, new Intent(), 0));

          registrationIntent.putExtra("sender", "escalator.test@gmail.com");
          startService(registrationIntent);

          Toast.makeText(this, "C2DM registration request sent.", 2).show();

          return true;
        case R.id.unregister_c2dm_item:
          Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
          unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
          startService(unregIntent);

          Toast.makeText(this, "C2DM unregistration request sent.", 2).show();

          return true;
        default:
          return super.onOptionsItemSelected(item);
      }
    }
}
