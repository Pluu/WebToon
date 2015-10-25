package com.pluu.webtoon;

import dagger.Module;

/**
 * Created by nohhs on 2015-03-17.
 */
@Module(
	injects = {
		MainFragment.class,
		EpisodesActivity.class,
		DetailActivity.class
	},
	complete = false,
	library = true
)
public final class UiModule {
}
