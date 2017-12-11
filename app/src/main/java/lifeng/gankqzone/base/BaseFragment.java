package lifeng.gankqzone.base;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import lifeng.gankqzone.MyApplication;
import lifeng.gankqzone.config.NetWorkState;
import lifeng.gankqzone.listener.NetConnectionObserver;
import lifeng.gankqzone.util.DialogUtil;
import lifeng.gankqzone.util.DynamicTimeFormat;
import lifeng.gankqzone.util.T;
import retrofit2.HttpException;

/**
 * Created by lifeng on 2017/10/24.
 *
 * @description
 */
public abstract class BaseFragment extends Fragment implements NetConnectionObserver {
    protected boolean isShowLoadingDialog = true;
    private Dialog mLoadingDialog;
    private NetWorkState mCurNetWorkState;
    private AlertDialog mNetworkExceptionDialog;

    /*** 是否对用户可见 ***/
    protected boolean mIsVisible;

    /*** 是否加载完成 当执行完onViewCreated方法后即为true ***/
    protected boolean mIsPrepare;

    private boolean mIsExecute = false;

    /*** 是否懒加载 ***/
    protected boolean isLazyLoad() {
        return true;
    }

    /*** 用户可见时执行 ***/
    protected void onVisible() {
        onLazyLoad();
    }

    /*** 用户不可见时执行 ***/
    protected void onInvisible() {
    }

    /*** 懒加载执行 ***/
    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            initFragmentData();
        }
    }

    /*** 初始化数据 ***/
    protected void initFragmentData() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyApplication.getInstance().addNetObserver(this);
        initPresenter();

        if (isLazyLoad()) {
            mIsPrepare = true;
            onLazyLoad();
        }
    }

    @Override
    public void updateNetStatus(NetWorkState netWorkState) {
        if (mCurNetWorkState != netWorkState) {
            mCurNetWorkState = netWorkState;
        }
    }

    public NetWorkState getCurNetWorkState() {
        return mCurNetWorkState;
    }

    public void rxOnStart() {
        if (mCurNetWorkState != NetWorkState.NO_NETWORK) {
            if (isShowLoadingDialog) {
                showLoadingDialog();
            }
        } else {
            showNetExceptionTip();
        }
    }

    public void rxOnCompleted() {
        if (isShowLoadingDialog) {
            DialogUtil.closeDialog(mLoadingDialog);
        }
    }

    public void rxOnError(Throwable e) {
        String msg = null;
        if (e instanceof UnknownHostException) {
            showNetExceptionTip();
        } else if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时";
        } else if (e instanceof ConnectException) {
            msg = "网络连接异常";
        } else if (e instanceof HttpException) {
            againReq();
        } else {
            againReq();
        }
        T.showShort(msg);
        if (isShowLoadingDialog) {
            DialogUtil.closeDialog(mLoadingDialog);
        }
    }

    /**
     * 再次请求网络
     */
    protected void againReq() {

    }

    private void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtil.createLoadingDialog(getContext());
        }
        mLoadingDialog.show();
    }

    /**
     * 显示网络异常提示
     */
    private void showNetExceptionTip() {
        if (mNetworkExceptionDialog == null) {
            mNetworkExceptionDialog = DialogUtil.networkExceptionDialog(getContext());
        }
        if (!mNetworkExceptionDialog.isShowing()) {
            mNetworkExceptionDialog.show();
        }
    }

    /**
     * 关闭网络异常提示框
     */
    private void closeNetExceptionTip() {
        if (mNetworkExceptionDialog != null) {
            if (mNetworkExceptionDialog.isShowing()) {
                mNetworkExceptionDialog.dismiss();
                mNetworkExceptionDialog = null;
            }
        }
    }

    /**
     * 下拉刷新控件初始化
     *
     * @param refreshLayout
     */
    protected void initSmartRefreshLayout(SmartRefreshLayout refreshLayout) {
        refreshLayout.setEnableLoadmoreWhenContentNotFull(false);
        int deta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        ClassicsHeader mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis() - deta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

        Drawable mDrawableProgress = mClassicsHeader.getProgressView().getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }

        mClassicsHeader.setSpinnerStyle(SpinnerStyle.Scale);
        refreshLayout.setPrimaryColors(0xff444444, 0xffffffff);
        if (Build.VERSION.SDK_INT >= 21) {
            mDrawableProgress.setTint(0xffffffff);
        } else if (mDrawableProgress instanceof VectorDrawableCompat) {
            ((VectorDrawableCompat) mDrawableProgress).setTint(0xffffffff);
        }
    }

    protected void stopRefresh(SmartRefreshLayout refreshLayout) {
        if (refreshLayout == null) {
            return;
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }

        if (refreshLayout.isLoading()) {
            refreshLayout.finishLoadmore();
        }
    }

    protected abstract void initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeNetObserver(this);

        closeNetExceptionTip();
    }
}
