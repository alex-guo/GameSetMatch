package com.cs130.gamesetmatch;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.Profile;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 5/8/2015.
 */
public class ViewMatchesActivity extends Activity {
    //private TextView textView1;
    //private TextView textView2;
    //private TextView textView3;
    private String session_key = null;
    private String user_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);

        //textView1 = (TextView) findViewById(R.id.TextView01);
        //textView2 = (TextView) findViewById(R.id.TextView02);
        //textView3 = (TextView) findViewById(R.id.TextView03);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Profile currentProfile = (Profile) extras.get("currentProfile");
            session_key = (String) extras.get("session_key");
            user_id = (String) extras.get("user_id");
            //textView1.setText(session_key);
            //textView2.setText(user_id);

            ConnectionTask task  = new ConnectionTask();
            task.execute(new String[]{"http://ec2-52-10-172-62.us-west-2.compute.amazonaws.com"});

        }
    }


    private class ConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-10-172-62.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("action", "common.getMatches"));
                nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
                nameValuePairs.add(new BasicNameValuePair("session_key", session_key));
                httpPostReq.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("URL", httpPostReq.toString());
                // Execute HTTP Post Request
                //HttpResponse response = httpclient.execute(httppost);
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }


            try{
                HttpResponse httpResponse = httpClient.execute(httpPostReq);
                String str = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                Log.d("str", str);


                return str;
            }  catch (IOException e){
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject json = new JSONObject(result);

                //textView3.setText(json.getString("matches"));

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /* from xml--for demo purposes
    <TextView
    android:id="@+id/TextView01"
    android:layout_width="match_parent"
    android:layout_height="87dp"
    android:layout_alignParentLeft="true"
    android:layout_alignParentStart="true">
    </TextView>

    <TextView
    android:id="@+id/TextView02"
    android:layout_width="match_parent"
    android:layout_height="87dp"
    android:layout_below="@+id/TextView01"
    android:layout_alignParentLeft="true"
    android:layout_alignParentStart="true">
    </TextView>

    <TextView
    android:id="@+id/TextView03"
    android:layout_width="match_parent"
    android:layout_height="87dp"
    android:layout_below="@+id/TextView02"
    android:layout_alignParentLeft="true"
    android:layout_alignParentStart="true">
    </TextView>
    */

}
