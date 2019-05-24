package com.example.database.comparison.realm.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class PersonRealm (
    var firstName: String,
    var secondsName: String,
    var age: Int
) : RealmObject() {
    @PrimaryKey var id: Long = 0
}