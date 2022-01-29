package com.pluu.webtoon.data.local

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pluu.webtoon.data.local.common.SqliteDatabaseTestHelper
import com.pluu.webtoon.data.local.common.SqliteTestDbOpenHelper
import com.pluu.webtoon.data.local.dao.RoomDao
import com.pluu.webtoon.data.local.db.AppDatabase
import com.pluu.webtoon.data.local.model.DBEpisode
import com.pluu.webtoon.data.local.model.DBToon
import com.pluu.webtoon.model.ToonId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("ClassName", "PrivatePropertyName")
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class Migartion1_2Test {

    private val TEST_DB_NAME = "test-db"

    @get:Rule
    var helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        listOf(
            AppDatabase.AutoMigration_1_2()
        )
    )

    private val db = Room.databaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java, TEST_DB_NAME
    ).build()

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
        helper.closeWhenFinished(db)
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

        helper.runMigrationsAndValidate(TEST_DB_NAME, 2, true)

        runTest {
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

        helper.runMigrationsAndValidate(TEST_DB_NAME, 2, true)

        runTest {
            assertTrue(db.roomDao().isReaded(episode.service, episode.toonId, episode.episodeId))
            assertFalse(db.roomDao().isReaded(episode.service, episode.toonId, "123"))
        }
    }

    private suspend fun RoomDao.isReaded(
        serviceId: String,
        toonId: ToonId,
        episodeId: String
    ): Boolean {
        val list = getEpisode(serviceId, toonId).first()
        return list.any {
            serviceId == it.service && toonId == it.toonId && episodeId == it.episodeId
        }
    }
}