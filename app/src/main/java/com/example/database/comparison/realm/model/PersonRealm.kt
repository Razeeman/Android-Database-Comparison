package com.example.database.comparison.realm.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PersonRealm (
    var firstName: String,
    var secondsName: String,
    var age: Int
) : RealmObject() {

    companion object {
        // Used to assign unique ids.
        var objectCounter = 0L
    }

    init {
        objectCounter++
    }

    // No-arg constructor required by Realm.
    constructor() : this("", "", 0)

    @PrimaryKey var id: Long = 0
}