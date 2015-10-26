package com.pluu.webtoon;

import com.pluu.webtoon.ui.DetailActivity;
import com.pluu.webtoon.ui.EpisodesActivity;
import com.pluu.webtoon.ui.MainFragment;
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
