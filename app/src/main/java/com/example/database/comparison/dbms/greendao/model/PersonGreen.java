package com.example.database.comparison.dbms.greendao.model;

import com.example.database.comparison.model.BasePerson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.jetbrains.annotations.NotNull;

@Entity(nameInDb = "person")
public class PersonGreen implements BasePerson {

    @Id(autoincrement = true)
    private Long _id;

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

    @Generated(hash = 1559220051)
    public PersonGreen(Long _id, String firstName, String secondName, int age) {
        this._id = _id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    @Generated(hash = 1121170119)
    public PersonGreen() {
    }

    @Override
    public long getId() {
        return _id;
    }

    @Override
    public void setId(long id) {
        _id = id;
    }

    @Override
    @NotNull
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    @Override
    @NotNull
    public String getSecondName() {
        return this.secondName;
    }

    @Override
    public void setSecondName(@NotNull String secondName) {
        this.secondName = secondName;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}
