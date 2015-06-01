package com.strangerchat.strangerchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MessageActivity extends FragmentActivity {
String tag = "MessageActivity";
    ImageView mImageView;
    Button sendBtn;
    EditText editTextMsg;
    TableLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        // mImageView = (ImageView) findViewById(R.id.imageView);
        RelativeLayout friends = (RelativeLayout) findViewById(R.id.friends);//Viser friends
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "Friends clicked");
                showFriednDialog();
            }
        });
                // get all messages for chatroom from db


        editTextMsg = (EditText) findViewById(R.id.editText);
        // sendBtn listener
        sendBtn = (Button) findViewById(R.id.button);

        // table view
        tab = (TableLayout)findViewById(R.id.tab);



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create new chatmessage
                if (editTextMsg.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Message can't be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {

                TableRow tr2 = new TableRow(getApplicationContext());
                tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#A901DB"));
                textview.setText(Html.fromHtml("<b>You : </b>" + editTextMsg.getText().toString()));
                tr2.addView(textview);
                tab.addView(tr2);

                editTextMsg.setText("");


                    send();
            }
            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

    }

    private void send() {



        // send message to Web Api
    }



    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("msg");
            String str1 = intent.getStringExtra("fromname");
            String str2 = intent.getStringExtra("fromu");


                TableRow tr1 = new TableRow(getApplicationContext());
                tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#0B0719"));
                textview.setText(Html.fromHtml("<b>" + str1 + " : </b>" + str));
                tr1.addView(textview);

                tab.addView(tr1);



        }
    };

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
