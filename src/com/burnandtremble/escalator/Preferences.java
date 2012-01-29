package com.burnandtremble.escalator;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Hermann Loose (hermannloose@gmail.com)
 */
public class Preferences extends PreferenceActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.preferences);
  }
}
