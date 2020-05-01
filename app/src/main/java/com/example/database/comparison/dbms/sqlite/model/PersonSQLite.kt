package com.example.database.comparison.dbms.sqlite.model

import com.example.database.comparison.model.BasePerson

data class PersonSQLite(
    override var firstName: String,
    override var secondName: String,
    override var age: Int
) : BasePerson {

    override var id: Long = 0
}