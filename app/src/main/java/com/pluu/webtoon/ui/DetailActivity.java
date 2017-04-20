package com.pluu.webtoon.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.support.impl.NAV_ITEM;
import com.pluu.webtoon.AppController;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.item.DETAIL_TYPE;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.item.ERROR_TYPE;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;
import com.pluu.webtoon.ui.detail.BaseDetailFragment;
import com.pluu.webtoon.ui.detail.DaumChattingFragment;
import com.pluu.webtoon.ui.detail.DaumMultiFragment;
import com.pluu.webtoon.ui.detail.DefaultDetailFragment;
import com.pluu.webtoon.ui.detail.FirstBindListener;
import com.pluu.webtoon.ui.detail.ToggleListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.pluu.webtoon.utils.MessageUtilsKt.getMessage;

/**
 * 상세화면 Activity
 * Created by nohhs on 15. 3. 2.
 */
public class DetailActivity extends AppCompatActivity
    implements ToggleListener, FirstBindListener {

    private final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar_actionbar) Toolbar toolbar;
    @BindView(R.id.btnPrev) Button btnPrev;
    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvSubTitle) TextView tvSubTitle;
    @BindView(R.id.bottomMenu) LinearLayout bottomMenu;

    @Inject RealmHelper realmHelper;

    private ProgressDialog dlg;
    private int titleColor, statusColor;

    private int SWIPE_MIN_DISTANCE;
    private int SWIPE_THRESHOLD_VELOCITY;
    private ObjectAnimator statusBarAnimator;

    private AbstractDetailApi serviceApi;
    private NAV_ITEM service;
    private Detail currentItem;
    private Episode episode;

    private final long DELAY_TIME = TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS);
    private boolean loadingFlag;

    private GestureDetector gd;

    private boolean isFragmentAttach = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ((AppController) getApplicationContext()).getRealmHelperComponent().inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initSupportActionBar();
        getApi();
        initView();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        SWIPE_MIN_DISTANCE = metrics.widthPixels / 3;
        SWIPE_THRESHOLD_VELOCITY = metrics.widthPixels / 2;

        loadingFlag = false;
        gd = new GestureDetector(this, listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loadingFlag) {
            loading(episode);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingFlag = true;
    }

    private void initSupportActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getApi() {
        Intent intent = getIntent();
        service = (NAV_ITEM) intent.getSerializableExtra(Const.EXTRA_API);
        serviceApi = AbstractDetailApi.getApi(this, service);
    }

    private void initView() {
        episode = getIntent().getParcelableExtra(Const.EXTRA_EPISODE);
        tvSubTitle.setText(episode.getTitle());

        dlg = new ProgressDialog(this);
        dlg.setMessage(getString(R.string.msg_loading));

        titleColor = getIntent().getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK);
        statusColor = getIntent().getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK);

        btnPrev.setEnabled(false);
        btnNext.setEnabled(false);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(getBgColorAnimator(), getStatusBarAnimator());
        set.setDuration(1000L);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }

    @NonNull
    private ValueAnimator getBgColorAnimator() {
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, value, true);

        ValueAnimator bgColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), value.data, titleColor);
        bgColorAnimator.addUpdateListener(animation -> {
            Integer value1 = (Integer) animation.getAnimatedValue();
            toolbar.setBackgroundColor(value1);
            btnPrev.setBackgroundColor(value1);
            btnNext.setBackgroundColor(value1);
        });
        bgColorAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ViewCompat.setBackground(btnNext, getStateListBgDrawable());
                ViewCompat.setBackground(btnPrev, getStateListBgDrawable());

                btnNext.setTextColor(getStateListTextDrawable());
                btnPrev.setTextColor(getStateListTextDrawable());
            }
        });
        return bgColorAnimator;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator getStatusBarAnimator() {
        if (statusBarAnimator != null) {
            statusBarAnimator.cancel();
        }
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        TypedValue resValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, resValue, true);
        statusBarAnimator = ObjectAnimator.ofInt(getWindow(), "statusBarColor", resValue.data,
            titleColor);
        statusBarAnimator.setEvaluator(argbEvaluator);
        return statusBarAnimator;
    }

    private StateListDrawable getStateListBgDrawable() {
        StateListDrawable list = new StateListDrawable();
        // disabled
        list.addState(new int[]{-android.R.attr.state_enabled}, new ColorDrawable(Color.GRAY));
        // pressed
        list.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.WHITE));
        // enabled
        list.addState(new int[]{android.R.attr.state_enabled}, new ColorDrawable(titleColor));
        return list;
    }

    private ColorStateList getStateListTextDrawable() {
        int[][] state = {
            new int[]{-android.R.attr.state_enabled},
            new int[]{android.R.attr.state_pressed},
            new int[]{android.R.attr.state_enabled},
        };

        int[] colors = {
            Color.WHITE,
            titleColor,
            Color.WHITE
        };

        return new ColorStateList(state, colors);
    }

    private void loading(Episode item) {
        Log.i(TAG, "Load Detail: " + item.getToonId() + ", " + item.getEpisodeId());
        if (currentItem != null) {
            currentItem.prevLink = currentItem.nextLink = null;
        }

        getRequestApi(item)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(disposable -> dlg.show())
            .doOnSuccess(detail -> dlg.dismiss())
            .subscribe(getRequestSubscriber());
    }

    //	@RxLogObservable
    private Single<Detail> getRequestApi(final Episode item) {
        return Single.defer(() -> Single.just(serviceApi.parseDetail(item)));
    }

    //	@RxLogSubscriber
    @NonNull
    private Consumer<Detail> getRequestSubscriber() {
        return item -> {
            if (item == null
                || item.list == null || item.list.isEmpty()
                || item.errorType != null) {

                ERROR_TYPE type;

                if (item != null && item.errorType != null) {
                    type = item.errorType;
                } else {
                    type = ERROR_TYPE.DEFAULT_ERROR;
                }

                new AlertDialog.Builder(DetailActivity.this)
                    .setMessage(getMessage(type, getBaseContext()))
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok,
                        (dialogInterface, i) -> finish())
                    .show();
                return;
            }

            readAsync(item);

            currentItem = item;
            tvTitle.setText(item.title);
            btnPrev.setEnabled(!TextUtils.isEmpty(item.prevLink));
            btnNext.setEnabled(!TextUtils.isEmpty(item.nextLink));

            fragmentInit(item.type);
            fragmentAttach(item.list);
        };
    }

    private void fragmentInit(DETAIL_TYPE type) {
        if (isFragmentAttach) {
            return;
        }
        Fragment f = null;
        switch (type) {
            case DEFAULT:
                f = new DefaultDetailFragment(gd, bottomMenu.getHeight());
                break;
            case DAUM_CHATTING:
                f = new DaumChattingFragment(gd);
                break;
            case DAUM_MULTI:
                f = new DaumMultiFragment(gd);
                break;
        }
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, f, Const.DETAIL_FRAG_TAG)
            .commit();
        isFragmentAttach = true;
    }

    private void fragmentAttach(List<DetailView> list) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Const.DETAIL_FRAG_TAG);
        if (fragment != null) {
            ((BaseDetailFragment) fragment).loadView(list);
        }
    }

    /**
     * Read Detail Item
     *
     * @param item Item
     */
    private void readAsync(Detail item) {
        realmHelper.readEpisode(service, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menu_item_share:
                // 공유하기
                if (currentItem != null && serviceApi != null) {
                    ShareItem sender = serviceApi.getDetailShare(episode, currentItem);
                    if (sender != null) {
                        Log.i(TAG, "Share=" + sender);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, sender.title);
                        intent.putExtra(Intent.EXTRA_TEXT, sender.url);
                        startActivity(Intent.createChooser(intent, "Share"));
                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btnPrev, R.id.btnNext})
    public void onMovePage(View view) {
        String link;
        link = view.getId() == R.id.btnPrev ? currentItem.prevLink : currentItem.nextLink;
        if (TextUtils.isEmpty(link)) {
            return;
        }
        episode.setEpisodeId(link);
        loadingFlag = false;
        loading(episode);
    }

    private final Handler mToggleHandler = new Handler(msg -> {
        toggleHideBar();
        return true;
    });

    /**
     * Detects and toggles immersive mode.
     */
    private void toggleHideBar() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();

        if ((uiOptions & View.SYSTEM_UI_FLAG_LOW_PROFILE) == 0) {
            moveToAxisY(toolbar, true);
            moveToAxisY(bottomMenu, false);
        } else {
            moveRevert(toolbar);
            moveRevert(bottomMenu);
        }

        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private void moveToAxisY(View view, boolean isToTop) {
        view.animate()
            .translationY(isToTop ? -view.getHeight() : view.getHeight())
            .start();
    }

    private void moveRevert(View view) {
        view.animate().translationY(0).start();
    }

    private final GestureDetector.SimpleOnGestureListener listener
        = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            toggleDelay(false);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onMovePage(btnNext);
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onMovePage(btnPrev);
                return true;
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private void toggleDelay(boolean isDelay) {
        final int TOGGLE_ID = 0;
        mToggleHandler.removeMessages(TOGGLE_ID);
        mToggleHandler.sendEmptyMessageDelayed(TOGGLE_ID, isDelay ? DELAY_TIME : 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    public void childCallToggle(boolean isDelay) {
        toggleDelay(isDelay);
    }

    @Override
    public void loadingHide() {
        dlg.dismiss();
    }

    @Override
    public void firstBind() {
        fragmentAttach(currentItem.list);
    }

}
