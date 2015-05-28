package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.util.Log;
import android.view.Gravity;
>>>>>>> edit_profile
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


public class ProfileViewActivity extends Activity {
    private String name;
    private String token;
    private String age;
    private String about;
    private String gender;
    private String height;
    private String ethnicity;

    private String sexualOrientationPref;
    private String ethnicityPref;
    private String agePref;
    private String heightPref;

    private String newAbout, newAge, newGender;
    private String updateProfStatus, updateProfMessage;

    private String newGenderPref, newEduPref, newEthnoPref, newHeightPref;
    private String updatePreferencesStatus, getUpdatePreferencesMessage;

    String user_id;
    String session_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        final Profile currentProfile;
        final TextView textName = (TextView) findViewById(R.id.textName);

        final TextView textHeight = (TextView) findViewById(R.id.textHeight);
        final Bundle extras = getIntent().getExtras();

        if(extras != null) {

            /* Enter starting values into fields from database through common.getPreferences*/
            GetProfileTask getProfileTask  = new GetProfileTask();
            getProfileTask.execute(new String[]{"http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com"});

            PrefConnectionTask prefConnectionTask  = new PrefConnectionTask();
            prefConnectionTask.execute(new String[]{"http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com"});

            currentProfile = (Profile) extras.get("currentProfile");
            textName.setText(currentProfile.getName());
            textHeight.setText("Enter a height in cm!");

             /* Redirect to Dashboard */
            Button updateProfileButton = (Button) findViewById(R.id.updateProfile_btn);
            updateProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileViewActivity.this, DashboardActivity.class);
                    intent.putExtra("currentProfile", (Profile) extras.get("currentProfile"));

                    //getProfile, setPreferences using their changes through updateProfile and updatePreferences

                    UpdateProfConnectionTask updateProfConnectionTask  = new UpdateProfConnectionTask();
                    updateProfConnectionTask.execute(new String[]{"http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com"});

                    UpdatePreferencesConnectionTask updatePreferencesConnectionTask  = new UpdatePreferencesConnectionTask();
                    updatePreferencesConnectionTask.execute(new String[]{"http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com"});



                /* Display success toast */
                    Toast toast = Toast.makeText(getApplicationContext(), "Profile Saved!", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(intent);
                }
            });

        }

    }

    private class GetProfileTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("action", "common.getProfile"));
                nameValuePairs.add(new BasicNameValuePair("name", "asdf"));
                nameValuePairs.add(new BasicNameValuePair("session_key", "asdf@gmail.com"));
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
            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject json = new JSONObject(result);

                //textView.setText(json.getString("user_id"));
                name = json.getString("name");
                token = json.getString("token");
                age = json.getString("age");
                about = json.getString("about");
                gender = json.getString("gender");
                height = json.getString("height");
                ethnicity = json.getString("ethnicity");


            } catch (JSONException e){
                e.printStackTrace();
            }
        }


    }



    private class PrefConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("action", "common.getPreferences"));
                nameValuePairs.add(new BasicNameValuePair("name", "asdf"));
                nameValuePairs.add(new BasicNameValuePair("session_key", "asdf@gmail.com"));
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
            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject json = new JSONObject(result);

                //textView.setText(json.getString("user_id"));
                sexualOrientationPref = json.getString("sexual_orientation");
                ethnicityPref = json.getString("ethnicity");
                agePref = json.getString("age");
                heightPref = json.getString("height");

            } catch (JSONException e){
                e.printStackTrace();
            }
        }


    }


    private class UpdateProfConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("action", "common.updateProfile"));
                nameValuePairs.add(new BasicNameValuePair("about", newAbout));
                nameValuePairs.add(new BasicNameValuePair("age", newAge));
                nameValuePairs.add(new BasicNameValuePair("gender", newGender));

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
            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject json = new JSONObject(result);

                updateProfStatus = json.getString("status");
                updateProfMessage = json.getString("message");

            } catch (JSONException e){
                e.printStackTrace();
            }
        }

    }


    private class UpdatePreferencesConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("action", "common.updatePreferences"));
                nameValuePairs.add(new BasicNameValuePair("gender_pref",newGenderPref ));
                nameValuePairs.add(new BasicNameValuePair("educationLevel", newEduPref));
                nameValuePairs.add(new BasicNameValuePair("ethnicity", newEthnoPref));
                nameValuePairs.add(new BasicNameValuePair("height", newHeightPref));

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
            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONObject json = new JSONObject(result);

                updatePreferencesStatus = json.getString("status");
                getUpdatePreferencesMessage = json.getString("message");

            } catch (JSONException e){
                e.printStackTrace();
            }
        }


    }
}
