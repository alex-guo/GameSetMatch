package com.cs130.gamesetmatch;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    //variables to pass to backend
    private String name, email, password, token;

    //variables to pass to dashboard activity
    private String user_id;
    private String session_key;

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("login", "onSuccess");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null){
                Log.d("Name", profile.getFirstName());
            }
        }

        @Override
        public void onCancel() {
            Log.d("login", "onCancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("login", "onError " + e);
        }
    };

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        mCallbackManager=CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();


        /*
        mProfileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //displayWelcomeMessage(newProfile);
            }
        };

        mProfileTracker.startTracking();
        */


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);
        final LoginButton loginButton = (LoginButton) view.findViewById((R.id.login_button));

        /*
        final ProfilePictureView profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePic);

        final TextView mTextDetails = (TextView) view.findViewById(R.id.text_details);

        //final ProfilePictureView img = (ProfilePictureView) view.findViewById(R.id.imageView1);
        final ProfilePictureView mOption1 = (ProfilePictureView)view.findViewById(R.id.profilePic);
        final ProfilePictureView mChoice1 = (ProfilePictureView) view.findViewById(R.id.choice_1);
        final ProfilePictureView mChoice2 = (ProfilePictureView) view.findViewById(R.id.choice_2);
        final ProfilePictureView mChoice3 = (ProfilePictureView) view.findViewById(R.id.choice_3);
        final ProfilePictureView mChoice4 = (ProfilePictureView) view.findViewById(R.id.choice_4);
        final ProfilePictureView mChoice5 = (ProfilePictureView) view.findViewById(R.id.choice_5);

        mOption1.setPresetSize(ProfilePictureView.SMALL);
        mChoice1.setPresetSize(ProfilePictureView.SMALL);
        mChoice2.setPresetSize(ProfilePictureView.SMALL);
        mChoice3.setPresetSize(ProfilePictureView.SMALL);
        mChoice4.setPresetSize(ProfilePictureView.SMALL);
        mChoice5.setPresetSize(ProfilePictureView.SMALL);
        */

        //loginButton.setReadPermissions("users_friends"); //ask for permission to user's friends
        loginButton.setFragment(this);
        mCallbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;


            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                mProfileTracker = new ProfileTracker() {

                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        Log.v("facebook - profile", currentProfile.getFirstName());
                        //mTextDetails.setText("Welcome " + currentProfile.getFirstName());
                        String userId = currentProfile.getId();
                        Log.d("userid", userId);

                        name = currentProfile.getName();
                        email = "asdf@gmail.com";
                        password = "asdfpassword";
                        token = currentProfile.getId();

                        ConnectionTask task  = new ConnectionTask();
                        task.execute(new String[]{"http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com"});


                        Intent i = new Intent(getActivity().getApplicationContext(), DashboardActivity.class);
                        i.putExtra("currentProfile", currentProfile);
                        i.putExtra("user_id", user_id);
                        i.putExtra("session_key", session_key);
                        startActivity(i);

                        mProfileTracker.stopTracking();


                    }
                };
                mProfileTracker.startTracking();
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
                //mTextDetails.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(FacebookException error) {
                Log.v("facebook - onError", error.getMessage());
            }
        });

    }


    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("setupTokenTracker", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("setupProfileTracker", "" + currentProfile);
            }
        };
    }

    private void setupLoginButton(View view) {
        LoginButton mButtonLogin = (LoginButton) view.findViewById(R.id.login_button);
        mButtonLogin.setFragment(this);
        //mButtonLogin.setReadPermissions("user_friends");
        mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);
    }
    /*
    @Override
    public void onResume(){
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);

    }

    */

    /*
    @Override
    public void onDestroy(){
        super.onDestroy();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();

    }
    */


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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


                //textView.setText(json.getString("user_id"));
                //textView.setText(json.getString("session_key"));


                Log.d("MAINstrID", user_id);
                Log.d("MAINstrKEY", session_key);


            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
