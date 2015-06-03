package Cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.Chat;
import Models.ChatRoom;
import Models.Person;

/**
 * Created by Christian on 30-05-2015.
 */
public class Cache {

    public static Person CurrentUser = new Person("Person0","Christian","Male", new Date(1992,10,24), "url",10.00,10.00);

    public static List<Person> CurrentGroupList = new ArrayList();

    public static List<ChatRoom> CurrentChatRoomList = new ArrayList();

    public static int minAge = 15;
    public static int maxAge = 99;
    public static String desiredSex = "male";
    public static double radius = 50.00;
}
