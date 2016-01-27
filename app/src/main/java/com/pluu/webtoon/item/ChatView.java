package com.pluu.webtoon.item;

/**
 * Daum, Chatting Item View
 * Created by PLUUSYSTEM-NEW on 2016-01-24.
 */
public class ChatView {
    private final String imgUrl;
    private final String name, text;
    private float hRatio;

    public ChatView(String imgUrl, String name, String text) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.text = text;
    }

    public void setHRatio(float hRatio) {
        this.hRatio = hRatio;
    }

    public float getHRatio() {
        return hRatio;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
