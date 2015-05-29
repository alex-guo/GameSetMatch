package com.cs130.gamesetmatch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WaitGameActivity extends ActionBarActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    String session_key;
    String user_id;
    Profile currentProfile;

    private String[] server = new String[]{"http://ec2-52-10-172-62.us-west-2.compute.amazonaws.com"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_game);

        textView1 = (TextView) findViewById(R.id.TextView01);
        textView2 = (TextView) findViewById(R.id.TextView02);
        textView3 = (TextView) findViewById(R.id.TextView03);



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            currentProfile = (Profile) extras.get("currentProfile");
            session_key = (String) extras.get("session_key");
            user_id = (String) extras.get("user_id");
            textView1.setText(session_key);
            textView2.setText(user_id);

            ConnectionTask task  = new ConnectionTask();
            task.execute(server);

        }

        Button gameButton = (Button) findViewById(R.id.play_game_button);
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(DashboardActivity.this, DragActivity.class);
                Intent intent = new Intent (WaitGameActivity.this, DragActivity.class);
                intent.putExtra("currentProfile", currentProfile);
                intent.putExtra("session_key", session_key);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wait_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                nameValuePairs.add(new BasicNameValuePair("action", "game.registerGame"));
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
                Log.d("strWaitGame", str);


                return str;
            } catch (IOException e){
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject json = new JSONObject(result);

                textView3.setText(json.getString("message"));


                //textView.setText(json.getString("user_id"));
                //textView.setText(json.getString("session_key"));


            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }



    private class ConnectionTaskContinuous extends AsyncTask<String, Void, String> {

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
                nameValuePairs.add(new BasicNameValuePair("action", "game.startGame"));
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
            } catch (IOException e){
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject json = new JSONObject(result);

                textView3.setText(json.getString("message"));


                //textView.setText(json.getString("user_id"));
                //textView.setText(json.getString("session_key"));


            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
