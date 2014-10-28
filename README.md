CalDynam
========

Master of Sciences HES-SO - Semester 1 - Android Project.

Information
-----------

The target version of Android for this project is Android Lollipop (API 21).

Database
--------

This project use [GreenDAO ORM](http://greendao-orm.com/).
The database is generated using MyDaoGenerator (module of the current project).

To specify the database schema, change its declaration in CalDynamGenerator.java.
For more information about database specification, please visit the [Green DAO documentation](http://greendao-orm.com/documentation/).

To generate the database :

* Go to Gradle task section in Android Studio.
* Pick MyDaoGenerator from the Gradle project tree.
* Chose run task.