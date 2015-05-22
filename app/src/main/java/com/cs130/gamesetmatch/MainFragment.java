package com.cs130.gamesetmatch;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ProfileTracker mProfileTracker;

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


                        Intent i = new Intent(getActivity().getApplicationContext(), DashboardActivity.class);
                        i.putExtra("currentProfile", currentProfile);
                        startActivity(i);
                        //startActivity(new Intent(getActivity(), DragActivity.class));

                        //profilePictureView.setProfileId(userId);

                        //ImageView fbImage = ((ImageView) profilePictureView.getChildAt(0));

                        //Bitmap bitmap = ((BitmapDrawable) fbImage.getDrawable()).getBitmap();

                        //img.setDefaultProfilePicture(bitmap);
                        //img.setVisibility(View.VISIBLE);


                        /*
                        mOption1.setOnLongClickListener(new OnLongClickListen());
                        mChoice1.setOnDragListener(new ChoiceDragListener(userId));
                        mChoice2.setOnDragListener(new ChoiceDragListener(userId));
                        mChoice3.setOnDragListener(new ChoiceDragListener(userId));
                        mChoice4.setOnDragListener(new ChoiceDragListener(userId));
                        mChoice5.setOnDragListener(new ChoiceDragListener(userId));
                        */

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


    private final class OnLongClickListen implements View.OnLongClickListener {
        public boolean onLongClick(View view) {
            //setup drag
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            //start dragging the item touched
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        }
    }

    private class ChoiceDragListener implements View.OnDragListener {
        String userId;
        public ChoiceDragListener(String userId){
            this.userId=userId;
        }
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //handle drag events
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DROP:


                    //handle the dragged view being dropped over a drop view


                    //handle the dragged view being dropped over a target view
                    View view = (View) event.getLocalState();


                    //stop displaying the view where it was before it was dragged
                    view.setVisibility(View.INVISIBLE);

                    //view dragged item is being dropped on
                    ProfilePictureView dropTarget = (ProfilePictureView) v;

                    //view being dragged and dropped
                    ProfilePictureView dropped = (ProfilePictureView) view;

                    //update the text in the target view to reflect the data being dropped
                    ///ropTarget.setText(dropped.getText());
                    //dropTarget.setVisibility(View.VISIBLE);
                    dropTarget.setProfileId(userId);


                    //make it bold to highlight the fact that an item has been dropped
                    //dropTarget.setTypeface(Typeface.DEFAULT_BOLD);

                    //if an item has already been dropped here, there will be a tag
                    Object tag = dropTarget.getTag();

                    //if there is already an item here, set it back visible in its original place
                    if(tag!=null)
                    {
                        //the tag is the view id already dropped here
                        int existingID = (Integer)tag;
                        //set the original view visible again
                        //findViewById(existingID).setVisibility(View.VISIBLE);
                    }

                    //set the tag in the target view to the ID of the view being dropped
                    dropTarget.setTag(dropped.getId());

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }

            return true;
        }

    }



}
