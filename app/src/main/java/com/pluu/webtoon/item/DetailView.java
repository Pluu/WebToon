package com.pluu.webtoon.item;

/**
 * 상세데이터 타입
 * Created by nohhs on 2015-03-13.
 */
public class DetailView {
	private final VIEW_TYPE type;
	private final String value;

	public DetailView(VIEW_TYPE type, String value) {
		this.type = type;
		this.value = value;
	}

	public VIEW_TYPE getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public static DetailView createImage(String value) {
		return generate(VIEW_TYPE.IMAGE, value);
	}

	public static DetailView createText(String value) {
		return generate(VIEW_TYPE.TEXT, value);
	}

	private static DetailView generate(VIEW_TYPE type, String value) {
		return new DetailView(type, value);
	}

	@Override
	public String toString() {
		return "DetailView{" +
			"type=" + type +
			", value='" + value + '\'' +
			'}';
	}
}
