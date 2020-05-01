package com.example.database.comparison.dbms.objectbox.model

import com.example.database.comparison.model.BasePerson
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

@Entity
data class PersonObjectbox(
    @NameInDb("first_name") override var firstName: String,
    @NameInDb("second_name") override var secondName: String,
    @NameInDb("age") override var age: Int
) : BasePerson {

    // No-arg constructor required by Objectbox.
    constructor() : this("", "", 0)

    @Id
    override var id: Long = 0
}