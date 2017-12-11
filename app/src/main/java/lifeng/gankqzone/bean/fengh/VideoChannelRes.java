package lifeng.gankqzone.bean.fengh;

import java.util.List;

import lifeng.gankqzone.bean.VideoDetailRes;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description
 */
public class VideoChannelRes {

    private int currentPage;
    private int totalPage;
    private String type;
    private List<VideoDetailRes.ItemBean> item;
    private List<TypesBean> types;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VideoDetailRes.ItemBean> getItem() {
        return item;
    }

    public void setItem(List<VideoDetailRes.ItemBean> item) {
        this.item = item;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }
}
