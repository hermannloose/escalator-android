package com.burnandtremble.escalator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.widget.TextView;

/**
 * @author Hermann Loose (hermannloose@gmail.com)
 */
public class BackendRegistrationTask extends AsyncTask<Void, Void, String> {
  private String host;
  private String port;
  private String token;
  private String registrationId;
  private TextView responseView;

  public BackendRegistrationTask(
      String host,
      String port,
      String token,
      String registrationId,
      TextView responseView) {

    this.host = host;
    this.port = port;
    this.token = token;
    this.registrationId = registrationId;
    this.responseView = responseView;
  }

  @Override
  protected void onPreExecute() {
    responseView.setText("Making request to " + host + ":" + port + "/profile/contact_details"
        + "?auth_token=" + token);
  }

  @Override
  protected String doInBackground(Void... arg0) {
    String rootUri = host + ":" + port;
    String tokenParam = "?auth_token=" + token;

    DefaultHttpClient httpClient = new DefaultHttpClient();

    String uriForUserId = rootUri + "/profile.json" + tokenParam;
    HttpGet getUserId = new HttpGet(uriForUserId);

    StringBuilder profileJsonString = new StringBuilder();
    try {
      HttpResponse response = httpClient.execute(getUserId);
      InputStream content = response.getEntity().getContent();

      final char[] buffer = new char[0x10000];

      InputStreamReader reader = new InputStreamReader(content);
      int read = 0;
      while (read >= 0) {
        read = reader.read(buffer, 0, buffer.length);
        if (read > 0) {
          profileJsonString.append(buffer, 0, read);
        }
      };
    } catch (ClientProtocolException e) {
      e.printStackTrace();

      return e.getMessage();
    } catch (IOException e) {
      e.printStackTrace();

      return e.getMessage();
    }

    int userId = -1;
    try {
      JSONObject profile = (JSONObject) new JSONTokener(profileJsonString.toString()).nextValue();
      userId = profile.getInt("id");
    } catch (JSONException e) {
      e.printStackTrace();

      return e.getMessage();
    }

    String uri = host + ":" + port + "/profile/contact_details"
        + "?auth_token=" + token;

    HttpPost httpPost = new HttpPost(uri);
    httpPost.addHeader("Accept", "application/json");
    List<NameValuePair> params = new ArrayList<NameValuePair>(1);
    params.add(new BasicNameValuePair("contact_detail[user_id]", Integer.toString(userId)));
    params.add(new BasicNameValuePair("contact_detail[name]", "Android"));
    params.add(new BasicNameValuePair("contact_detail[category]", "android"));
    params.add(new BasicNameValuePair("contact_detail[details]",
        "{\"registration_id\":\"" + registrationId + "\"}"));

    try {
      httpPost.setEntity(new UrlEncodedFormEntity(params));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();

      return e.getMessage();
    }

    try {
      HttpResponse response = httpClient.execute(httpPost);
      InputStream content = response.getEntity().getContent();

      final char[] buffer = new char[0x10000];
      StringBuilder out = new StringBuilder();
      InputStreamReader reader = new InputStreamReader(content);
      int read = 0;
      while (read >= 0) {
        read = reader.read(buffer, 0, buffer.length);
        if (read > 0) {
          out.append(buffer, 0, read);
        }
      };

      return out.toString();
    } catch (ClientProtocolException e) {
      e.printStackTrace();

      return e.getMessage();
    } catch (IOException e) {
      e.printStackTrace();

      return e.getMessage();
    }
  }

  @Override
  protected void onPostExecute(String result) {
    responseView.setText(result);
    super.onPostExecute(result);
  }
}
