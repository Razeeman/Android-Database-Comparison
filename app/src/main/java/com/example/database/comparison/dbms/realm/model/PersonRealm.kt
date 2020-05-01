package com.example.database.comparison.dbms.realm.model

import com.example.database.comparison.model.BasePerson
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PersonRealm(
    override var firstName: String,
    override var secondName: String,
    override var age: Int
) : RealmObject(), BasePerson {

    companion object {
        // Used to assign unique ids.
        var objectCounter = 0L
    }

    init {
        objectCounter++
    }

    // No-arg constructor required by Realm.
    constructor() : this("", "", 0)

    @PrimaryKey
    override var id: Long = 0
}