package com.pluu.webtoon.data

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pluu.webtoon.data.common.SqliteDatabaseTestHelper
import com.pluu.webtoon.data.common.SqliteTestDbOpenHelper
import com.pluu.webtoon.data.dao.RoomDao
import com.pluu.webtoon.data.db.AppDatabase
import com.pluu.webtoon.data.db.migration.migration_1_2
import com.pluu.webtoon.data.model.DBEpisode
import com.pluu.webtoon.data.model.DBToon
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("ClassName", "EXPERIMENTAL_API_USAGE")
@RunWith(AndroidJUnit4::class)
class Migartion1_2Test {

    private val TEST_DB_NAME = "test-db"

    @get:Rule
    var migrationHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    private lateinit var mSqliteTestDbHelper: SqliteTestDbOpenHelper

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mSqliteTestDbHelper = SqliteTestDbOpenHelper(
            ApplicationProvider.getApplicationContext(),
            TEST_DB_NAME
        )
        SqliteDatabaseTestHelper.createTable(mSqliteTestDbHelper)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        SqliteDatabaseTestHelper.clearDatabase(mSqliteTestDbHelper)
    }

    @Test
    fun migrationToon() {
        val toon = DBToon("TestService", "TestToon")
        SqliteDatabaseTestHelper.insertToon(
            toon.service,
            toon.toonId,
            mSqliteTestDbHelper
        )

        migrationHelper.runMigrationsAndValidate(
            TEST_DB_NAME,
            2,
            true,
            migration_1_2
        )

        val db = getMigratedRoomDatabase()
        runBlocking {
            assertTrue(db.roomDao().isFavorite(toon.service, toon.toonId))
            assertFalse(db.roomDao().isFavorite(toon.service, "123"))
        }
    }

    @Test
    fun migrationEpisode() {
        val episode = DBEpisode("TestService", "TestToon", "TestEpisode")
        SqliteDatabaseTestHelper.insertEpisode(
            episode.service,
            episode.toonId,
            episode.episodeId,
            mSqliteTestDbHelper
        )

        migrationHelper.runMigrationsAndValidate(
            TEST_DB_NAME,
            2,
            true,
            migration_1_2
        )

        val db = getMigratedRoomDatabase()
        runBlocking {
            assertTrue(db.roomDao().isReaded(episode.service, episode.toonId, episode.episodeId))
            assertFalse(db.roomDao().isReaded(episode.service, episode.toonId, "123"))
        }
    }

    private suspend fun RoomDao.isReaded(
        serviceId: String,
        toonId: String,
        episodeId: String
    ): Boolean {
        return getEpisode(serviceId, toonId).any {
            serviceId == it.service && toonId == it.toonId && episodeId == it.episodeId
        }
    }

    private fun getMigratedRoomDatabase(): AppDatabase {
        val database = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java, TEST_DB_NAME
        ).addMigrations(migration_1_2)
            .build()
        // close the database and release any stream resources when the test finishes
        migrationHelper.closeWhenFinished(database)
        return database
    }

}