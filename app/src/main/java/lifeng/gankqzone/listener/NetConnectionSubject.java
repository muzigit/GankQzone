package lifeng.gankqzone.listener;

import lifeng.gankqzone.config.NetWorkState;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description
 */
public interface NetConnectionSubject {

    /**
     * 注册观察者
     *
     * @param observer
     */
    void addNetObserver(NetConnectionObserver observer);

    /**
     * 移除观察者
     */
    void removeNetObserver(NetConnectionObserver observer);

    /**
     * 状态更新通知
     */
    void notifyNetObserver(NetWorkState netWorkState);
}
