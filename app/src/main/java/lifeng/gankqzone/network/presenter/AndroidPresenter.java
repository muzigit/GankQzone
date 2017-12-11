package lifeng.gankqzone.network.presenter;

import java.util.concurrent.TimeUnit;

import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.jiandan.FreshNewsBean;
import lifeng.gankqzone.config.ApiConfig;
import lifeng.gankqzone.network.MySubscriber;
import lifeng.gankqzone.network.ServiceFactory;
import lifeng.gankqzone.network.callback.IAndroidView;
import lifeng.gankqzone.network.service.GankApi;
import lifeng.gankqzone.network.service.JanDanApiService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static lifeng.gankqzone.config.JianDanApi.TYPE_FRESH;

/**
 * Created by lifeng on 2017/11/13.
 *
 * @description
 */
public class AndroidPresenter extends IPresenter {

    public void getFreshNews(int page){
        mSubscription.add(
                ServiceFactory.getJianDanRetrofit()
                        .create(JanDanApiService.class)
                        .getFreshNews(ApiConfig.JIANDAN_API, TYPE_FRESH,
                                "url,date,tags,author,title,excerpt,comment_count,comment_status,custom_fields",
                                page, "thumb_c,views", "1")

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<FreshNewsBean>(mView){
                            @Override
                            public void onNext(FreshNewsBean freshNewsBean) {
                                if(mView instanceof IAndroidView) {
                                    IAndroidView iAndroidView = (IAndroidView) mView;
                                    iAndroidView.getFreshNewsList(freshNewsBean);
                                }
                            }
                        }));
    }

    public void getAndroidListData(String type,int page){
        mSubscription.add(
                ServiceFactory.getRetrofit()
                        .create(GankApi.class)
                        .getAndroidGank(type,page)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<AndroidListRes>(mView){
                            @Override
                            public void onNext(AndroidListRes androidListRes) {
                                if(mView instanceof IAndroidView) {
                                    IAndroidView iAndroidView = (IAndroidView) mView;
                                    iAndroidView.getAndroidList(androidListRes);
                                }
                            }
                        }));
    }
}
