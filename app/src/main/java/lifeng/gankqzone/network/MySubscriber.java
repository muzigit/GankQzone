package lifeng.gankqzone.network;

import lifeng.gankqzone.network.callback.IView;
import rx.Subscriber;

/**
 * Created by spring on 2017/4/25.
 */

public class MySubscriber<T> extends Subscriber<T> {
    IView mView;

    public MySubscriber(IView view) {
        this.mView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mView != null) mView.rxOnStart();
    }

    @Override
    public void onCompleted() {
        if (mView != null) mView.rxOnCompleted();
    }

    @Override
    public void onError(Throwable e) {
        if (mView != null) mView.rxOnError(e);
    }

    @Override
    public void onNext(T t) {
        if (mView != null) mView.rxOnNext(t);
    }
}
