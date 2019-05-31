# Android Database Comparison

Benchmark app to compare speed of CRUD operations of different android database management systems.

Test are executed with a collection of 10000 objects of a simple model type. Execution times are averaged between 100 runs. Tests was run on the emulator.

- ```create(data: List<T>)``` Inserts list of objects into database.

- ```read(): List<T>``` Reads all objects from the database. Read is coupled with access to prevent lazy query results (Realm).

- ```update(data: List<T>)``` Updates objects in the database with data of provided objects.

- ```delete(data: List<T>)``` Delete provided objects from the database.

## Model

```kotlin
data class Person (
    var firstName: String,
    var secondsName: String,
    var age: Int
)
```

## Results

|Library|create|read|update|delete|
|:---:|:---:|:---:|:---:|:---:|
|SQLite|1062|48|1308|1203|
|GreenDAO|587|24|609|528|
|Room|582|51|587|490|
|Realm|81|82|15|25|
|SQLiteBatched|35|48|44|20|
|ObjectBox|25|36|27|10|

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
