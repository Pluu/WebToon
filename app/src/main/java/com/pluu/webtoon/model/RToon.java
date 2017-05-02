package com.pluu.webtoon.model;

import io.realm.RealmObject;

/**
 * 웹툰 Model
 * Created by PLUUSYSTEM-NEW on 2015-11-11.
 */
public class RToon extends RealmObject {
	private String service;
	private String toonId;

	public RToon() { }

	public void setService(String service) {
		this.service = service;
	}

	public void setToonId(String toonId) {
		this.toonId = toonId;
	}

	public String getService() {
		return service;
	}

	public String getToonId() {
		return toonId;
	}

}
