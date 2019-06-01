package com.example.database.comparison.dbms.objectbox

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
data class PersonObjectbox (
    @NameInDb("first_name") var firstName: String,
    @NameInDb("second_name") var secondName: String,
    @NameInDb("age") var age: Int
) {

    // No-arg constructor required by Objectbox.
    constructor() : this("", "", 0)

    @Id var id: Long = 0

}