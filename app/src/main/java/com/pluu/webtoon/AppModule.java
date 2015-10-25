package com.pluu.webtoon;

import android.app.Application;

import javax.inject.Singleton;

import com.pluu.webtoon.db.DbModule;
import dagger.Module;
import dagger.Provides;

/**
 * Created by nohhs on 2015-03-17.
 */
@Module(
	includes = {
		DbModule.class,
		UiModule.class
	}
)
public final class AppModule {
	private final Application application;

	AppModule(Application application) {
		this.application = application;
	}

	@Provides
	@Singleton
	Application provideApplication() {
		return application;
	}
}
