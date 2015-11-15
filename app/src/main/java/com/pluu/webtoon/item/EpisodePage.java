package com.pluu.webtoon.item;

import android.text.TextUtils;

import java.util.List;

import com.pluu.support.impl.AbstractEpisodeApi;

/**
 * Episode Page Info Class
 * Created by nohhs on 2015-04-06.
 */
public class EpisodePage {
	private final AbstractEpisodeApi api;
	public String nextLink;
	public List<Episode> episodes;

	public EpisodePage(AbstractEpisodeApi nextApi) {
		this.api = nextApi;
	}

	public List<Episode> getEpisodes() {
        return episodes;
    }

	public String getNextLink() {
		return nextLink;
	}

	public String moreLink() {
		if (TextUtils.isEmpty(nextLink)) {
			return null;
		}
		return api.moreParseEpisode(this);
	}
}
