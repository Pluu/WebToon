package com.pluu.webtoon.item;

/**
 * 상세데이터 타입
 * Created by nohhs on 2015-03-13.
 */
public class DetailView {
	private final VIEW_TYPE type;
	private final String value;
	private final ChatView chatValue;

	public DetailView(VIEW_TYPE type, String value) {
		this.type = type;
		this.value = value;
		this.chatValue = null;
	}

	public DetailView(VIEW_TYPE type, ChatView value) {
		this.type = type;
		this.value = null;
		this.chatValue = value;
	}

	public VIEW_TYPE getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public ChatView getChatValue() {
		return chatValue;
	}

	public static DetailView createImage(String value) {
		return generate(VIEW_TYPE.IMAGE, value);
	}

	public static DetailView createText(String value) {
		return generate(VIEW_TYPE.TEXT, value);
	}

	public static DetailView createChatNotice(String value) {
		return new DetailView(VIEW_TYPE.CHAT_NOTICE, new ChatView(null, null, value));
	}

	public static DetailView createChatNoticeImage(String img) {
		return new DetailView(VIEW_TYPE.CHAT_NOTICE_IMAGE, new ChatView(img, null, null));
	}

	public static DetailView createChatLeft(String img, String name, String text) {
		ChatView item = new ChatView(img, name, text);
		return new DetailView(VIEW_TYPE.CHAT_LEFT, item);
	}

	public static DetailView createChatRight(String img, String name, String text) {
		ChatView item = new ChatView(img, name, text);
		return new DetailView(VIEW_TYPE.CHAT_RIGHT, item);
	}

	public static DetailView createChatEmpty() {
		return generate(VIEW_TYPE.CHAT_EMPTY, null);
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
