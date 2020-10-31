package com.pluu.webtoon.utils.log

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import timber.log.Timber
import javax.inject.Inject

private const val loggerTag = "Logger"

private inline val <T : Any> T.javaClassName: String
    get() = javaClass.name

class ComponentLogger @Inject constructor() {
    fun initialize(application: Application) {
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    Timber.tag(loggerTag).d("[Activity] onCreate - ${activity.javaClassName}")
                    handleActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                    Timber.tag(loggerTag).d("[Activity] onStart - ${activity.javaClassName}")
                }

                override fun onActivityResumed(activity: Activity) {
                    Timber.tag(loggerTag).d("[Activity] onResume - ${activity.javaClassName}")
                }

                override fun onActivityPaused(activity: Activity) {
                    Timber.tag(loggerTag).d("[Activity] onPause - ${activity.javaClassName}")
                }

                override fun onActivityStopped(activity: Activity) {
                    Timber.tag(loggerTag).d("[Activity] onStop - ${activity.javaClassName}")
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                    Timber.tag(loggerTag).d("[Activity] onSaveInstance - ${activity.javaClassName}")
                }

                override fun onActivityDestroyed(activity: Activity) {
                    Timber.tag(loggerTag).d("[Activity] onDestroy - ${activity.javaClassName}")
                }
            }
        )
    }

    private fun handleActivity(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentPreAttached(
                        fm: FragmentManager,
                        f: Fragment,
                        context: Context
                    ) {

                    }

                    override fun onFragmentCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        super.onFragmentCreated(fm, f, savedInstanceState)
                        Timber.tag(loggerTag).d("[Fragment] onCreate - ${f.javaClassName}")
                    }

                    override fun onFragmentViewCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        v: View,
                        savedInstanceState: Bundle?
                    ) {
                        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                        Timber.tag(loggerTag).d("[Fragment] onViewCreated - ${f.javaClassName}")
                    }

                    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                        super.onFragmentStarted(fm, f)
                        Timber.tag(loggerTag).d("[Fragment] onStart - ${f.javaClassName}")
                    }

                    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentResumed(fm, f)
                        Timber.tag(loggerTag).d("[Fragment] onResume - ${f.javaClassName}")
                    }

                    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                        super.onFragmentPaused(fm, f)
                        Timber.tag(loggerTag).d("[Fragment] onPause - ${f.javaClassName}")
                    }

                    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                        super.onFragmentStopped(fm, f)
                        Timber.tag(loggerTag).d("[Fragment] onStop - ${f.javaClassName}")
                    }

                    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentViewDestroyed(fm, f)
                        Timber.tag(loggerTag).d("[Fragment] onViewDestroy - ${f.javaClassName}")
                    }

                    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                        super.onFragmentDestroyed(fm, f)
                        Timber.tag(loggerTag).d("[Fragment] onDestroy - ${f.javaClassName}")
                    }
                }, true
            )
        }
    }
}
