package lifeng.gankqzone.config;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lifeng.gankqzone.bean.jiandan.FreshNewsArticleBean;
import lifeng.gankqzone.bean.jiandan.FreshNewsBean;
import lifeng.gankqzone.bean.jiandan.JdDetailBean;
import lifeng.gankqzone.network.service.JanDanApiService;
import rx.Observable;


public class JianDanApi {

    public static final String TYPE_FRESH = "get_recent_posts";
    public static final String TYPE_FRESHARTICLE = "get_post";
    public static final String TYPE_BORED = "jandan.get_pic_comments";
    public static final String TYPE_GIRLS = "jandan.get_ooxx_comments";
    public static final String TYPE_Duan = "jandan.get_duan_comments";

    @StringDef({TYPE_FRESH, TYPE_BORED, TYPE_GIRLS, TYPE_Duan})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    public static JianDanApi sInstance;

    private JanDanApiService mService;

    public JianDanApi(JanDanApiService janDanApiService) {
        this.mService = janDanApiService;
    }

    public static JianDanApi getInstance(JanDanApiService janDanApiService) {
        if (sInstance == null)
            sInstance = new JianDanApi(janDanApiService);
        return sInstance;
    }

    /**
     * 获取新鲜事列表
     *
     * @param page 页码
     * @return
     */
    public Observable<FreshNewsBean> getFreshNews(int page) {
        return mService.getFreshNews(ApiConfig.JIANDAN_API, TYPE_FRESH,
                "url,date,tags,author,title,excerpt,comment_count,comment_status,custom_fields",
                page, "thumb_c,views", "1");
    }

    /**
     * 获取 无聊图，妹子图，段子列表
     *
     * @param type {@link Type}
     * @param page 页码
     * @return
     */
    public Observable<JdDetailBean> getJdDetails(@Type String type, int page) {
        return mService.getDetailData(ApiConfig.JIANDAN_API, type, page);
    }

    /**
     * 获取新鲜事文章详情
     *
     * @param id PostsBean id {@link FreshNewsBean.PostsBean}
     * @return
     */
    public Observable<FreshNewsArticleBean> getFreshNewsArticle(int id) {
        return mService.getFreshNewsArticle(ApiConfig.JIANDAN_API, TYPE_FRESHARTICLE, "content,date,modified", id);
    }

}
