package lifeng.gankqzone.bean;

/**
 * Created by lifeng on 2017/11/14.
 *
 * @description
 */
public class AndroidGankBean {

    private String imageUrl;
    private OnedayRes.Gank mGank;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public OnedayRes.Gank getGank() {
        return mGank;
    }

    public void setGank(OnedayRes.Gank gank) {
        mGank = gank;
    }
}
