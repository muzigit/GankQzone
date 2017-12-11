package lifeng.gankqzone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import lifeng.gankqzone.MyApplication;
import lifeng.gankqzone.config.NetWorkState;
import lifeng.gankqzone.util.NetworkUtil;

import static lifeng.gankqzone.config.NetWorkState.MOBILE_NETWORK;
import static lifeng.gankqzone.config.NetWorkState.NO_NETWORK;
import static lifeng.gankqzone.config.NetWorkState.WIFI_NETWORK;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description 监听网络变化
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private NetWorkState networkState;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
            //说明网络是连接的
            int type = activeNetworkInfo.getType();
            switch (type) {
                case ConnectivityManager.TYPE_MOBILE:
                    networkState = MOBILE_NETWORK;
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    networkState = WIFI_NETWORK;
                    break;
            }
        } else {
            networkState = NO_NETWORK;
        }
        if (NetworkUtil.getInstance().getNetWorkState() == null) {
            //第一次进来
            NetworkUtil.getInstance().setNetWorkState(networkState);
            MyApplication.getInstance().notifyNetObserver(networkState);
        } else {
            //防止重复发送相同状态
            if (networkState != NetworkUtil.getInstance().getNetWorkState()) {
                NetworkUtil.getInstance().setNetWorkState(networkState);
                MyApplication.getInstance().notifyNetObserver(networkState);
            }
        }
    }
}
