package com.pluu.webtoon.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.pluu.support.impl.AbstractDetailApi;
import com.pluu.support.impl.ServiceConst;
import com.pluu.webtoon.R;
import com.pluu.webtoon.common.Const;
import com.pluu.webtoon.db.RealmHelper;
import com.pluu.webtoon.item.DETAIL_TYPE;
import com.pluu.webtoon.item.Detail;
import com.pluu.webtoon.item.DetailView;
import com.pluu.webtoon.item.Episode;
import com.pluu.webtoon.item.ShareItem;
import com.pluu.webtoon.ui.detail.BaseDetailFragment;
import com.pluu.webtoon.ui.detail.DaumChattingFragment;
import com.pluu.webtoon.ui.detail.DefaultDetailFragment;
import com.pluu.webtoon.ui.detail.FirstBindListener;
import com.pluu.webtoon.ui.detail.ToggleListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 상세화면 Activity
 * Created by nohhs on 15. 3. 2.
 */
public class DetailActivity extends AppCompatActivity
    implements ToggleListener, FirstBindListener {

    private final String TAG = DetailActivity.class.getSimpleName();

    @Bind(R.id.toolbar_actionbar)
    Toolbar toolbar;
    @Bind(R.id.btnPrev)
    Button btnPrev;
    @Bind(R.id.btnNext)
    Button btnNext;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvSubTitle)
    TextView tvSubTitle;
    @Bind(R.id.bottomMenu)
    LinearLayout bottomMenu;

    private ProgressDialog dlg;
    private int titleColor, statusColor;

    private int SWIPE_MIN_DISTANCE;
    private int SWIPE_THRESHOLD_VELOCITY;
    private ObjectAnimator statusBarAnimator;

    private AbstractDetailApi serviceApi;
    private ServiceConst.NAV_ITEM service;
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        }
    }

    private void getApi() {
        Intent intent = getIntent();
        service = (ServiceConst.NAV_ITEM) intent.getSerializableExtra(Const.EXTRA_API);
        serviceApi = AbstractDetailApi.getApi(service);
    }

    private void initView() {
        episode = getIntent().getParcelableExtra(Const.EXTRA_EPISODE);
        tvSubTitle.setText(episode.getTitle());

        dlg = new ProgressDialog(this);
        dlg.setMessage(getString(R.string.msg_loading));

        titleColor = getIntent().getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK);
        statusColor = getIntent().getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK);

        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, value, true);

        btnPrev.setEnabled(false);
        btnNext.setEnabled(false);

        ValueAnimator bg = ValueAnimator.ofObject(new ArgbEvaluator(), value.data, titleColor);
        bg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                toolbar.setBackgroundColor(value);
                btnPrev.setBackgroundColor(value);
                btnNext.setBackgroundColor(value);
            }
        });
        bg.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                btnNext.setBackgroundDrawable(getStateListBgDrawable());
                btnPrev.setBackgroundDrawable(getStateListBgDrawable());

                btnNext.setTextColor(getStateListTextDrawable());
                btnPrev.setTextColor(getStateListTextDrawable());
            }
        });
        bg.setDuration(1000L);
        bg.setInterpolator(new DecelerateInterpolator());
        bg.start();

        changeStatusBar();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBar() {
        if (statusBarAnimator != null) {
            statusBarAnimator.cancel();
        }
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        TypedValue resValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, resValue, true);
        statusBarAnimator = ObjectAnimator.ofInt(getWindow(), "statusBarColor", resValue.data,
                statusColor);
        statusBarAnimator.setDuration(250L);
        statusBarAnimator.setEvaluator(argbEvaluator);
        statusBarAnimator.start();
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
        dlg.show();

        getRequestApi(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getRequestSubscriber());
    }

    //	@RxLogObservable
    private Observable<Detail> getRequestApi(final Episode item) {
        return Observable
                .create(new Observable.OnSubscribe<Detail>() {
                    @Override
                    public void call(Subscriber<? super Detail> subscriber) {
                        Detail detail = serviceApi.parseDetail(item);
                        subscriber.onNext(detail);
                        subscriber.onCompleted();
                    }
                });
    }

    //	@RxLogSubscriber
    @NonNull
    private Subscriber<Detail> getRequestSubscriber() {
        return new Subscriber<Detail>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                dlg.dismiss();
            }

            @Override
            public void onNext(Detail item) {
                dlg.dismiss();
                if (item != null && item.errorType != null) {
                    new AlertDialog.Builder(DetailActivity.this)
                            .setMessage(item.errorMsg)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                            .show();
                    return;
                } else if (item == null || item.list == null || item.list.isEmpty()) {
                    Toast.makeText(getBaseContext(), R.string.network_fail, Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    return;
                }

                readAsync(item);

                currentItem = item;
                tvTitle.setText(item.title);
                btnPrev.setEnabled(!TextUtils.isEmpty(item.prevLink));
                btnNext.setEnabled(!TextUtils.isEmpty(item.nextLink));

                fragmentInit(item.type);
                fragmentAttach(item.list);
            }
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
                break;
        }
        if (f != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, f, Const.DETAIL_FRAG_TAG)
                    .commit();
        }
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
     * @param item Item
     */
    private void readAsync(Detail item) {
        RealmHelper helper = RealmHelper.getInstance();
        helper.readEpisode(this, service, item);
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

    private final Handler mToggleHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            toggleHideBar();
            return true;
        }
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
