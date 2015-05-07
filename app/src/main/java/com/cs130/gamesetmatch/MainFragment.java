package com.cs130.gamesetmatch;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.facebook.login.widget.ProfilePictureView;

import org.w3c.dom.Text;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {



    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;



    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        /*
        mTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        mProfileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //displayWelcomeMessage(newProfile);
            }
        };
        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
        */
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstaceState){
        super.onViewCreated(view, savedInstaceState);
        LoginButton loginButton = (LoginButton) view.findViewById((R.id.login_button));
        final ProfilePictureView profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePic);
        final TextView mTextDetails = (TextView) view.findViewById(R.id.text_details);
        //loginButton.setReadPermissions("users_friends"); //ask for permission to user's friends
        loginButton.setFragment(this);
        mCallbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                mProfileTracker = new ProfileTracker() {

                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        Log.v("facebook - profile", currentProfile.getFirstName());
                        mTextDetails.setText("Welcome " + currentProfile.getFirstName());
                        String userId = currentProfile.getId();
                        Log.d("userid", userId);

                        profilePictureView.setProfileId(userId);
                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
                mTextDetails.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(FacebookException error) {
                Log.v("facebook - onError", error.getMessage());
            }
        });

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

}
