package lifeng.gankqzone.network.service;

import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.ImageRes;
import lifeng.gankqzone.bean.OnedayRes;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lifeng on 2017/10/23.
 *
 * @description 干货集中营Api
 */
public interface GankApi {
    //获取分页图片 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
    @GET("/api/data/福利/{limit}/{page}")
    Observable<ImageRes> getImage(@Path("limit") int limit, @Path("page") int page);

    //获取随机图片
    //http://gank.io/api/random/data/福利/20
    @GET("/api/random/data/福利/{count}")
    Observable<ImageRes> getImageRandom(@Path("count") int count);

    //根据日期获取数据
//    http://gank.io/api/day/2017/10/27
    @GET("/api/day/{year}/{month}/{day}")
    Observable<OnedayRes> getDateGank(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    //获取Android内容
    @GET("/api/data/{type}/10/{page}")
    Observable<AndroidListRes> getAndroidGank(@Path("type") String type, @Path("page") int page);

    //获取休息视频
    @GET("/api/data/休息视频/10/{page}")
    Observable<AndroidListRes> getVideoGank(@Path("page") int page);
}
