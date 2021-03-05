/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.androidkotlinfundamentals.database.SleepDatabase
import com.example.androidkotlinfundamentals.database.SleepDatabaseDao
import com.example.androidkotlinfundamentals.database.SleepNight
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

//
//import androidx.room.Room
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.platform.app.InstrumentationRegistry
//import com.example.android.trackmysleepquality.database.SleepDatabase
//import com.example.android.trackmysleepquality.database.SleepDatabaseDao
//import com.example.android.trackmysleepquality.database.SleepNight
//import org.junit.Assert.assertEquals
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.io.IOException
//
//
///**
// * This is not meant to be a full set of tests. For simplicity, most of your samples do not
// * include tests. However, when building the Room, it is helpful to make sure it works before
// * adding the UI.
// */
//Аннотация @RunWith идентифицирует средство запуска тестов,
// то есть программу, которая устанавливает и выполняет тесты.
@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase
    // Во время установки выполняется функция, помеченная @Before,
    // и она создает в памяти SleepDatabase с SleepDatabaseDao.
    // «В памяти» означает, что эта база данных не сохраняется в
    // файловой системе и будет удалена после запуска тестов.
    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                // Также при построении базы данных в памяти код вызывает другой метод,
            // специфичный для теста, allowMainThreadQueries.
            // По умолчанию вы получаете сообщение об ошибке, если пытаетесь выполнить
            // запросы в основном потоке. Этот метод позволяет запускать тесты в основном
            // потоке, что следует делать только во время тестирования.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDatabaseDao
    }
    // Когда тестирование завершено, функция, помеченная @After, выполняется для закрытия базы данных.
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
    // В тестовом методе, помеченном @Test, вы создаете, вставляете и извлекаете SleepNight и
    // утверждаете, что они одинаковы. Если что-то пойдет не так,
    // вызовите исключение. В реальном тесте у вас будет несколько методов @Test.
    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        assertEquals(tonight?.sleepQuality, -1)
    }
}