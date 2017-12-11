package lifeng.gankqzone.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lifeng.gankqzone.R;
import lifeng.gankqzone.adapter.VideoViewPagerAdapter;
import lifeng.gankqzone.base.BaseActivity;
import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.bean.fengh.TypesBean;
import lifeng.gankqzone.bean.fengh.VideoChannelRes;
import lifeng.gankqzone.network.callback.IFengHVideoView;
import lifeng.gankqzone.network.presenter.FengHVideoPresenter;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description 凤凰新闻Api
 */
public class FengHVideoActivity extends BaseActivity implements IFengHVideoView<VideoChannelRes> {

    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private FengHVideoPresenter mVideoPresenter;
    private VideoViewPagerAdapter mVideoPagerAdapter;

    private void initPresenter() {
        mVideoPresenter = new FengHVideoPresenter();
        mVideoPresenter.onAttach(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fengh_video);
        ButterKnife.bind(this);

        initPresenter();
        initData();
    }

    private void initData() {
        mVideoPresenter.getVideoChannelList(1);
    }

    @Override
    public void getVideoChannelList(List<VideoChannelRes> videoChannelRes) {
        if (videoChannelRes != null && videoChannelRes.size() > 0) {
            VideoChannelRes channelRes = videoChannelRes.get(0);
            List<TypesBean> typesBeen = channelRes.getTypes();
            typesBeen.add(0, addChanne());//新增一个频道

            mVideoPagerAdapter = new VideoViewPagerAdapter(getSupportFragmentManager(), videoChannelRes.get(0));
            mViewPager.setAdapter(mVideoPagerAdapter);
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setCurrentItem(0, false);
            mSlidingTabLayout.setViewPager(mViewPager);
        }
    }

    TypesBean typesBean;

    public TypesBean addChanne() {
        if (typesBean == null) {
            typesBean = new TypesBean();
        }
        typesBean.setName("精选");
        typesBean.setId(-1);
        typesBean.setPosition("down");
        typesBean.setChType("自己新增的频道");
        return typesBean;
    }


    @Override
    public void getVidelDetailList(List<VideoDetailRes> videoDetailRes) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPresenter != null) {
            mVideoPresenter.onDetach();
        }
    }

    @Override
    public void rxOnNext(VideoChannelRes videoChannelRes) {

    }
}
