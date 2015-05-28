package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class DragActivity extends Activity {
    private TextView textView;

    private String session_key = null;
    private String user_id = null;
    private Profile currentProfile;


    private ProfilePictureView mOption1;
    private ProfilePictureView mOption2;
    private ProfilePictureView mOption3;
    private ProfilePictureView mOption4;
    private ProfilePictureView mOption5;

    private ProfilePictureView mChoice1;
    private ProfilePictureView mChoice2;
    private ProfilePictureView mChoice3;
    private ProfilePictureView mChoice4;
    private ProfilePictureView mChoice5;

    private String[] otherUsers = new String[5];

    private String[] server =  new String[] {"http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);


        new CountDownTimer(10000, 1) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                // Here do what you like...
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.putExtra("session_key", session_key);
                intent.putExtra("user_id", user_id);
                intent.putExtra("currentProfile", currentProfile);

                startActivity(intent);
            }
        }.start();

        textView = (TextView) findViewById(R.id.TextView01);

        final TextView mTextDetails = (TextView) findViewById(R.id.text_details);

        mOption1 = (ProfilePictureView)findViewById(R.id.option_1);
        mOption2 = (ProfilePictureView)findViewById(R.id.option_2);
        mOption3 = (ProfilePictureView)findViewById(R.id.option_3);
        mOption4 = (ProfilePictureView)findViewById(R.id.option_4);
        mOption5 = (ProfilePictureView)findViewById(R.id.option_5);

        mChoice1 = (ProfilePictureView)findViewById(R.id.choice_1);
        mChoice2 = (ProfilePictureView)findViewById(R.id.choice_2);
        mChoice3 = (ProfilePictureView)findViewById(R.id.choice_3);
        mChoice4 = (ProfilePictureView)findViewById(R.id.choice_4);
        mChoice5 = (ProfilePictureView)findViewById(R.id.choice_5);


        mOption1.setPresetSize(ProfilePictureView.SMALL);
        mOption2.setPresetSize(ProfilePictureView.SMALL);

        mChoice1.setPresetSize(ProfilePictureView.SMALL);
        mChoice2.setPresetSize(ProfilePictureView.SMALL);
        mChoice3.setPresetSize(ProfilePictureView.SMALL);
        mChoice4.setPresetSize(ProfilePictureView.SMALL);
        mChoice5.setPresetSize(ProfilePictureView.SMALL);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentProfile = (Profile) extras.get("currentProfile");
            user_id = (String) extras.get("user_id");
            session_key = (String) extras.get("session_key");

            //get other users through startGame
            StartGameConnectionTask startGameConnectionTask  = new StartGameConnectionTask();
            startGameConnectionTask.execute(server);


            //go through array of users
            //get other users through getProfile and set information
            //pass in array of information to ClickListener
            GetProfileTask getProfileTask  = new GetProfileTask();
            getProfileTask.execute(server);

            String userID=currentProfile.getId();

            mTextDetails.setText("Welcome " + currentProfile.getFirstName());

            mOption1.setProfileId(currentProfile.getId());
            mOption2.setProfileId("10152998692153003");
            mOption3.setProfileId("10152998692153003");
            mOption4.setProfileId("10152998692153003");
            mOption5.setProfileId("10152998692153003");


            mOption1.setOnLongClickListener(new OnLongClickListen());
            mOption2.setOnLongClickListener(new OnLongClickListen());
            mOption3.setOnLongClickListener(new OnLongClickListen());
            mOption4.setOnLongClickListener(new OnLongClickListen());
            mOption5.setOnLongClickListener(new OnLongClickListen());

            mOption1.setOnClickListener(new ClickListener());
            mOption2.setOnClickListener(new ClickListener());
            mOption3.setOnClickListener(new ClickListener());
            mOption4.setOnClickListener(new ClickListener());
            mOption5.setOnClickListener(new ClickListener());


            mChoice1.setOnDragListener(new ChoiceDragListener(userID));
            mChoice2.setOnDragListener(new ChoiceDragListener(userID));
            mChoice3.setOnDragListener(new ChoiceDragListener(userID));
            mChoice4.setOnDragListener(new ChoiceDragListener(userID));
            mChoice5.setOnDragListener(new ChoiceDragListener(userID));


        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        public ChoiceDragListener(){}
        public ChoiceDragListener(String userId){
            this.userId=userId;
        }
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //handle drag events
            View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    ProfilePictureView behind = (ProfilePictureView) v;
                    ProfilePictureView drag = (ProfilePictureView) view;

                    int tag2 = behind.getId();
                    Log.d("tag2", String.valueOf(tag2));
                    if (String.valueOf(tag2).equals(String.valueOf(mChoice1.getTag()))){
                        v.setVisibility(View.VISIBLE);
                    }




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
                    //View view = (View) event.getLocalState();


                    //stop displaying the view where it was before it was dragged
                    view.setVisibility(View.INVISIBLE);

                    //view dragged item is being dropped on
                    ProfilePictureView dropTarget = (ProfilePictureView) v;

                    //view being dragged and dropped
                    ProfilePictureView dropped = (ProfilePictureView) view;


                    //update the text in the target view to reflect the data being dropped
                    ///ropTarget.setText(dropped.getText());
                    //dropTarget.setVisibility(View.VISIBLE);


                    dropTarget.setProfileId(dropped.getProfileId());

                    dropTarget.setOnLongClickListener(new OnLongClickListen());



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
                        findViewById(existingID).setVisibility(View.VISIBLE);
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


    private class ClickListener implements View.OnClickListener {


        public void onClick(View v){


            LayoutInflater inflater = getLayoutInflater();


            View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_id));


            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("This is a Custom Toast Message");

            ProfilePictureView pic = (ProfilePictureView) layout.findViewById(R.id.toastPic);
            pic.setProfileId("10152998692153003");


            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            //Toast toast = Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT);
            toast.show();
            }
    }


    //calling game.startGame to get other users' names
    private class StartGameConnectionTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);

                nameValuePairs.add(new BasicNameValuePair("action", "game.startGame"));
                nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
                nameValuePairs.add(new BasicNameValuePair("session_key", session_key));
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
                Log.d("strStartGameTask", str);


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
                 //textView.setText(json.getString("playerList"));

                 textView.setText("onPostExecute");

             } catch (JSONException e){
                 e.printStackTrace();
             }
        }
    }



    //calling common.getProfile to get other users' information
    private class GetProfileTask extends AsyncTask<String, Void, String>{

        String other_user_id;
        public GetProfileTask(){

        }
        public GetProfileTask(String user_id){
            other_user_id = user_id;
        }
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-25-127-194.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("action", "game.startGame"));
                nameValuePairs.add(new BasicNameValuePair("user_id", other_user_id));
                nameValuePairs.add(new BasicNameValuePair("session_key", session_key));
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
                Log.d("strGetProfileTask", str);


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
                textView.setText(json.getString("playerList"));
                json.getString("gameId");

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
