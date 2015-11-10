package com.pluu.webtoon.model;

import io.realm.RealmObject;

/**
 * 에피소드 Model
 * Created by PLUUSYSTEM-NEW on 2015-11-11.
 */
public class REpisode extends RealmObject {
	private String service;
	private String toonId;
	private String episodeId;

	public void setService(String service) {
		this.service = service;
	}

	public void setToonId(String toonId) {
		this.toonId = toonId;
	}

	public void setEpisodeId(String episodeId) {
		this.episodeId = episodeId;
	}

	public String getService() {
		return service;
	}

	public String getToonId() {
		return toonId;
	}

	public String getEpisodeId() {
		return episodeId;
	}
}
