package RESTHelper;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

import Models.Chat;
import Models.Person;

/**
 * Created by Christian on 30-05-2015.
 */
public class RESTHelper {

    public RESTHelper()
    {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("http://strangerchat.azure-mobile.net\\admin", "LBWtXpISapjpfHURBHzuspsLBjmVqP80".toCharArray());
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
    public String UpdatePerson(Person person)
    {
        URL requestUrl;
        HttpURLConnection con = null;
        Gson gson = null;
        InputStream response = null;

        try {
            requestUrl = new URL("http://strangerchat.azure-mobile.net/Api/tables/People/"+person.Id);

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

    // Finds a random available person
    public Person GetPerson(String id)
    {
        URL requestUrl;
        HttpURLConnection con = null;
        Gson gson = null;
        InputStream response = null;

        try {
            requestUrl = new URL("http://strangerchat.azure-mobile.net/Api/tables/People/"+id);

            con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.connect();

            int responseCode = con.getResponseCode();

            response = con.getInputStream();

            // deserialize to person object

        } catch(
                IOException e
                )

        {
            e.printStackTrace();

        }

        return gson.fromJson(response.toString(),Person.class);
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


    // insert Chatroom


    // get chatroom chats

    // get users chatrooms


        }
