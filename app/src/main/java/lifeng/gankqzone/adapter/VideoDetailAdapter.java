package lifeng.gankqzone.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import cn.jzvd.JZUserAction;
import cn.jzvd.JZVideoPlayerStandard;
import lifeng.gankqzone.R;
import lifeng.gankqzone.activity.AgentWebActivity;
import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.util.glide.GlideUtils;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description
 */
public class VideoDetailAdapter extends BaseMultiItemQuickAdapter<VideoDetailRes.ItemBean, BaseViewHolder> {
    private Context mContext;

    public VideoDetailAdapter(List<VideoDetailRes.ItemBean> data, WeakReference<Context> context) {
        super(data);
        this.mContext = context.get();
        addItemType(VideoDetailRes.ItemBean.SIFT_SHORT_TYPE, R.layout.item_video_short);
        addItemType(VideoDetailRes.ItemBean.SIFT_Long_TYPE, R.layout.item_video_long);
        addItemType(VideoDetailRes.ItemBean.NORMAL_TYPE, R.layout.item_video_normal);
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, final VideoDetailRes.ItemBean itemBean) {
        JZVideoPlayerStandard videoPlayerStandard = viewHolder.getView(R.id.videoplayer);
        GlideUtils.LoadImage(mContext, itemBean.getImage(), videoPlayerStandard.thumbImageView);
        if(itemBean.getDuration() != 0) {
            viewHolder.setVisible(R.id.tv_videoduration, true);
        }

        switch (viewHolder.getItemViewType()) {
            case VideoDetailRes.ItemBean.SIFT_SHORT_TYPE:
                viewHolder.setText(R.id.tv_title, itemBean.getTitle());
                /*** 设置播放时长 ***/
                viewHolder.setText(R.id.tv_videoduration, conversionTime(itemBean.getDuration()));
                videoPlayerStandard.setUp(itemBean.getVideo_url()
                        , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,"");

                if (!TextUtils.isEmpty(itemBean.getPlayTime())) {
                    //播放次数
                    viewHolder.setText(R.id.tv_info, conversionPlayTime(Integer.valueOf(itemBean.getPlayTime())));
                }
                LinearLayout llContent = (LinearLayout) viewHolder.getView(R.id.ll_content);
                llContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgentWebActivity.startIntent(mContext,itemBean.getTitle(),itemBean.getCommentsUrl());
                    }
                });
                break;
            case VideoDetailRes.ItemBean.SIFT_Long_TYPE:
                viewHolder.setText(R.id.tv_title, itemBean.getTitle());
                videoPlayerStandard.setUp(itemBean.getVideo_url()
                        , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);

                viewHolder.setText(R.id.tv_videoduration, conversionTime(itemBean.getDuration()));
                break;
            case VideoDetailRes.ItemBean.NORMAL_TYPE:
                videoPlayerStandard.setUp(itemBean.getVideo_url()
                        , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, itemBean.getTitle());
                viewHolder.setText(R.id.tv_videoduration, conversionTime(itemBean.getDuration()));

                if (!TextUtils.isEmpty(itemBean.getPlayTime())) {
                    viewHolder.setText(R.id.tv_info, conversionPlayTime(Integer.valueOf(itemBean.getPlayTime())));
                }
                break;
        }

        videoPlayerStandard.setJzUserAction(new JZUserAction() {
            @Override
            public void onEvent(int type, String s, int i1, Object... objects) {
                switch (type) {
                    case JZUserAction.ON_CLICK_START_ICON:
                        //视频标题设置为Gone
                        viewHolder.setVisible(R.id.tv_videoduration, false);
                        notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private String conversionPlayTime(int playtime) {
        if (sizeOf(playtime) > 4) {
            return accuracy(playtime, 10000, 1) + "万次播放";
        } else {
            return String.valueOf(playtime) + "次播放";
        }
    }

    public static String accuracy(double num, double total, int digit) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(digit);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total;
        return df.format(accuracy_num);
    }

    private String conversionTime(int duration) {
        int minutes = duration / 60;
        int seconds = duration - minutes * 60;
        String m = sizeOf(minutes) > 1 ? String.valueOf(minutes) : "0" + minutes;
        String s = sizeOf(seconds) > 1 ? String.valueOf(seconds) : "0" + seconds;
        return m + ":" + s;
    }

    /**
     * 判断是几位数字
     *
     * @param size
     * @return
     */
    private int sizeOf(int size) {
        final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
                99999999, 999999999, Integer.MAX_VALUE};
        for (int i = 0; ; i++)
            if (size <= sizeTable[i])
                return i + 1;
    }
}
