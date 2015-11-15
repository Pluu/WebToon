package com.pluu.event;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * OttoBusHolder, MainThread Support
 * Created by PLUUSYSTEM-NEW on 2015-10-27.
 */
public class OttoBusHolder extends Bus {

	private static final Bus mBus = new OttoBusHolder();

	public static Bus get() {
		return mBus;
	}

	private final Handler handler = new Handler(Looper.getMainLooper());

	@Override public void post(final Object event) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			super.post(event);
		} else {
			asyncPost(event);
		}
	}

	public void postQueue(final Object event) {
		asyncPost(event);
	}

	private void asyncPost(final Object obj) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				OttoBusHolder.get().post(obj);
			}
		});
	}

}
