package com.strangerchat.strangerchat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MessageActivity extends FragmentActivity {
String tag = "MessageActivity";
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        // mImageView = (ImageView) findViewById(R.id.imageView);
        RelativeLayout friends = (RelativeLayout) findViewById(R.id.friends);//starnger chatt knappen
        //"Knappen" til stranger chat
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Friends clicked");
                showFriednDialog();


            }
        });
    }

    private void showFriednDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FriendFragment editNameDialog = FriendFragment.newInstance("Some Title");
        editNameDialog.show(fm, "fragment_edit_name");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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




}
