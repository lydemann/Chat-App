package com.strangerchat.strangerchat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Cache.Cache;
import Models.ChatRoom;
import Models.Person;
import RESTHelper.RESTHelper;
import Utility.Utilities;


public class GuiActivity extends ActionBarActivity implements OnItemRecycleViewClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String tag = "Hoved";
    RecyclerView mRecyclerView;
    TextView stranger;
    private List<ChatRoom> chatRoomList = new ArrayList<>();
    RESTHelper rest = new RESTHelper();
    Switch onOff;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;//brugerens location
    String StrangerId;
    ProgressDialog dialog;
    RecyclerAdapter recyclerAdapter;
    public static GoogleCloudMessaging gcm;
    public static NotificationHub hub;

    Person person = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get a persons chatrooms

        getPersonChatrooms();





        setContentView(R.layout.activity_main2);

        mRecyclerView = (RecyclerView) findViewById(R.id.idRecyclerView);
        stranger = (TextView) findViewById(R.id.txt1);
        RelativeLayout StrangerLayout = (RelativeLayout) findViewById(R.id.StrangerLayout);//starnger chatt knappen

        LinearLayoutManager mLinearManager = new LinearLayoutManager(this);


        recyclerAdapter = new RecyclerAdapter(Cache.CurrentChatRoomList, this);
        mRecyclerView.setLayoutManager(mLinearManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(recyclerAdapter);




        MyHandler.GUIActivity = this;
        NotificationsManager.handleNotifications(this, Utilities.SENDER_ID, MyHandler.class);
        gcm = GoogleCloudMessaging.getInstance(this);
        hub = new NotificationHub(Utilities.HubName, Utilities.HubListenConnectionString, this);

        registerWithNotificationHubs();

        buildGoogleApiClient();//Opretter google api funktionen
        onOff = (Switch) findViewById(R.id.switch1);//S�tter switchen s� den kan aendres


    }


    @Override
    protected void onResume() {
        super.onResume();
        //Saetter status ved startup
        if (Cache.CurrentUser.available) {
            onOff.setChecked(true);//s��ter brugerens online status grafisk
            stranger.setTextColor(Color.BLACK);
        }
        else{
            onOff.setChecked(false);//s��ter brugerens online status grafisk
            stranger.setTextColor(Color.GRAY);

        }

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
    private void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(Utilities.SENDER_ID);
                    hub.register(regid, Cache.CurrentUser.id); // default id is Person0
                    Log.d("RegDevice", Cache.CurrentUser.id);

                } catch (Exception e) {
                    DialogNotify("Exception", e.getMessage());
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
    }

    @SuppressWarnings("unchecked")
    private void getPersonChatrooms() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    //get a persons chatrooms

                    String result = rest.getChatRoomsOfPerson(Cache.CurrentUser.id);

                    Gson gson = new Gson();
                    chatRoomList = gson.fromJson(result,new TypeToken<List<ChatRoom>>(){}.getType());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // update mRecycleView

                            synchronized (recyclerAdapter)
                            {


                                if(chatRoomList.size() != 0) {
                                    Cache.CurrentChatRoomList.clear();
                                    for(ChatRoom room : chatRoomList) {
                                        Cache.CurrentChatRoomList.add(room);

                                        recyclerAdapter.notifyDataSetChanged();
                                    }

                                }

                            }


                        }
                    });


                } catch (Exception e) {

                }
                return null;

            }
        }.execute(null, null, null);

    }


    /**
     * A modal AlertDialog for displaying a message on the UI thread
     * when theres an exception or message to report.
     *
     * @param title   Title for the AlertDialog box.
     * @param message The message displayed for the AlertDialog box.
     */
    public void DialogNotify(final String title, final String message) {
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
        //Bruger id skal sendes med her i stedet for positionen
        startChat(String.valueOf(position));
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

    public void StartStrangerChat(View view){

        dialog = new ProgressDialog(GuiActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Finding a stranger...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setIndeterminate(true);
        dialog.show();

        //Saetter personen til online
        mGoogleApiClient.connect();

        Log.d("Gui", "On");
        onOff.setChecked(true);
        stranger.setTextColor(Color.BLACK);
        Cache.CurrentUser.available = true;

        new FindStranger().execute(Cache.CurrentUser, Cache.radius, Cache.desiredSex, Cache.minAge, Cache.maxAge);



    }

    //Location pjat
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //finder brugerens placering og gemmer den i cachen
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d(tag, "location");
        if (mLastLocation != null) {
            Log.d(tag, "lat " + mLastLocation.getLatitude());
            Log.d(tag, "lat " + mLastLocation.getLongitude());
            Cache.CurrentUser.latitude = mLastLocation.getLatitude();
            Cache.CurrentUser.longitude = mLastLocation.getLongitude();

        } else
            Toast.makeText(this, "Could not get loaction", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Toast.makeText(this, "Loaction suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to get loaction", Toast.LENGTH_LONG).show();
    }

    private void startChat(Person person){

        Intent i = new Intent(getBaseContext(), MessageActivity.class);
        i.putExtra("personId", person.id);
        startActivity(i);
    }
    private void startChat(String personId){

        Intent i = new Intent(getBaseContext(), MessageActivity.class);
        i.putExtra("personId", personId);
        startActivity(i);
    }

    private void setStatus(boolean status){



    }


    private class UpdatePerson extends AsyncTask<Person, Void, String> {

        @Override
        protected String doInBackground(Person... params) {
            String re = rest.UpdatePerson(params[0]);
            Log.d(tag, re);
            return re;
        }

        protected void onPostExecute(String result) {
            Log.d(tag, result);
        }

    }

    private class FindStranger extends AsyncTask<Object, Void, Person> {
        RESTHelper rest = new RESTHelper();

        @Override
        protected Person doInBackground(Object... params) {
            //Soeger efter stragners
            Person re = rest.FindStranger((Person) params[0], (double) params[1], (String) params[2], (int) params[3], (int) params[4]);
            Log.d(tag, re.name);
            StrangerId = re.id;
            return re;
        }
         @Override
        protected void onPostExecute(Person re) {
            new UpdatePerson().execute(Cache.CurrentUser);
             Log.d(tag, "fundet en fremmede: " + re.name);
             dialog.hide();
            startChat(re);
        }

    }


}



