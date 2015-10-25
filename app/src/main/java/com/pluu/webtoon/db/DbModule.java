package com.pluu.webtoon.db;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Singleton;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;

/**
 * Created by nohhs on 2015-03-17.
 */
@Module(complete = false, library = true)
public final class DbModule {
	@Provides @Singleton
	SQLiteOpenHelper provideOpenHelper(Application application) {
		return new DbOpenHelper(application);
	}

	@Provides @Singleton
	SqlBrite provideSqlBrite() {
		return SqlBrite.create();
	}

	@Provides @Singleton
	BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
		return sqlBrite.wrapDatabaseHelper(helper);
	}
}
