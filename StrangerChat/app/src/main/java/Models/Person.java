package Models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Christian on 30-05-2015.
 */
public class Person {

    public Person(String id, String name, String sex, Date birthDay, String picUrl, double latitude, double longitude)
    {
        this.id = id;
        this.sex = sex;
        this.name = name;
        this.birthDay = birthDay;
        this.picUrl = picUrl;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String id;
    public String name;
    public String sex;
    public Date birthDay;
    public int age;
    public boolean available;
    public String picUrl;
    public double longitude;
    public double latitude;



}
