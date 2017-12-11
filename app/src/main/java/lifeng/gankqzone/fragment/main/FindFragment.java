package lifeng.gankqzone.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.R;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.fragment.find.AndroidGankFragment;
import lifeng.gankqzone.fragment.find.ResourceGankFragment;
import lifeng.gankqzone.fragment.find.TodayGankFragment;
import lifeng.gankqzone.fragment.find.VideoGankFragment;

/**
 * Created by lifeng on 2017/10/24.
 *
 * @description
 */
public class FindFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private final String[] mTitles = {
            "最新干货", "Android", "休息视频", "拓展资源"
    };

    private List<Fragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
    }

    @Override
    protected void initPresenter() {

    }

    private void initViewPager() {
        TodayGankFragment todayGankFragment = new TodayGankFragment();
        AndroidGankFragment androidGankFragment = new AndroidGankFragment();
        VideoGankFragment videoGankFragment = new VideoGankFragment();
        ResourceGankFragment resourceGankFragment = new ResourceGankFragment();

        mFragments.add(todayGankFragment);
        mFragments.add(androidGankFragment);
        mFragments.add(videoGankFragment);
        mFragments.add(resourceGankFragment);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        /*** 设置默认选中位置 ***/
        mViewPager.setCurrentItem(0);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
