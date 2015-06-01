package com.strangerchat.strangerchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.FacebookRequestErrorClassification;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Cache.Cache;


public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
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


            Cache.CurrentUser.Id = userId;
            Cache.CurrentUser.Name = name;

            String profilePic = profile.getProfilePictureUri(500, 500).toString();
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) throws JSONException {

                    Log.d("response", response.toString());
                    Log.d("Object", object.toString());
                }
            });

            request = GraphRequest.newMyFriendsRequest(accessToken, new GraphRequest.GraphJSONArrayCallback() {
                @Override
                public void onCompleted(JSONArray objects, GraphResponse response) {

                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
            JSONObject jsonObject = request.getGraphObject();



            // TODO: Get friendlist and birthday



            //Cache.CurrentUser()

            Intent intent = new Intent(getActivity(), GuiActivity.class);
            ((MainActivity)getActivity()).startActivity(intent);

            ((MainActivity)getActivity()).finish();

            if(profile != null)
            {
                mTextDetails.setText("Welcome "+ profile.getName());
            }



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
        if (accessToken == null) {
            Log.d("tag", ">>>" + "Signed Out");
        } else {
            Log.d("tag", ">>>" + "Signed In");
            Intent intent = new Intent(getActivity(), GuiActivity.class);
            ((MainActivity)getActivity()).startActivity(intent);

            ((MainActivity)getActivity()).finish();
        }

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
            }
        });



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
