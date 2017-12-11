package lifeng.gankqzone.network.presenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.network.MySubscriber;
import lifeng.gankqzone.network.ServiceFactory;
import lifeng.gankqzone.network.callback.IVideoView;
import lifeng.gankqzone.network.service.GankApi;
import lifeng.gankqzone.network.service.FengHApiService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lifeng on 2017/11/15.
 *
 * @description
 */
public class VideoPresenter extends IPresenter {

    public void getVideoList(int page) {
        mSubscription.add(
                ServiceFactory.getRetrofit()
                        .create(GankApi.class)
                        .getVideoGank(page)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<AndroidListRes>(mView) {
                            @Override
                            public void onNext(AndroidListRes androidListRes) {
                                if(mView instanceof IVideoView) {
                                    IVideoView iVideoView = (IVideoView) mView;
                                    iVideoView.getGankVideo(androidListRes);
                                }
                            }
                        })
        );
    }

    /**
     * 获取视频封面图
     */
    public void getVideoImageList(int page, String typeid) {
        mSubscription.add(
                ServiceFactory.getFengHuangVideoRetrofit()
                        .create(FengHApiService.class)
                        .getVideoDetail(page, "list", typeid)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<List<VideoDetailRes>>(mView){
                            @Override
                            public void onNext(List<VideoDetailRes> videoDetailRes) {
                                if(mView instanceof IVideoView) {
                                    IVideoView iVideoView = (IVideoView) mView;
                                    iVideoView.getVideoImage(videoDetailRes);
                                }
                            }
                        })
        );

    }
}
