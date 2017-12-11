package lifeng.gankqzone.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;

import lifeng.gankqzone.R;
import lifeng.gankqzone.bean.AndroidGankBean;
import lifeng.gankqzone.bean.OnedayRes;
import lifeng.gankqzone.util.DateFormatUtil;
import lifeng.gankqzone.util.glide.GlideUtils;

/**
 * Created by lifeng on 2017/11/16.
 *
 * @description
 */
public class ResourceGankAdapter extends BaseQuickAdapter<AndroidGankBean, BaseViewHolder> {

    public ResourceGankAdapter() {
        super(R.layout.item_freshnews);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, AndroidGankBean item) {
        OnedayRes.Gank gank = item.getGank();
        viewHolder.setText(R.id.tv_title, gank.getDesc());
        viewHolder.setText(R.id.tv_title, gank.getDesc());

        //时间格式转换
        viewHolder.setText(R.id.tv_commnetsize, formatStr(gank.getPublishedAt()));
        if (gank.getWho() != null) {
            viewHolder.setText(R.id.tv_info, "来源:" + gank.getWho());
        } else {
            viewHolder.setText(R.id.tv_info, R.string.gank_name);
        }

        ImageView ivLogo = viewHolder.getView(R.id.iv_logo);
        if(item.getImageUrl() != null) {
            GlideUtils.LoadImage(mContext, item.getImageUrl(), ivLogo);
        }
    }

    private String formatStr(String date) {
        Date dateFromStr = DateFormatUtil.formatDateFromStr(date);
        return DateFormatUtil.getFormatDateStr(dateFromStr);
    }

}
