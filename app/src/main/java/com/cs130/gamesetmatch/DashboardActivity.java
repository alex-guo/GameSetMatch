package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by Alex on 5/7/2015.
 */
public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final Profile currentProfile;

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            currentProfile = (Profile) extras.get("currentProfile");


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
                    Intent intent = new Intent(DashboardActivity.this, DragActivity.class);
                    intent.putExtra("currentProfile", currentProfile);
                    startActivity(intent);
                }
            });

            Button profileButton = (Button) findViewById(R.id.profile_button);
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, ProfileViewActivity.class);
                    intent.putExtra("currentProfile", currentProfile);
                    startActivity(intent);
                }
            });
            Button viewMatchesButton = (Button) findViewById(R.id.matches_button);
            viewMatchesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, ViewMatchesActivity.class);
                    startActivity(intent);
                }
            });

            profilePictureView.setProfileId(currentProfile.getId());
            mTextDetails.setText("Hello there, " + currentProfile.getFirstName());

        }

    }
}
