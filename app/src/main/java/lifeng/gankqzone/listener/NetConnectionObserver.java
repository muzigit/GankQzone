package lifeng.gankqzone.listener;

import lifeng.gankqzone.config.NetWorkState;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description 网络状态监听
 */
public interface NetConnectionObserver {

    void updateNetStatus(NetWorkState netWorkState);
}
