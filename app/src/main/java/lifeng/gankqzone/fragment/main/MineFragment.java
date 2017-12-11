package lifeng.gankqzone.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import lifeng.gankqzone.R;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.util.SizeConvertUtil;
import lifeng.gankqzone.util.T;

/**
 * Created by lifeng on 2017/10/24.
 *
 * @description
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_cache)
    TextView mTvCache;
    Unbinder unbinder;
    @BindView(R.id.btn_deleteCache)
    Button mBtnDeleteCache;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            WeakReference<Context> wfContent = new WeakReference<Context>(getContext());
            String cacheSize = SizeConvertUtil.getTotalCacheSize(wfContent);
            mTvCache.setText("当前缓存大小:" + cacheSize);
            if (SizeConvertUtil.getCacheSize(wfContent) > 0) {
                mBtnDeleteCache.setVisibility(View.VISIBLE);
            } else {
                mBtnDeleteCache.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({
            R.id.btn_deleteCache
    })
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_deleteCache:
                SizeConvertUtil.clearAllCache(new WeakReference<Context>(getContext()));
                try {
                    mTvCache.setText("当前缓存大小:" + SizeConvertUtil.getTotalCacheSize(new WeakReference<Context>(getContext())));
                    mBtnDeleteCache.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                T.showShort("清理完成");
                break;
        }
    }
}
