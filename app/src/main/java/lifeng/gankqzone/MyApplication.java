package lifeng.gankqzone;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import lifeng.gankqzone.config.NetWorkState;
import lifeng.gankqzone.listener.NetConnectionObserver;
import lifeng.gankqzone.listener.NetConnectionSubject;
import lifeng.gankqzone.util.NetworkUtil;

/**
 * Created by lifeng on 2017/10/24.
 *
 * @description
 */
public class MyApplication extends Application implements NetConnectionSubject {

    private static MyApplication instance;
    private List<NetConnectionObserver> observers = new ArrayList<>();
    private NetWorkState currentNetType;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Utils.init(this);
        showDebugDBAddressLogToast();
        LeakCanary.install(this);
        BGASwipeBackManager.getInstance().init(this);
        currentNetType = NetworkUtil.getInstance().getNetWorkState();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static void showDebugDBAddressLogToast() {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
//                Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
                /*** Debug模式下查看数据库 ***/
                LogUtils.i("DebugDB:     " + value);
            } catch (Exception ignore) {

            }
        }
    }

    @Override
    public void addNetObserver(NetConnectionObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeNetObserver(NetConnectionObserver observer) {
        if (observers != null && observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyNetObserver(NetWorkState netWorkState) {
        /**
         * 避免多次发送相同的网络状态
         */
        if (currentNetType == netWorkState) {
            return;
        } else {
            currentNetType = netWorkState;
            if (observers != null && observers.size() > 0) {
                for (NetConnectionObserver observer : observers) {
                    observer.updateNetStatus(netWorkState);
                }
            }
        }
    }

    public static Context getContext() {
        return instance;
    }
}
