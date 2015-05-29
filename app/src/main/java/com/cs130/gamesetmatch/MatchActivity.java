package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;


public class MatchActivity extends Activity {
    private Profile currentProfile;
    private String user_id;
    private String session_key;


    private ProfilePictureView match_picture;
    private TextView match_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        match_picture = (ProfilePictureView)findViewById(R.id.profilePic);
        match_picture.setProfileId("100000059978309");
        match_name = (TextView) findViewById(R.id.match_name);
        match_name.setText("Christopher Lim");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = (String)extras.get("user_id");
            session_key = (String)extras.get("session_key");
            currentProfile = (Profile) extras.get("currentProfile");
            Button dashButton = (Button) findViewById(R.id.backToDash_button);

            dashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MatchActivity.this, DashboardActivity.class);
                    intent.putExtra("currentProfile", currentProfile);
                    intent.putExtra("haveID", "true");
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("session_key", session_key);
                    startActivity(intent);
                }
            });
        }
    }


}
