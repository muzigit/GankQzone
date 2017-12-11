package lifeng.gankqzone.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lifeng on 2017/10/25.
 *
 * @description  某一天的干货
 */
public class OnedayRes extends BaseRes {


    /**
     * category : ["休息视频","Android","前端","iOS","福利","拓展资源"]
     * error : false
     * results : {"Android":[{"_id":"59efe9fb421aa90fe72535c1","createdAt":"2017-10-25T09:33:47.784Z","desc":"堪比阿里插件的Android Studio插件集合(IDE通用)（上）","publishedAt":"2017-10-25T11:39:10.950Z","source":"web","type":"Android","url":"https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247487640&idx=1&sn=3752d389f908a6116341a03e6f3c6730","used":true,"who":"陈宇明"}],"iOS":[{"_id":"59f0010c421aa90fef2034cc","createdAt":"2017-10-25T11:12:12.416Z","desc":"MVVM + FLUX Reactive Facade ViewKit","publishedAt":"2017-10-25T11:39:10.950Z","source":"chrome","type":"iOS","url":"https://github.com/geekaurora/ReactiveListViewKit","used":true,"who":"daimajia"}]}
     */

    private ResultsBean results;
    private List<String> category;

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public static class ResultsBean {
        @SerializedName("Android")
        private List<Gank> androidList;
        @SerializedName("休息视频")
        private List<Gank> videoList;
        @SerializedName("iOS")
        private List<Gank> iOSList;
        @SerializedName("福利")
        private List<Gank> imageList;
        @SerializedName("拓展资源")
        private List<Gank> resourcesList;
        @SerializedName("瞎推荐")
        private List<Gank> recommendList;
        @SerializedName("App")
        private List<Gank> appList;

        public List<Gank> getAndroidList() {
            return androidList;
        }

        public void setAndroidList(List<Gank> androidList) {
            this.androidList = androidList;
        }

        public List<Gank> getVideoList() {
            return videoList;
        }

        public void setVideoList(List<Gank> videoList) {
            this.videoList = videoList;
        }

        public List<Gank> getiOSList() {
            return iOSList;
        }

        public void setiOSList(List<Gank> iOSList) {
            this.iOSList = iOSList;
        }

        public List<Gank> getImageList() {
            return imageList;
        }

        public void setImageList(List<Gank> imageList) {
            this.imageList = imageList;
        }

        public List<Gank> getResourcesList() {
            return resourcesList;
        }

        public void setResourcesList(List<Gank> resourcesList) {
            this.resourcesList = resourcesList;
        }

        public List<Gank> getRecommendList() {
            return recommendList;
        }

        public void setRecommendList(List<Gank> recommendList) {
            this.recommendList = recommendList;
        }

        public List<Gank> getAppList() {
            return appList;
        }

        public void setAppList(List<Gank> appList) {
            this.appList = appList;
        }
    }

    public static class Gank {

        /**
         * _id : 59e948c6421aa90fe2f02bd1
         * createdAt : 2017-10-20T08:52:22.906Z
         * desc : 图文并茂全面总结Android Studio好用的插件(IDE通用)
         * images : ["http://img.gank.io/3a2dfc7c-f579-409b-86c5-1e94ecddeba9"]
         * publishedAt : 2017-10-20T10:26:24.673Z
         * source : web
         * type : Android
         * url : http://www.jianshu.com/p/269a48d7508d
         * used : true
         * who : 阿韦
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
