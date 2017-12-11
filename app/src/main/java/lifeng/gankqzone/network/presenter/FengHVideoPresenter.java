package lifeng.gankqzone.network.presenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.bean.fengh.VideoChannelRes;
import lifeng.gankqzone.network.MySubscriber;
import lifeng.gankqzone.network.ServiceFactory;
import lifeng.gankqzone.network.callback.IFengHVideoView;
import lifeng.gankqzone.network.service.FengHApiService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description
 */
public class FengHVideoPresenter extends IPresenter {

    /*** 视频频道 ***/
    public void getVideoChannelList(int page) {
        mSubscription.add(
                ServiceFactory.getFengHuangVideoRetrofit()
                        .create(FengHApiService.class)
                        .getVideoChannel(page)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<List<VideoChannelRes>>(mView) {
                            @Override
                            public void onNext(List<VideoChannelRes> videoChannelRes) {
                                if (mView instanceof IFengHVideoView) {
                                    IFengHVideoView iVideoView = (IFengHVideoView) mView;
                                    iVideoView.getVideoChannelList(videoChannelRes);
                                }
                            }
                        })
        );
    }

    /*** 视频列表 ***/
    public void getVidelDetailList(int page, String typeId) {
        mSubscription.add(
                ServiceFactory.getFengHuangVideoRetrofit()
                        .create(FengHApiService.class)
                        .getVideoDetail(page, "list", typeId)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<List<VideoDetailRes>>(mView) {
                            @Override
                            public void onNext(List<VideoDetailRes> videoDetailRes) {
                                if (mView instanceof IFengHVideoView) {
                                    IFengHVideoView iVideoView = (IFengHVideoView) mView;
                                    iVideoView.getVidelDetailList(videoDetailRes);
                                }
                            }
                        })
        );
    }
}
