package lifeng.gankqzone.network.service;

import lifeng.gankqzone.bean.jiandan.FreshNewsArticleBean;
import lifeng.gankqzone.bean.jiandan.FreshNewsBean;
import lifeng.gankqzone.bean.jiandan.JdDetailBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 煎蛋网
 */
public interface JanDanApiService {

    @GET
    Observable<FreshNewsBean> getFreshNews(@Url String url, @Query("oxwlxojflwblxbsapi") String oxwlxojflwblxbsapi,
                                           @Query("include") String include,
                                           @Query("page") int page,
                                           @Query("custom_fields") String custom_fields,
                                           @Query("dev") String dev
    );

    @GET
    Observable<JdDetailBean> getDetailData(@Url String url, @Query("oxwlxojflwblxbsapi") String oxwlxojflwblxbsapi,
                                           @Query("page") int page
    );

    @GET
    Observable<FreshNewsArticleBean> getFreshNewsArticle(@Url String url, @Query("oxwlxojflwblxbsapi") String oxwlxojflwblxbsapi,
                                                         @Query("include") String include,
                                                         @Query("id") int id
    );
}
