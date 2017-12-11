package lifeng.gankqzone.network.callback;

import lifeng.gankqzone.bean.OnedayRes;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description
 */
public interface ITodayView<T> extends IView<T> {

    void todayGank(OnedayRes onedayRes);
}
