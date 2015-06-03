package RESTHelper;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Date;
import java.util.List;

import Models.Chat;
import Models.ChatRoom;
import Models.Person;

/**
 * Created by Christian on 30-05-2015.
 */
public class RESTHelper  {

    public RESTHelper()
    {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "LBWtXpISapjpfHURBHzuspsLBjmVqP80".toCharArray());
            }
        });
    }


    // Finds a random available person
    public Person FindStranger(Person person, double radius, String sex, int minAge, int maxAge)
    {
        URL requestUrl;
        HttpURLConnection con = null;
        Gson gson = null;
        InputStream response = null;

        try {
            requestUrl = new URL("http://strangerchat.azure-mobile.net/Api/findstranger?radius="+radius+"&sex="+sex+"&minAge="+minAge+"&maxAge="+maxAge);

            con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.connect();





                    //Send request
                    DataOutputStream wr = new DataOutputStream(
                            con.getOutputStream());
                    String jsonObj;
                    gson = new Gson();
                    jsonObj = gson.toJson(person);
                    wr.writeBytes(jsonObj);
                    wr.close();

                    int responseCode = con.getResponseCode();

                    response = con.getInputStream();




                }

                catch(
                MalformedURLException e
                )

                {
                    e.printStackTrace();
                }

                catch(
                IOException e
                )

                {

                    e.printStackTrace();
                }

        if (response == null) {
            Log.d("rest", "Could not find any persons");
            return new Person("Error", "Error","Error",new Date(1992,10,24), "Error", 10.00,10.00 );
        }
        // deserialize to person object
                return gson.fromJson(response.toString(),Person.class);
            }


    // Finds a random available person
    public String InsertPerson(Person person)
    {
        URL requestUrl;
        HttpURLConnection con = null;
        Gson gson = null;
        InputStream response = null;

        try {
            requestUrl = new URL("http://strangerchat.azure-mobile.net/Api/tables/People");

            con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.connect();
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    con.getOutputStream());
            String jsonObj;
            gson = new Gson();
            jsonObj = gson.toJson(person);
            wr.writeBytes(jsonObj);
            wr.close();

            int responseCode = con.getResponseCode();

            response = con.getInputStream();

            // deserialize to person object

        } catch(
                IOException e
                )

        {
            e.printStackTrace();
            return "Error";
        }

        return "Person inserted";
    }


    // Finds a random available person
    public String UpdatePerson (Person person)
    {

        URL requestUrl;
        HttpURLConnection con = null;
        Gson gson = null;
        InputStream response = null;

        try {
            requestUrl = new URL("http://strangerchat.azure-mobile.net/Api/tables/People/"+person.id);

            con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.connect();
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    con.getOutputStream());
            String jsonObj;
            gson = new Gson();
            jsonObj = gson.toJson(person);
            wr.writeBytes(jsonObj);
            wr.close();

            int responseCode = con.getResponseCode();

            response = con.getInputStream();

        } catch(
                IOException e
                )

            {
            e.printStackTrace();
                return "Error";
        }

        return "Person updated";
    }

    // get person

    // gets  person /////////////////////////////////////////////////////////////////////////////////////////////
    public String GetPerson(String id)
    {

        URL requestUrl;
        String credentials = "admin" + ":" + "LBWtXpISapjpfHURBHzuspsLBjmVqP80";
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        String result = "";

        HttpResponse response = null;


        try {
            requestUrl = new URL("http://strangerchat.azure-mobile.net/tables/People/"+id);

            HttpUriRequest request = new HttpGet(requestUrl.toString());

            request.addHeader("Authorization", "Basic " + base64EncodedCredentials);


            HttpClient httpclient = new DefaultHttpClient();

            response = httpclient.execute(request);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));


            result = null;
            String line = "";
            while ((line = rd.readLine()) != null) {
                boolean firsttime = true;

                if(firsttime) {
                    result = line;
                    firsttime = false;
                }
                else
                    result += line;

            }


            // deserialize to person object

        } catch(
                IOException e
                )

        {
            e.printStackTrace();

        }
        if (response == null) {
            Log.d("rest", "Could not find requested person");
            //return new Person("Person not found", "Error","Error",new Date(0000,00,00), "Error", 10.00,10.00 );
            return "Error";
        }

        //Person per = gson.fromJson(result,Person.class);


        return result;
    }


    // Finds a random available person
    public String InsertChat(Chat chat)
    {
        URL requestUrl = null;
        HttpURLConnection con = null;
        Gson gson = null;
        InputStream response = null;

        try {
            requestUrl = new URL("http://strangerchat.azure-mobile.net/Api/tables/People");

            con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.connect();
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    con.getOutputStream());
            String jsonObj;
            gson = new Gson();
            jsonObj = gson.toJson(chat);
            wr.writeBytes(jsonObj);
            wr.close();

            int responseCode = con.getResponseCode();

            response = con.getInputStream();

            // deserialize to person object

        } catch(
                IOException e
                )

        {
            e.printStackTrace();
            return  "Error";
        }

        return "Person inserted";
    }


    // get person's chatrooms

    public String getChatRoomsOfPerson(String personId)
    {

        URL requestUrl;
        String credentials = "admin" + ":" + "LBWtXpISapjpfHURBHzuspsLBjmVqP80";
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        String result = "";
        HttpResponse response = null;

            try {
                requestUrl = new URL("http://strangerchat.azure-mobile.net/GetPersonsChatrooms?personId="+personId);

                HttpUriRequest request = new HttpGet(requestUrl.toString());

                request.addHeader("Authorization", "Basic " + base64EncodedCredentials);


                HttpClient httpclient = new DefaultHttpClient();

                response = httpclient.execute(request);

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));


                result = null;
                String line = "";
                while ((line = rd.readLine()) != null) {
                    boolean firsttime = true;

                    if(firsttime) {
                        result = line;
                        firsttime = false;
                    }
                    else
                        result += line;

                }


                // deserialize to person object

            } catch(
                    IOException e
                    )

            {
                e.printStackTrace();

            }
        if (response == null) {
            Log.d("rest", "Could not find any persons");
            //return new Person("Person not found", "Error","Error",new Date(0000,00,00), "Error", 10.00,10.00 );
            return "Error";
        }

        //Person per = gson.fromJson(result,Person.class);


        return result;
    }


        }




