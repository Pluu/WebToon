package com.pluu.event;

/**
 * RxBusProvider from RxJava
 * Created by PLUUSYSTEM-NEW on 2016-03-21.
 */
public class RxBusProvider {
    private static final RxBus BUS = new RxBus();

    private RxBusProvider() {
        // No instances.
    }

    public static RxBus getInstance() {
        return BUS;
    }
}
