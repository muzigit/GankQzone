package lifeng.gankqzone.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;

import lifeng.gankqzone.R;
import lifeng.gankqzone.bean.AndroidGankBean;
import lifeng.gankqzone.util.DateFormatUtil;
import lifeng.gankqzone.util.glide.GlideUtils;

/**
 * Created by lifeng on 2017/11/15.
 *
 * @description
 */
public class VideoGankAdapter extends BaseQuickAdapter<AndroidGankBean, BaseViewHolder> {

    public VideoGankAdapter() {
        super(R.layout.item_video_gank);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final AndroidGankBean gankBean) {
        //时间格式转换
        viewHolder.setText(R.id.tv_publicTime, formatStr(gankBean.getGank().getPublishedAt()));
        viewHolder.setText(R.id.tv_title, gankBean.getGank().getDesc());
        if (gankBean.getGank().getWho() != null) {
            viewHolder.setText(R.id.tv_info, "来源:" + gankBean.getGank().getWho());
        } else {
            viewHolder.setText(R.id.tv_info, R.string.gank_name);
        }

//        JZVideoPlayerStandard videoPlayer = viewHolder.getView(R.id.videoplayer);
//        GlideUtils.LoadImage(mContext, gankBean.getImageUrl(), videoPlayer.thumbImageView);
//        videoPlayer.setUp(gankBean.getGank().getUrl(),JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, gankBean.getGank().getDesc());

        ImageView videoPlayer = viewHolder.getView(R.id.videoplayer);
        if (gankBean.getImageUrl() != null) {
            GlideUtils.LoadImage(mContext, gankBean.getImageUrl(), videoPlayer);
        }

        viewHolder.addOnClickListener(R.id.videoplayer);
//        videoPlayer.setJzUserAction(new JZUserAction() {
//            @Override
//            public void onEvent(int type, String url, int screen, Object... objects) {
//                Log.i("VideoDetailAdapter", "onEvent: " + type);
//                switch (type) {
//                    case JZUserAction.ON_CLICK_START_ICON:
//                        Log.i("VideoDetailAdapter", "onEvent: " + "开始了");
//                        break;
//                }
//            }
//        });
    }

    private String formatStr(String date) {
        Date dateFromStr = DateFormatUtil.formatDateFromStr(date);
        return DateFormatUtil.getFormatDateStr(dateFromStr);
    }
}
