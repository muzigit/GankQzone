package lifeng.gankqzone.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ScreenUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import lifeng.gankqzone.MyApplication;
import lifeng.gankqzone.R;
import lifeng.gankqzone.config.NetWorkState;
import lifeng.gankqzone.listener.NetConnectionObserver;
import lifeng.gankqzone.receiver.NetworkStateReceiver;
import lifeng.gankqzone.util.T;
import retrofit2.HttpException;

/**
 * Created by lifeng on 2017/10/25.
 *
 * @description
 */
public class BaseActivity extends AppCompatActivity implements NetConnectionObserver, BGASwipeBackHelper.Delegate {

    private NetworkStateReceiver mNetworkStateReceiver;
    private NetWorkState mCurNetWorkState;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
//        AppManager.getAppManager().addActivity(this);

        registerBroadcastReceiver();
        MyApplication.getInstance().addNetObserver(this);
    }

    /**
     * 获取当前网络状态
     * @return
     */
    protected NetWorkState getCurNetWorkState() {
        return mCurNetWorkState;
    }

    /**
     * 注册广播
     */
    private void registerBroadcastReceiver() {
        mNetworkStateReceiver = new NetworkStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, intentFilter);
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    public void rxOnStart() {
        if(mCurNetWorkState != NetWorkState.NO_NETWORK) {
//            if (isShowLoadingDialog) {
//                showLoadingDialog();
//            }
        } else {
//            showNetExceptionTip();
        }
    }

    public void rxOnCompleted() {
//        if (isShowLoadingDialog) {
//            DialogUtil.closeDialog(mLoadingDialog);
//        }
    }

    public void rxOnError(Throwable e) {
        String msg = null;
        if (e instanceof UnknownHostException) {
//            showNetExceptionTip();
        } else if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时";
        } else if (e instanceof ConnectException) {
            msg = "网络连接异常";
        } else if (e instanceof HttpException) {
//            againReq();
        } else {
//            againReq();
        }
        T.showShort(msg);
//        if (isShowLoadingDialog) {
//            DialogUtil.closeDialog(mLoadingDialog);
//        }
    }

    /**
     * 是否支持滑动返回 默认不支持
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*** 设置为竖屏 ***/
        ScreenUtils.setPortrait(this);
    }

    @Override
    public void updateNetStatus(NetWorkState netWorkState) {
        if (mCurNetWorkState != netWorkState) {
            mCurNetWorkState = netWorkState;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkStateReceiver != null) {
            unregisterReceiver(mNetworkStateReceiver);
        }
        MyApplication.getInstance().removeNetObserver(this);
//        AppManager.getAppManager().finishActivity(this);
//        Activity topActivity = ActivityUtils.getTopActivity();
//        if(topActivity != null) {
//            topActivity.finish();
//            topActivity= null;
//        }
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }
}
