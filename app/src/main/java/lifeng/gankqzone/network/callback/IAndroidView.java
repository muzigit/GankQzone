package lifeng.gankqzone.network.callback;

import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.jiandan.FreshNewsBean;

/**
 * Created by lifeng on 2017/11/14.
 *
 * @description
 */
public interface IAndroidView<T> extends IView<T> {

    void getAndroidList(AndroidListRes androidListRes);

    void getFreshNewsList(FreshNewsBean freshNewsBean);
}
