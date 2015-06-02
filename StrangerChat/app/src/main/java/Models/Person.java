package Models;

import java.util.Date;

/**
 * Created by Christian on 30-05-2015.
 */
public class Person {

    public Person(String id, String name, String sex, Date birthDay, String picUrl, double latitude, double longitude)
    {
        this.Id = id;
        this.Name = name;
        this.BirthDay = birthDay;
        this.PicUrl = picUrl;
        this.Latitude = latitude;
        this.Longitude = longitude;

    }

    public String Id;
    public String Name;
    public String Sex;
    public Date BirthDay;
    public int Age;
    public boolean Available;
    public String PicUrl;
    public double Longitude;
    public double Latitude;



}
