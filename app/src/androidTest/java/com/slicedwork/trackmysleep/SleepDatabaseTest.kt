package com.slicedwork.trackmysleep

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.slicedwork.trackmysleep.data.source.local.dao.SleepDatabaseDao
import com.slicedwork.trackmysleep.data.source.local.database.SleepDatabase
import com.slicedwork.trackmysleep.data.source.local.entity.SleepNight
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        assertEquals(tonight.sleepQuality, -1)
    }

    @Test
    @Throws(Exception::class)
    fun insertUpdateAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        var tonight = sleepDao.getTonight()
        tonight.sleepQuality = 5
        sleepDao.update(tonight)
        tonight = sleepDao.get(tonight.nightId)
        assertEquals(tonight.sleepQuality, 5)
    }

    @Test
    @Throws(Exception::class)
    fun insertDeleteAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        var tonight = sleepDao.getTonight()
        tonight.sleepQuality = 5
        sleepDao.delete(tonight)
        tonight = sleepDao.get(tonight.nightId)
        assertEquals(tonight, null)
    }

    @Test
    @Throws(Exception::class)
    fun insertDeleteAllAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        var tonight = sleepDao.getTonight()
        tonight.sleepQuality = 5
        sleepDao.clear()
        tonight = sleepDao.get(tonight.nightId)
        assertEquals(tonight, null)
    }
}