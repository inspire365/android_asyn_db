android_asyn_db
===============

Asynchronous DB service for android application

Asynchronous DB service for android is a framework for uniformly asynchronous access to android DB
The DB service provides both SQLite DB and sqlcipher DB, the secured encrypted version of the SQLite.

Features:

1. Asynchronous DB operation. Therefore it will not slow down the main UI thread
2. Support two type of DB, normal SQLite and the secured encrypted SQLite
3. Priority queue type DB operation.


General Usage and API
---------------------

This framework mainly takes three components to provide service:

1. Worker component, the worker is just a thread that take charge of all
   the work, such as insert DB, query DB. depend on different DB you select,
   there are two diffent DB, SQLite DB worker (SqliteDbWorker.java) and
   the secured version SQLCipher DB (CipherDbWorker.java).

2. Handler component (IDbHandler.java), the Handler component handle all the
   specific DB work, include create DB, migrate DB, and also should handle the
   real specific work of the DB operations. The handler must be implemented and register
   to DB service. Then when worker can use the handler to handle specific Db operations.

3. Observer component (IDbObserver.java), all the DB operation result will be
   delivered to the observers.The same, the observer should be registered to the DB service.
   If any result, the DB service can deliver back the result to user via registered observers.


Example
---------------------

The Demo project is a simple example to use the DB service

1. Implement IDbHandler interfaces to handle the bussiness logical of
   all the DB operations, DB creation, migration and specific
   DB Requests. Mind that, the DB connection should be cast
   back to the DB you select, either SQLite or SQLCipher.

2. Implement IDbObserver interfaces to receive DB operation results
   Attention that the observer notify is just a normal callback, it's
   your resposibility to handle it well, including post the result
   back to UI. You can not directly set the result back to UI, otherwise
   it will be a crash, You can try runOnUiThread or android Hanlder (not
   the above IDbHandler).

3. Setup the DB service, register handlers and observers, please check
   the Demo example

4. Derived a request from request base class and then put the request to
   the DB service. For example, the SampleInsertReq in the demo project.


For more information about SQLCipher, please visit:
http://sqlcipher.net/sqlcipher-for-android/
https://github.com/sqlcipher/android-database-sqlcipher

For more about android Hanlder, please visit:
https://developer.android.com/training/multiple-threads/communicate-ui.html


At Last, because I'm new to Java/Android, If you have any good advices, Please
contact me. Thanks in advance!


License
-------
Copyright (C) 2014

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

           By Dan

Thanks!

