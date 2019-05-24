package com.example.database.comparison.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "person")
public class Person {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "first_name")
    private String firstName;

    @Property(nameInDb = "second_name")
    private String secondName;

    @Property(nameInDb = "age")
    private int age;

    public Person(String firstName, String secondName, int age) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    @Generated(hash = 763899716)
    public Person(Long id, String firstName, String secondName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    @Generated(hash = 1024547259)
    public Person() {
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
