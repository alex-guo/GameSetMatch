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

    //variables passed in from MainFragment
    private String user_id;
    private String session_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        textView = (TextView) findViewById(R.id.TextView01);


        final Profile currentProfile;

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            currentProfile = (Profile) extras.get("currentProfile");
            user_id = (String) extras.get("user_id");
            session_key = (String) extras.get("session_key");


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
                    intent.putExtra("session_key", session_key);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            });

            profilePictureView.setProfileId(currentProfile.getId());
            mTextDetails.setText("Hello there, " + currentProfile.getFirstName());

        }

    }


}
