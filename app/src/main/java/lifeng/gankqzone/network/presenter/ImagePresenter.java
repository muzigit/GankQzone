package lifeng.gankqzone.network.presenter;

import java.util.concurrent.TimeUnit;

import lifeng.gankqzone.bean.ImageRes;
import lifeng.gankqzone.network.MySubscriber;
import lifeng.gankqzone.network.ServiceFactory;
import lifeng.gankqzone.network.callback.IImaeView;
import lifeng.gankqzone.network.service.GankApi;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lifeng on 2017/10/25.
 *
 * @description
 */
public class ImagePresenter extends IPresenter{

    //获取图片
    public void getImage(int limit,int page) {
        mSubscription.add(
                ServiceFactory.getRetrofit()
                        .create(GankApi.class)
                        .getImage(limit,page)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<ImageRes>(mView)));
    }

    public void getImageRandom(int count){
        mSubscription.add(
                ServiceFactory.getRetrofit()
                        .create(GankApi.class)
                        .getImageRandom(count)

                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new MySubscriber<ImageRes>(mView){
                            @Override
                            public void onNext(ImageRes imageRes) {
                                if(mView instanceof IImaeView) {
                                    IImaeView imageView = (IImaeView) mView;
                                    imageView.getImageRandom(imageRes);
                                }
                            }
                        }));
    }
}
