package com.strangerchat.strangerchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.Profile;
import com.facebook.ProfileTracker;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;


import java.io.IOException;

import java.net.MalformedURLException;

import java.net.URL;
import java.util.Arrays;


import Cache.Cache;
import Models.Person;
import RESTHelper.RESTHelper;

import static com.google.android.gms.internal.zzhl.runOnUiThread;


public class MainFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CallbackManager mCallbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    Bitmap profilePic;

    FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {


            AccessToken accessToken;
            accessToken = loginResult.getAccessToken();

            String res = loginResult.toString();
            Profile profile;
            profile = Profile.getCurrentProfile();

            String userId = profile.getId();
            String name = profile.getName();


            Cache.CurrentUser.id = userId;
            Cache.CurrentUser.name = name;

            String profilePic = profile.getProfilePictureUri(500, 500).toString();

            Cache.CurrentUser.picUrl = profilePic;


            //Cache.CurrentUser()

            Intent intent = new Intent(getActivity(), GuiActivity.class);
            (getActivity()).startActivity(intent);



            insertPerson();

        }



        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    private TextView mTextDetails;


    public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");


            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();

        //check login
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        /*
        if (accessToken == null) {
            Log.d("tag", ">>>" + "Signed Out");
        } else {
            Log.d("tag", ">>>" + "Signed In");
            Intent intent = new Intent(getActivity(), GuiActivity.class);
            ((MainActivity)getActivity()).startActivity(intent);

            ((MainActivity)getActivity()).finish();
        }
        */

        setupTrackers();

        //start tracking
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

    }





    private void setupTrackers() {
        accessTokenTracker = new AccessTokenTracker() { // for tracking accesstoken changes
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextDetails = (TextView) view.findViewById(R.id.text_details);


        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setFragment(this);

        loginButton.setReadPermissions(Arrays.asList("user_birthday"));
        loginButton.registerCallback(mCallbackManager, mCallBack);


        Button skipButton = (Button) view.findViewById(R.id.skip_button);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GuiActivity.class);
                ((MainActivity) getActivity()).startActivity(intent);
                getActivity().finish();
            }
        });



    }

    RESTHelper rest = new RESTHelper();
    @SuppressWarnings("unchecked")
    private void insertPerson() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {



                    final String result = rest.InsertPerson(Cache.CurrentUser);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            (getActivity()).finish();

                        }
                    });


                } catch (Exception e) {

                }
                return null;

            }
        }.execute(null, null, null);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();

        //stop tracking
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}
