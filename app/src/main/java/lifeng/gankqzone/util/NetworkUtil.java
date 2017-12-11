package lifeng.gankqzone.util;

import lifeng.gankqzone.config.NetWorkState;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description
 */
public class NetworkUtil {
    private NetworkUtil instance;
    private NetWorkState mNetWorkState;

    private static class LazyHolder {
        private static final NetworkUtil INSTANCE = new NetworkUtil();
    }

    private NetworkUtil() {
    }

    public static final NetworkUtil getInstance() {
        return LazyHolder.INSTANCE;
    }

    public NetWorkState getNetWorkState() {
        return mNetWorkState;
    }

    public void setNetWorkState(NetWorkState netWorkState) {
        mNetWorkState = netWorkState;
    }
}
