package com.cs130.gamesetmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowContextWrapper;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Alex on 5/27/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)

public class DashboardActivityTest {
    private DashboardActivity dashboardActivity;

    @Before
    public void setup() {
        dashboardActivity = Robolectric.setupActivity(DashboardActivity.class);
    }
    @Test
    public void intentProperly() throws Exception {
        DashboardActivity activity = Robolectric.buildActivity(DashboardActivity.class).create().start().get();
        Intent intent = shadowOf(activity).getNextStartedActivity();
    }

    @Test
    public void opensCorrectly() throws Exception {
        assertTrue(Robolectric.buildActivity(DashboardActivity.class).create().get() != null);
    }

    @Test
    public void playGameButtonVerifyText() throws Exception {
        Button button = (Button) dashboardActivity.findViewById(R.id.play_game_button);
        assertThat(button.getText().toString()).isEqualTo("PLAY A GAME");
    }
    @Test
     public void matchesButtonVerifyText() throws Exception {
        Button button = (Button) dashboardActivity.findViewById(R.id.matches_button);
        assertThat(button.getText().toString()).isEqualTo("See matches");
    }
    @Test
    public void profileButtonVerifyText() throws Exception {
        Button button = (Button) dashboardActivity.findViewById(R.id.profile_button);
        assertThat(button.getText().toString()).isEqualTo("See/Edit Profile");
    }
    @Test
    public void logoutButtonVerifyText() throws Exception {
        Button button = (Button) dashboardActivity.findViewById(R.id.logout_button);
        assertThat(button.getText().toString()).isEqualTo("Logout");
    }
    @Test
    public void textViewVerifyText() throws Exception {
        DashboardActivity dashboardActivity2 = Robolectric.buildActivity(DashboardActivity.class).create().get();
        TextView textView = (TextView) dashboardActivity2.findViewById(R.id.text_details);
        assertThat(textView.getText().toString()).isEqualTo("Hey you!");
    }
}