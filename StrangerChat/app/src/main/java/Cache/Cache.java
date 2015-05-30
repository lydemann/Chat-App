package Cache;

import java.util.Date;
import java.util.List;

import Models.Chat;
import Models.Person;

/**
 * Created by Christian on 30-05-2015.
 */
public class Cache {

    public static Person CurrentUser = new Person("0","Christian","Male", new Date(1992,10,24), "url",10.00,10.00);

    public static List<Person> CurrentGroupList;

    public static List<Chat> ChatList;

}
