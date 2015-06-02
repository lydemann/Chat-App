package com.strangerchat.strangerchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import Cache.Cache;
import Models.Person;
import RESTHelper.RESTHelper;


public class MessageActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    String tag = "MessageActivity";
    ImageView mImageView;
    Button sendBtn;
    EditText editTextMsg;
    TableLayout tab;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;//brugerens location
    String StrangerId;

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
        tab = (TableLayout) findViewById(R.id.tab);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create new chatmessage
                if (editTextMsg.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Message can't be empty", Toast.LENGTH_SHORT).show();
                } else {

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

        buildGoogleApiClient();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void send() {


        // send message to Web Api
    }


    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("msg");
            String str1 = intent.getStringExtra("fromname");


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
            Intent i = new Intent(getBaseContext(), SettingsActivity.class);

            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Location pjat
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

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

            new FindStranger().execute(Cache.CurrentUser, Cache.radius, Cache.desiredSex, Cache.minAge, Cache.maxAge);

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


    private class FindStranger extends AsyncTask<Object, Void, Person> {
        RESTHelper rest = new RESTHelper();

        @Override
        protected Person doInBackground(Object... params) {
            //search efter stragners
            Person re = rest.FindStranger((Person) params[0], (double) params[1], (String) params[2], (int) params[3], (int) params[4]);
            Log.d(tag, re.name);
            StrangerId = re.id;
            return re;
        }

        protected void onPostExecute(String result) {
            Log.d(tag, "Downloaded " + result);
        }

    }

}
