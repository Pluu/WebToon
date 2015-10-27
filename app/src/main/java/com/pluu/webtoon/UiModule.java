package com.pluu.webtoon;

import com.pluu.webtoon.ui.DetailActivity;
import com.pluu.webtoon.ui.EpisodesActivity;
import com.pluu.webtoon.ui.MainFragment;
import com.pluu.webtoon.ui.WebtoonListFragment;
import dagger.Module;

/**
 * Created by nohhs on 2015-03-17.
 */
@Module(
	injects = {
		MainFragment.class,
		WebtoonListFragment.class,
		EpisodesActivity.class,
		DetailActivity.class
	},
	complete = false,
	library = true
)
public final class UiModule {
}
