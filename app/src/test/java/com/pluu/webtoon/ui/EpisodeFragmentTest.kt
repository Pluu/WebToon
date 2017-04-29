package com.pluu.webtoon.ui

import android.app.ProgressDialog
import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.model.REpisode
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Test Code
 * Created by pluu on 2017-04-30.
 */
class EpisodeFragmentTest {

    private lateinit var episodeFragment: EpisodeFragment
    private lateinit var mockRealm: RealmHelper

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockRealm = mock<RealmHelper>(RealmHelper::class.java)
        episodeFragment = EpisodeFragment().apply {
            realmHelper = mockRealm
            loadDlg = mock(ProgressDialog::class.java)
            webToonInfo = mock(WebToonInfo::class.java)
            service = NAV_ITEM.DAUM
        }
    }

    @Test
    @Throws(Exception::class)
    fun getReadAction() {
        val value = dummyEpisodes()
        `when`(mockRealm.getEpisode(any<NAV_ITEM>(), anyString())).thenReturn(value)

        val dispose = booleanArrayOf(false)
        val unsubscribe = booleanArrayOf(false)
        val onEventSuccess = booleanArrayOf(true)
        val onEventError = booleanArrayOf(false)
        val onEventCount = intArrayOf(0)

        val subscriber = TestObserver.create<List<String>>()
        episodeFragment.readAction
                .flatMapObservable {
                    episodes ->
                    Observable.fromIterable(episodes).map { it.episodeId }
                }
                .toList()
                .doOnDispose { dispose[0] = true }
                .doOnEvent { strings, throwable ->
                    onEventCount[0]++
                    if (strings != null) {
                        onEventSuccess[0] = true
                    }

                    if (throwable != null) {
                        throwable.printStackTrace()
                        onEventError[0] = true
                    }
                }
                .doOnSubscribe { _ -> unsubscribe[0] = true }
                .subscribe(subscriber)


        subscriber.awaitTerminalEvent()

        assertThat(dispose[0]).isFalse()
        assertThat(unsubscribe[0]).isTrue()
        assertThat(onEventSuccess[0]).isTrue()
        assertThat(onEventError[0]).isFalse()
        assertThat(onEventCount[0]).isEqualTo(1)
    }

    private fun dummyEpisodes() = mutableListOf<REpisode>().apply {
        add(REpisode().apply {
            episodeId = "1"
        })
        add(REpisode().apply {
            episodeId = "2"
        })
    }
}