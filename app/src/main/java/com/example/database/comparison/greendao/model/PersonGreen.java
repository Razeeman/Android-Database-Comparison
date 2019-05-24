package com.example.database.comparison.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "person")
public class PersonGreen {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "first_name")
    private String firstName;

    @Property(nameInDb = "second_name")
    private String secondName;

    @Property(nameInDb = "age")
    private int age;

    public PersonGreen(String firstName, String secondName, int age) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    @Generated(hash = 2003878665)
    public PersonGreen(Long id, String firstName, String secondName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    @Generated(hash = 1121170119)
    public PersonGreen() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
