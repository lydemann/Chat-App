package com.strangerchat.strangerchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import Cache.Cache;
import Models.Chat;
import Models.ChatRoom;
import RESTHelper.RESTHelper;


public class MessageActivity extends ActionBarActivity  {
    String tag = "MessageActivity";
    ImageView mImageView;
    Button sendBtn;
    EditText editTextMsg;
    TableLayout tab;
    String StrangerId;


    RESTHelper rest = new RESTHelper();
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

       StrangerId = getIntent().getStringExtra("personId");

        // get all messages for chatroom from db
        editTextMsg = (EditText) findViewById(R.id.editText);
        // sendBtn listener
        sendBtn = (Button) findViewById(R.id.button);

        // table view
        tab = (TableLayout) findViewById(R.id.tab);


        Chat chat = new Chat();
        chat.personId = "hdahd";
        chat.message = "Hello from stranger";
        chat.chatRoomId = 1;
        Cache.CurrentChatList.add(chat);

        getChatRoomMessagesAndUpdate();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create new chatmessage
                if (editTextMsg.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Message can't be empty", Toast.LENGTH_SHORT).show();
                } else {

                    addChatMsgToView("You", editTextMsg.getText().toString(), "#00FF00");

                    send();
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));


    }



    @SuppressWarnings("unchecked")
    private void send() {

            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        //get a persons chatrooms
                        final String result;

                        Chat theChat = new Chat();
                        theChat.chatRoomId = Cache.CurrentChatRoom.id;
                        theChat.personId = Cache.CurrentUser.id;
                        theChat.message = editTextMsg.getText().toString();


                        result = rest.InsertChat(theChat);




                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(result == "Error") {
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                                editTextMsg.setText("");
                            }
                        });


                    }
                    catch (Exception e) {

                        Log.d("ex", e.getMessage());
                    }
                    return null;

                }
            }.execute(null, null, null);

        }


    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            Log.d("BroadcastReceiver","MsgReceived");


            getChatRoomMessagesAndUpdate();


        }
    };



    private void updateChats()
    {
        for(Chat chat : Cache.CurrentChatList)
        {
            if(chat.personId.equals(Cache.CurrentUser.id)) {
                addChatMsgToView("You", chat.message, "#00FF00");
            }
            else {
                addChatMsgToView("Stranger", chat.message, "#FF0000");
            }

        }

    }

    private void addChatMsgToView(String name, String msg, String colorCode)
    {
        TableRow tr1 = new TableRow(getApplicationContext());
        tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView textview = new TextView(getApplicationContext());
        textview.setTextSize(20);
        textview.setTextColor(Color.parseColor(colorCode));
        textview.setText(Html.fromHtml("<b>" + name + " : </b>" + msg));
        tr1.addView(textview);

        tab.addView(tr1);
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
            Intent i = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("unchecked")
    private void getChatRoomMessagesAndUpdate() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    //get a persons chatrooms
                    String result;
                    result = rest.getChatsInChatRoom(Cache.CurrentChatRoom.id);


                    Gson gson = new Gson();



                    Cache.CurrentChatList.clear();

                    Cache.CurrentChatList = gson.fromJson(result,new TypeToken<List<Chat>>(){}.getType());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // update mRecycleView



                                if(Cache.CurrentChatList.size() != 0) {


                                    updateChats();

                                }



                        }
                    });


                }
                catch (Exception e) {

                    Log.d("ex", e.getMessage());
                }
                return null;

            }
        }.execute(null, null, null);

    }



    private void hideBar(View View){
        Log.d(tag, "hidbar");
        bar.setVisibility(View.INVISIBLE);
    }
}
