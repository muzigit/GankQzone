package lifeng.gankqzone.network.callback;

import lifeng.gankqzone.bean.ImageRes;

/**
 * Created by lifeng on 2017/10/25.
 *
 * @description
 */
public interface IImaeView<T> extends IView<T> {

    void getImageRandom(ImageRes imageRes);
}
