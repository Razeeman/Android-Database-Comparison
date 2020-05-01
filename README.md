# Android Database Comparison

Benchmark app to compare speed of CRUD operations of different android database management systems.

Test are executed with a collection of 10000 objects. Execution times are averaged between 10 runs. Tests were run on the Nexus 5X.

- ```create(data: List<T>)``` Inserts list of objects into database.

- ```read(): List<T>``` Reads all objects from the database. Read is coupled with access to prevent lazy query results (Realm).

- ```update(data: List<T>)``` Updates objects in the database with data of provided objects.

- ```delete(data: List<T>)``` Delete provided objects from the database.

## Model

```kotlin
data class Person (
    var firstName: String,
    var secondName: String,
    var age: Int
)
```

## Results

|Library|create|read|update|delete|
|:---:|:---:|:---:|:---:|:---:|
|SQLite|926|234|1617|1065|
|GreenDAO|582|104|698|329|
|Room|566|204|646|289|
|Realm|463|328|269|920|
|SQLiteBatched|233|229|287|93|
|ObjectBox|129|183|139|63|

<br>

![Results][results]

## Index
- [SQLite]
- [GreenDAO]
- [ObjectBox]
- [Room]
- [Realm]
- [Dagger Module]



[results]: dev_files/Results.png

[SQLite]: https://github.com/Razeeman/Android-Database-Comparison/tree/master/app/src/main/java/com/example/database/comparison/dbms/sqlite
[GreenDAO]: https://github.com/Razeeman/Android-Database-Comparison/tree/master/app/src/main/java/com/example/database/comparison/dbms/greendao
[ObjectBox]: https://github.com/Razeeman/Android-Database-Comparison/tree/master/app/src/main/java/com/example/database/comparison/dbms/objectbox
[Room]: https://github.com/Razeeman/Android-Database-Comparison/tree/master/app/src/main/java/com/example/database/comparison/dbms/room
[Realm]: https://github.com/Razeeman/Android-Database-Comparison/tree/master/app/src/main/java/com/example/database/comparison/dbms/realm
[Dagger Module]: https://github.com/Razeeman/Android-Database-Comparison/blob/master/app/src/main/java/com/example/database/comparison/di/AppModule.kt
