package com.scorpio.myexpensemanager.db.vo;



import com.myexpense.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkundu on 07-07-2016.
 */
public class Person {
    String name;
    String age;
    int photoId;

    Person(String name, String age, int photoId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
    }




    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    public static List<Person> initializeData(){
         List<Person> persons;
        persons = new ArrayList<>();
        persons.add(new Person("Emma Wilson", "23 years old", android.R.drawable.editbox_background));
        persons.add(new Person("Lavery Maiss", "25 years old", R.drawable.ic_menu_gallery));
        persons.add(new Person("Lillie Watts", "35 years old", R.drawable.ic_menu_send));

        return persons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
