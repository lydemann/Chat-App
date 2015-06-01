package com.strangerchat.strangerchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.util.ArrayList;
import java.util.List;

import Cache.Cache;
import Models.Person;
import RESTHelper.RESTHelper;
import Utility.Utilities;


public class GuiActivity extends Activity implements OnItemRecycleViewClickListener {

    RecyclerView mRecyclerView;
    TextView stranger;
    private List<Data> mData = new ArrayList<>();
    RESTHelper rest = new RESTHelper();

    public static GoogleCloudMessaging gcm;
    public static NotificationHub hub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for(int x = 10; x < 20; x =x+1) {
          mData.add(new Data("Mor" + x, "Yo, pikfjs, hva sker der for dig"));
          mData.add(new Data("Far"+ x, "Yo, pikfjs, hva sker der for dig"));
          mData.add(new Data("Bror" + x, "Yo, pikfj, hva sker der for dig"));
        }
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
        mRecyclerView.setAdapter(new RecyclerAdapter(mData, this));


        //"Knappen" til stranger chat
        StrangerLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
                    hub.register(regid, "Person"+Cache.CurrentUser.Id); // default id is 0

                } catch (Exception e) {
                    DialogNotify("Exception",e.getMessage());
                    return e;
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
            Cache.CurrentUser.Available = true;
            new UpdatePerson().execute(Cache.CurrentUser);


        } else {
            Log.d("Gui", "Off");
            stranger.setTextColor(Color.GRAY);
            Cache.CurrentUser.Available = false;
            new UpdatePerson().execute(Cache.CurrentUser);

        }
    }
    private class UpdatePerson extends AsyncTask<Person, Void, String>
    {

        @Override
        protected String doInBackground(Person... params) {
            Log.d("gui", "para aval" + params[0].Available);
            Log.d("gui", "Person object" + params[0].Name + ", "+ params[0].Id + ", "+ params[0].BirthDay );
           String re = rest.UpdatePerson(params[0]);
            Log.d("gui", re);
            return re;
        }

        protected void onPostExecute(String result) {
            Log.d("giu", "Downloaded " + result + " bytes");
        }

    }

}



