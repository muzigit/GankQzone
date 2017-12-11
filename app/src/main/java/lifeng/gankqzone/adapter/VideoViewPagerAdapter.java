package lifeng.gankqzone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.bean.fengh.VideoChannelRes;
import lifeng.gankqzone.fragment.FengHVideoDetailFragment;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description
 */
public class VideoViewPagerAdapter extends FragmentStatePagerAdapter {

    private final VideoChannelRes mVideoChannelRes;

    public VideoViewPagerAdapter(FragmentManager fm, VideoChannelRes videoChannelRes) {
        super(fm);
        this.mVideoChannelRes = videoChannelRes;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            List<VideoDetailRes.ItemBean> itemBean = mVideoChannelRes.getItem();
            FengHVideoDetailFragment.newInstance(position, itemBean);
        }
        return FengHVideoDetailFragment.newInstance(position,
                "clientvideo_" + mVideoChannelRes.getTypes().get(position).getId());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mVideoChannelRes.getTypes().get(position).getName();
    }

    @Override
    public int getCount() {
        return mVideoChannelRes != null ? mVideoChannelRes.getTypes().size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
