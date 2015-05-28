package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

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
 * Created by Alex on 5/7/2015.
 */
public class DashboardActivity extends Activity {
    private TextView textView;

    //variables to pass to backend
    private String name, email, password, token;

    //variables returned from backend
    private String user_id;
    private String session_key;

    private String haveID;
    private String[] server = new String[]{"http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        textView = (TextView) findViewById(R.id.TextView01);


        final Profile currentProfile;

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            currentProfile = (Profile) extras.get("currentProfile");
            haveID = (String) extras.get("haveID");
            name = currentProfile.getName();
            email = "asdf@gmail.com";
            password = "asdfpassword";
            token = currentProfile.getId();

            Log.d("haveIDstr", haveID);

            if (haveID.equals("false")){
                Log.d("IDstr", "newID");
                haveID="true";
                ConnectionTask task  = new ConnectionTask();
                task.execute(server);
            }
            else{
                Log.d("IDstr", "noIDneeded");
            }


            final ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
            final TextView mTextDetails = (TextView) findViewById(R.id.text_details);
            Button logoutButton = (Button) findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            Button gameButton = (Button) findViewById(R.id.play_game_button);
            gameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(DashboardActivity.this, DragActivity.class);
                    Intent intent = new Intent (DashboardActivity.this, WaitGameActivity.class);
                    intent.putExtra("currentProfile", currentProfile);

                    intent.putExtra("user_id", user_id);
                    intent.putExtra("session_key", session_key);
                    startActivity(intent);
                }
            });

            Button profileButton = (Button) findViewById(R.id.profile_button);
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, ProfileViewActivity.class);
                    intent.putExtra("currentProfile", currentProfile);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("session_key", session_key);
                    startActivity(intent);
                }
            });
            Button viewMatchesButton = (Button) findViewById(R.id.matches_button);
            viewMatchesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, ViewMatchesActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("session_key", session_key);
                    startActivity(intent);
                }
            });

            profilePictureView.setProfileId(currentProfile.getId());
            mTextDetails.setText("Hello there, " + currentProfile.getFirstName());

        }

    }


    private class ConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("action", "common.reg"));
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("token", token));

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
                Log.d("strConnection", str);


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
                session_key = json.getString("session_key");
                user_id = json.getString("user_id");


                textView.setText(json.getString("user_id"));
                //textView.setText(json.getString("session_key"));

                user_id = json.getString("user_id");
                session_key = json.getString("session_key");
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}