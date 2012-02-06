package com.burnandtremble.escalator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Hermann Loose (hermannloose@gmail.com)
 */
public class IssueActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.issue_details);

    TextView title = (TextView) findViewById(R.id.issueTitle);
    TextView description = (TextView) findViewById(R.id.issueDescription);

    Intent intent = getIntent();
    if (intent != null) {
      String issueJson = intent.getStringExtra("issue");
      if (issueJson == null) {
        Log.e("Escalator", "Issue was null!");
        description.setText("Intent extra for 'issue' was null!");
        return;
      }
      try{
        JSONObject issue = (JSONObject) new JSONTokener(issueJson).nextValue();

        title.setText(issue.getString("title"));
        description.setText(issue.getString("description"));
      } catch (JSONException e) {
        Log.e("Escalator", "Could not process the issue.", e);

        description.setText(e.getMessage());
      }
    }
  }
}
