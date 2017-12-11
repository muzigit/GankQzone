package lifeng.gankqzone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.R;
import lifeng.gankqzone.base.BaseActivity;
import lifeng.gankqzone.util.StatusBarUtil;
import lifeng.gankqzone.util.glide.GlideUtils;
import lifeng.gankqzone.view.MyScrollView;
import lifeng.gankqzone.view.SwipeBackLayout;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static lifeng.gankqzone.util.T.isShow;

/**
 * Created by lifeng on 2017/10/25.
 *
 * @description
 */
public class ImageActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tvCurPosition)
    TextView mTvCurPosition;
    @BindView(R.id.swipe_layout)
    SwipeBackLayout mSwipeLayout;
    @BindView(R.id.btn_titlebar_left)
    ImageView mBtnTitlebarLeft;
    @BindView(R.id.tv_titlebar_name)
    TextView mTvTitlebarName;
    @BindView(R.id.rl_top)
    RelativeLayout mRlTop;
    @BindView(R.id.scrollview)
    MyScrollView mScrollview;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;

    private Unbinder mUnbinder;

    public static final String TRANSIT_PIC = "image";
    private static final String POSITION = "POSITION";
    private static final String IMAGE_URLS = "IMAGE_URLS";
    private static final String TITLE = "IMAGETitle";

    private static final String IMAGEURL = "IMAGEURL";
    private int mPosition;
    private ArrayList<String> mUrlLists = new ArrayList<>();
    private PhotoView mPhotoView;
    private static ActivityOptionsCompat sOptionsCompat;

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*** 全屏显示 ***/
//        ScreenUtils.setFullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mUnbinder = ButterKnife.bind(this);

        initIntentData();
        initSwipeLayout();
        initViewPager();
        initListener();
    }

    private void initSwipeLayout() {
        /*** 设置状态栏颜色 ***/
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, android.R.color.black));
        //将背景初始化为不透明 不然有时会出现一进来是透明色的情况
        mRelativeLayout.getBackground().setAlpha(255);

        mSwipeBackHelper.setSwipeBackEnable(true);
        mSwipeLayout.setDragDirectMode(SwipeBackLayout.DragDirectMode.VERTICAL);

        mScrollview.getBackground().mutate().setAlpha(100);
        mRlTop.getBackground().mutate().setAlpha(100);
    }

    private void initListener() {
        mSwipeLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                //根据滚动距离 将背景逐渐设置为透明
                mRelativeLayout.getBackground().setAlpha(255 - (int) Math.ceil(255 * fractionAnchor));
                DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                df.setMaximumFractionDigits(1);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String dd = df.format(fractionAnchor);
                double alpah = 1 - (Float.valueOf(dd) + 0.8);

                if (fractionAnchor == 0 && isShow) {
                    mScrollview.setAlpha(1f);
                    mRlTop.setAlpha(1f);
                    mRlTop.setVisibility(View.VISIBLE);
                    mScrollview.setVisibility(View.VISIBLE);
                } else {
                    if (alpah == 0) {
                        mRlTop.setVisibility(View.GONE);
                        mScrollview.setVisibility(View.GONE);
                        mScrollview.setAlpha(1f);
                        mRlTop.setAlpha(1f);
                    } else {
                        if (mRlTop.getVisibility() != View.GONE) {
                            mRlTop.setAlpha((float) alpah);
                            mScrollview.setAlpha((float) alpah);
                        }
                    }
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTvCurPosition.setText((position + 1) + " / " + mUrlLists.size());
                if (position == 0) {
                    mSwipeBackHelper.setSwipeBackEnable(true);
                } else {
                    mSwipeBackHelper.setSwipeBackEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBtnTitlebarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager() {
        SamplePagerAdapter adapter = new SamplePagerAdapter(mUrlLists);
        mViewPager.setAdapter(adapter);
        if (mPosition != -1) {
            mViewPager.setCurrentItem(mPosition);
        }
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String imageUrl = intent.getStringExtra(IMAGEURL);
            if (imageUrl != null) {
                mUrlLists.add(imageUrl);
            } else {
                mUrlLists = intent.getStringArrayListExtra(IMAGE_URLS);
            }

            mPosition = intent.getIntExtra(POSITION, -1);
        }
    }

    public static void startIntent(Activity context, String imageUrl, String title) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(IMAGEURL, imageUrl);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
        //设置Activity出入场动画
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void startIntent(Activity context, ArrayList<String> imageUrls, int position, String title) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putStringArrayListExtra(IMAGE_URLS, imageUrls);
        intent.putExtra(POSITION, position);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private class SamplePagerAdapter extends PagerAdapter {

        private final List<String> lists;

        public SamplePagerAdapter(List<String> lists) {
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            mPhotoView = new PhotoView(container.getContext());

            mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (isShow) {
                        isShow = false;
                        setView(mRlTop, false);
                        setView(mScrollview, false);
                    } else {
                        isShow = true;
                        setView(mRlTop, true);
                        setView(mScrollview, true);
                    }
                }
            });

            GlideUtils.disPlayShowLoading(container.getContext(), lists.get(position), mPhotoView, null);
            container.addView(mPhotoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return mPhotoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private void setView(final View view, final boolean isShow) {
        AlphaAnimation alphaAnimation;
        if (isShow) {
            alphaAnimation = new AlphaAnimation(0, 1);
        } else {
            alphaAnimation = new AlphaAnimation(1, 0);
        }
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(500);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mUrlLists.size() > 0) {
            mUrlLists.clear();
            mUrlLists = null;
        }
        if (sOptionsCompat != null) {
            sOptionsCompat = null;
        }
    }
}
