package com.cs130.gamesetmatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;

/**
 * Created by Alex on 5/8/2015.
 */
public class ProfileViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        final Profile currentProfile;
        final TextView textName = (TextView) findViewById(R.id.textName);

        Button editProfileButton = (Button) findViewById(R.id.edit_profile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            currentProfile = (Profile) extras.get("currentProfile");
            textName.setText("Name: " + currentProfile.getName());
        }

    }
}
