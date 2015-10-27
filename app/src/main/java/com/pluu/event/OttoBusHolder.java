package com.pluu.event;

import com.squareup.otto.Bus;

/**
 * OttoBusHolder, MainThread Support
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class OttoBusHolder extends Bus {

	private static final Bus mBus = new OttoMainThreadBus();

	public static Bus get() {
		return mBus;
	}
}
