package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.Profile;


public class MatchActivity extends Activity {
    private Profile currentProfile;
    private String user_id;
    private String session_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);


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
