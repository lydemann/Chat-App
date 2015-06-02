package com.strangerchat.strangerchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Cache.Cache;
import Models.ChatRoom;
import Models.Person;
import RESTHelper.RESTHelper;
import Utility.Utilities;


public class GuiActivity extends FragmentActivity implements OnItemRecycleViewClickListener {

    RecyclerView mRecyclerView;
    TextView stranger;
    private List<ChatRoom> chatRoomList = new ArrayList<>();
    RESTHelper rest = new RESTHelper();
    Switch onOff;

    public static GoogleCloudMessaging gcm;
    public static NotificationHub hub;

    Person person = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get a persons chatrooms

        //person = getPersonChatrooms();

        Gson gson = new Gson();
        JSONObject jsonObj = null;



            String str = "{\"id\":1,\"name\":\"The chatroom name\"}";


        String perStr = "{\n" +
                "    \"$id\": \"1\",\n" +
                "    \"id\": \"person0\",\n" +
                "    \"name\": \"Christian\",\n" +
                "    \"sex\": \"Male\",\n" +
                "    \"birthDay\": \"1992-10-24T00:00:00Z\",\n" +
                "    \"age\": 22,\n" +
                "    \"available\": true,\n" +
                "    \"picUrl\": \"dada\",\n" +
                "    \"longitude\": 6.1,\n" +
                "    \"latitude\": 7.1\n" +
                "}";


        person = gson.fromJson(perStr,Person.class);


        ChatRoom chatRoom;

        chatRoom = gson.fromJson(str,ChatRoom.class);

        //List<ChatRoom> chatRoomList = rest.getChatRoomsOfPerson(Cache.CurrentUser.id);
        chatRoomList.add(chatRoom);




        setContentView(R.layout.activity_main2);

        mRecyclerView = (RecyclerView) findViewById(R.id.idRecyclerView);
        stranger = (TextView) findViewById(R.id.txt1);
        RelativeLayout StrangerLayout =(RelativeLayout)findViewById(R.id.StrangerLayout);//starnger chatt knappen

        LinearLayoutManager mLinearManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearManager);

        //          StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        //          mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        //
        //          GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        //          mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new RecyclerAdapter(chatRoomList, this));


        //"Knappen" til stranger chat
        StrangerLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Saetter personen til online
                onOff = (Switch) findViewById(R.id.switch1);
                Log.d("Gui", "On");
                onOff.setChecked(true);
                stranger.setTextColor(Color.BLACK);
                Cache.CurrentUser.available = true;
                new UpdatePerson().execute(Cache.CurrentUser);
                Intent i = new Intent(getBaseContext(), MessageActivity.class);
                startActivity(i);
            }
        });


        MyHandler.GUIActivity = this;
        NotificationsManager.handleNotifications(this, Utilities.SENDER_ID, MyHandler.class);
        gcm = GoogleCloudMessaging.getInstance(this);
        hub = new NotificationHub(Utilities.HubName, Utilities.HubListenConnectionString, this);

        registerWithNotificationHubs();

    }


    @SuppressWarnings("unchecked")
    private void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(Utilities.SENDER_ID);
                    hub.register(regid, "Person"+Cache.CurrentUser.id); // default id is 0
                    Log.d("RegDevice","Person"+Cache.CurrentUser.id);

                } catch (Exception e) {
                    DialogNotify("Exception",e.getMessage());
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
    }

    @SuppressWarnings("unchecked")
    private Person getPersonChatrooms() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    //get a persons chatrooms



                    person = rest.GetPerson("Person0");

                    Log.d("dbperson", person.name);

                } catch (Exception e) {

                }
                return person;
            }
        }.execute(null, null, null);
        return person;
    }


    /**
     * A modal AlertDialog for displaying a message on the UI thread
     * when theres an exception or message to report.
     *
     * @param title   Title for the AlertDialog box.
     * @param message The message displayed for the AlertDialog box.
     */
    public void DialogNotify(final String title,final String message)
    {
        final AlertDialog.Builder dlg;
        dlg = new AlertDialog.Builder(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dlgAlert = dlg.create();
                dlgAlert.setTitle(title);
                dlgAlert.setButton(DialogInterface.BUTTON_POSITIVE,
                        (CharSequence) "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dlgAlert.setMessage(message);
                dlgAlert.setCancelable(false);
                dlgAlert.show();
            }
        });
    }

    @Override
    public void onItemClicked(int position, RecyclerAdapter mAdapter) {
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_LONG).show();
    }

    //Online offline toogle
    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();

        if (on) {
            Log.d("Gui", "On");
            stranger.setTextColor(Color.BLACK);
            Cache.CurrentUser.available = true;
            new UpdatePerson().execute(Cache.CurrentUser);


        } else {
            Log.d("Gui", "Off");
            stranger.setTextColor(Color.GRAY);
            Cache.CurrentUser.available = false;
            new UpdatePerson().execute(Cache.CurrentUser);

        }
    }
    private class UpdatePerson extends AsyncTask<Person, Void, String>
    {

        @Override
        protected String doInBackground(Person... params) {
            Log.d("gui", "para aval" + params[0].available);
            Log.d("gui", "Person object" + params[0].name + ", "+ params[0].id + ", "+ params[0].birthDay );
           String re = rest.UpdatePerson(params[0]);
            Log.d("gui", re);
            return re;
        }

        protected void onPostExecute(String result) {
            Log.d("giu", "Downloaded " + result + " bytes");
        }

    }

}



