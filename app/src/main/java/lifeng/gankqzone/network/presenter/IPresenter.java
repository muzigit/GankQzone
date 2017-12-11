package lifeng.gankqzone.network.presenter;

import lifeng.gankqzone.network.callback.IView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/4/25.
 */

public abstract class IPresenter {
    IView mView;
    CompositeSubscription mSubscription = new CompositeSubscription();

    public void onAttach(IView view) {
        mView = view;
    }

    public void onDetach() {
        if (mSubscription.hasSubscriptions()) {
            mSubscription.unsubscribe();
        }
        if (this.mView != null) {
            this.mView = null;
        }
    }
}
