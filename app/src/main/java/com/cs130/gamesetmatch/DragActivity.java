package com.cs130.gamesetmatch;

import android.app.Activity;
import android.content.ClipData;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


public class DragActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);


        final ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
        final TextView mTextDetails = (TextView) findViewById(R.id.text_details);

        final ProfilePictureView mOption1 = (ProfilePictureView)findViewById(R.id.profilePic);
        final ProfilePictureView mOption2 = (ProfilePictureView)findViewById(R.id.option_2);
        final ProfilePictureView mOption3 = (ProfilePictureView)findViewById(R.id.option_3);
        final ProfilePictureView mOption4 = (ProfilePictureView)findViewById(R.id.option_4);
        final ProfilePictureView mOption5 = (ProfilePictureView)findViewById(R.id.option_5);

        final ProfilePictureView mChoice1 = (ProfilePictureView)findViewById(R.id.choice_1);
        final ProfilePictureView mChoice2 = (ProfilePictureView)findViewById(R.id.choice_2);
        final ProfilePictureView mChoice3 = (ProfilePictureView)findViewById(R.id.choice_3);
        final ProfilePictureView mChoice4 = (ProfilePictureView)findViewById(R.id.choice_4);
        final ProfilePictureView mChoice5 = (ProfilePictureView)findViewById(R.id.choice_5);

        mOption1.setPresetSize(ProfilePictureView.SMALL);
        mOption2.setPresetSize(mOption2.SMALL);

        mChoice1.setPresetSize(ProfilePictureView.SMALL);
        mChoice2.setPresetSize(ProfilePictureView.SMALL);
        mChoice3.setPresetSize(ProfilePictureView.SMALL);
        mChoice4.setPresetSize(ProfilePictureView.SMALL);
        mChoice5.setPresetSize(ProfilePictureView.SMALL);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Profile currentProfile = (Profile) extras.get("currentProfile");
            profilePictureView.setProfileId(currentProfile.getId());

            String userID=currentProfile.getId();

            mTextDetails.setText("Welcome " + currentProfile.getFirstName());

            mOption2.setProfileId("10152998692153003");


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
        public ChoiceDragListener(String userId){
            this.userId=userId;
        }
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //handle drag events
            View view = (View) event.getLocalState();
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

}
