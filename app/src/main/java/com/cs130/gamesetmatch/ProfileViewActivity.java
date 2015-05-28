package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;


public class ProfileViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        final Profile currentProfile;
        final TextView textName = (TextView) findViewById(R.id.textName);
        final TextView textHeight = (TextView) findViewById(R.id.textHeight);
        final Bundle extras = getIntent().getExtras();

        if(extras != null) {

            /* Enter starting values into fields */
            currentProfile = (Profile) extras.get("currentProfile");
            textName.setText(currentProfile.getName());
            textHeight.setText("Enter a height in cm!");

             /* Redirect to Dashboard */
            Button logoutButton = (Button) findViewById(R.id.updateProfile_btn);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileViewActivity.this, DashboardActivity.class);
                    intent.putExtra("currentProfile", (Profile) extras.get("currentProfile"));

                /* Display success toast */
                    Toast toast = Toast.makeText(getApplicationContext(), "Profile Saved!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(intent);
                }
            });

        }


    }
}
