package lifeng.gankqzone.network.callback;

/**
 * Created by Administrator on 2017/5/3.
 */

public interface IView<T> {

    void rxOnNext(T t);

    void rxOnStart();

    void rxOnCompleted();

    void rxOnError(Throwable e);
}
