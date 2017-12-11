package lifeng.gankqzone.network.service;

import java.util.List;

import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.bean.fengh.VideoChannelRes;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lifeng on 2017/11/15.
 *
 * @description
 */
public interface FengHApiService {

    /*** 
     * 此接口在两处使用了:
     *   1:发现-休息视频 作为封面使用
     *   2.侧滑菜单视频***/
    @GET("ifengvideoList")
    Observable<List<VideoDetailRes>> getVideoDetail(@Query("page") int page,
                                                    @Query("listtype") String listtype,
                                                    @Query("typeid") String typeid);
    
    /***
     * 获取视频频道列表
     * ***/
    @GET("ifengvideoList")
    Observable<List<VideoChannelRes>> getVideoChannel(@Query("page") int page);

}
