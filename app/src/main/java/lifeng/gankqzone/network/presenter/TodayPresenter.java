package lifeng.gankqzone.network.presenter;

import java.util.concurrent.TimeUnit;

import lifeng.gankqzone.bean.OnedayRes;
import lifeng.gankqzone.network.MySubscriber;
import lifeng.gankqzone.network.ServiceFactory;
import lifeng.gankqzone.network.callback.ITodayView;
import lifeng.gankqzone.network.service.GankApi;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description
 */
public class TodayPresenter extends IPresenter {
    /*** 获取某一日的数据 ***/
    public void getTodayData(int year,int month,int day) {
        mSubscription.add(
                ServiceFactory.getRetrofit()
                        .create(GankApi.class)
                        .getDateGank(year,month,day)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<OnedayRes>(mView){
                            @Override
                            public void onNext(OnedayRes onedayRes) {
                                if(mView instanceof ITodayView) {
                                    ITodayView todayView = (ITodayView) mView;
                                    todayView.todayGank(onedayRes);
                                }
                            }
                        }));
    }
}
