package com.example.database.comparison.test

import com.example.database.comparison.base.BaseRepo
import com.example.database.comparison.model.BasePerson
import com.example.database.comparison.model.Person
import com.example.database.comparison.util.Runner

class Test<T : BasePerson>(
    private val runner: Runner,
    private val repo: BaseRepo<T>
) {

    private val name = repo.name

    fun run(runs: Int, data: List<Person>) {

        val persons = repo.transformData(data)
        var reloaded: List<T> = emptyList()
        var updated: List<T> = emptyList()

        runner
            .beforeEach { repo.clear() }
            .run("$name-create", runs) { repo.addAll(persons) }

        runner
            .before {
                repo.clear()
                repo.addAll(persons)
            }
            .run("$name-read", runs) {
                reloaded = repo.loadAll()
                repo.access(reloaded)
            }

        runner
            .beforeEach {
                repo.clear()
                repo.addAll(persons)
                reloaded = repo.loadAll()
                updated = repo.change(reloaded)
            }
            .run("$name-update", runs) { repo.updateAll(updated) }

        runner
            .beforeEach {
                repo.clear()
                repo.addAll(persons)
                reloaded = repo.loadAll()
            }
            .run("$name-delete", runs) { repo.deleteAll(reloaded) }
    }
}