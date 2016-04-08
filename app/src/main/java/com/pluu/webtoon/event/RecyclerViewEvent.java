package com.pluu.webtoon.event;

/**
 * RecyclerView Click Event
 * Created by PLUUSYSTEM-SURFACE on 2016-04-08.
 */
public class RecyclerViewEvent {
    private final int pos;

    public RecyclerViewEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
