package com.cs130.gamesetmatch;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.facebook.Profile;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 5/28/2015.
 */
public class ChatActivity extends Activity {
    private String user_id = null;
    private String session_key = null;
    private String target_user_id = null;
    private String[] server = new String[]{"http://ec2-52-10-172-62.us-west-2.compute.amazonaws.com"};
    List<Map<String, String>> messageList = new ArrayList<Map<String,String>>();
    private Button sendButton = null;
    private EditText messageBox;
    private SimpleAdapter simpleAdpt;
    private Profile currentProfile;
    private Button deleteHistoryButton;
    private ListView messageListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user_id = (String) extras.get("user_id");
            //session_key = (String) extras.get("session_key");
            target_user_id = (String) extras.get("target_user_id");
            currentProfile = (Profile) extras.get("currentProfile");

            //messageList.add(createMessage("yolo", "YOLO", "DONT YOLO"));
            messageBox = (EditText) findViewById(R.id.messageBox);
            sendButton = (Button) findViewById(R.id.sendButton);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("onSendButtonClicked", messageBox.getText().toString());
                    //StoreMessageTask smTask = new StoreMessageTask();
                    //smTask.execute(server);

                    messageList.add(createMessage(currentProfile.getFirstName(), target_user_id, messageBox.getText().toString()));
                    simpleAdpt.notifyDataSetChanged();
                    messageBox.setText("");

                }
            });

            deleteHistoryButton = (Button) findViewById(R.id.deleteHistoryButton);
            deleteHistoryButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d("onHistoryButtonClicked", "Clicky");
                    //messageListView.setAdapter(null);
                    simpleAdpt.notifyDataSetChanged();
                }
            });

            //GetNewMessagesTask task  = new GetNewMessagesTask();
            //task.execute(server);

            messageListView = (ListView) findViewById(R.id.chat);
            messageList.add(createMessage("Christopher", user_id, "Hey! How's it going? :)"));
            messageList.add(createMessage("Christopher", user_id, "Can I follow you home?"));
            messageList.add(createMessage("Christopher", user_id, "Cause my parents always told me to follow my dreams."));
            messageList.add(createMessage("Christopher", user_id, "LOL :)"));
            simpleAdpt = new SimpleAdapter(this, messageList, android.R.layout.simple_list_item_1, new String[] {"message"}, new int[] {android.R.id.text1});
            messageListView.setAdapter(simpleAdpt);


        }


    }

    private class GetNewMessagesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-10-172-62.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("action", "chat.getNewMessages"));
                nameValuePairs.add(new BasicNameValuePair("user_id1", user_id));
                nameValuePairs.add(new BasicNameValuePair("user_id2", target_user_id));

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
                Log.d("result", result);
                JSONObject json1 = new JSONObject(result);
                JSONObject messages = json1.getJSONObject("messages");
                Iterator x = messages.keys();
                JSONArray json = new JSONArray();
                while (x.hasNext()){
                    String key = (String) x.next();
                    json.put(messages.get(key));
                }

                for(int i=0;i<json.length();i++)
                {
                    JSONObject c = (JSONObject) json.get(i);// Used JSON Object from Android

                    //Storing each Json in a string variable

                    String sender = c.getString("uuid1");
                    String receiver = c.getString("uuid2");
                    String msg = c.getString("msg");


                    messageList.add(createMessage(sender, receiver, msg));

                }

                //textView.setText(json.getString("user_id"));
                //textView.setText(json.getString("session_key"));


            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
    private HashMap<String, String> createMessage(String sender, String receiver, String msg) {
        HashMap<String, String> message = new HashMap<String, String>();
        message.put("message", sender +": "+msg);
        return message;
    }
    private class StoreMessageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...urls){

            //create HTTP client
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String link = "http://ec2-52-10-172-62.us-west-2.compute.amazonaws.com";

            //create HTTP post
            HttpPost httpPostReq = new HttpPost(link);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("action", "chat.storeMessage"));
                nameValuePairs.add(new BasicNameValuePair("user_id1", user_id));
                nameValuePairs.add(new BasicNameValuePair("user_id2", target_user_id));
                nameValuePairs.add(new BasicNameValuePair("msg", messageBox.getText().toString()));

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

        }
    }
}
