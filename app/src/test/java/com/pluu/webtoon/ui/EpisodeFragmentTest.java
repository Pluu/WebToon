package com.pluu.webtoon.ui;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.item.WebToonInfo;
import com.pluu.webtoon.model.REpisode;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EpisodeFragmentTest {

    private EpisodeFragment episodeFragment;
    private RealmHelper mockRealm;

    @Before
    public void setUp() throws Exception {
        episodeFragment = new EpisodeFragment();
        mockRealm = mock(RealmHelper.class);
        episodeFragment.realmHelper = mockRealm;
        episodeFragment.loadDlg = mock(ProgressDialog.class);
        episodeFragment.webToonInfo = mock(WebToonInfo.class);
        episodeFragment.service = ServiceConst.NAV_ITEM.DAUM;
    }

    @Test
    public void getReadAction() throws Exception {

        List<REpisode> value = getrEpisodes();
        when(mockRealm.getEpisode(any(), anyString())).thenReturn(value);

        final boolean[] dispose = {false};
        final boolean[] unsubscribe = {false};
        final boolean[] onEventSuccess = {true};
        final boolean[] onEventError = {false};
        final int[] onEventCount = {0};


        TestObserver<List<String>> subscriber = TestObserver.create();
        episodeFragment.getReadAction()
                .flatMapObservable(episodes -> Observable.fromIterable(episodes).map(REpisode::getEpisodeId))
                .toList()
                .doOnDispose(() -> dispose[0] = true)
                .doOnEvent((strings, throwable) -> {
                    onEventCount[0]++;
                    if (strings != null) {
                        onEventSuccess[0] = true;
                    }

                    if (throwable != null) {
                        throwable.printStackTrace();
                        onEventError[0] = true;
                    }
                })
                .doOnSubscribe(disposable -> unsubscribe[0] = true)
                .subscribe(subscriber);


        subscriber.awaitTerminalEvent();

        assertThat(dispose[0]).isFalse();
        assertThat(unsubscribe[0]).isTrue();
        assertThat(onEventSuccess[0]).isTrue();
        assertThat(onEventError[0]).isFalse();
        assertThat(onEventCount[0]).isEqualTo(1);
    }

    @NonNull
    private List<REpisode> getrEpisodes() {
        List<REpisode> value = new ArrayList<>();
        REpisode e = new REpisode();
        e.setEpisodeId("1");
        value.add(e);

        e = new REpisode();
        e.setEpisodeId("2");
        value.add(e);
        return value;
    }
}