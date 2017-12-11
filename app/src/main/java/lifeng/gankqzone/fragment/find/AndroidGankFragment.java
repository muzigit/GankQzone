package lifeng.gankqzone.fragment.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.R;
import lifeng.gankqzone.activity.WebViewActivity;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.bean.AndroidGankBean;
import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.OnedayRes;
import lifeng.gankqzone.bean.jiandan.FreshNewsBean;
import lifeng.gankqzone.config.GankApiConfig;
import lifeng.gankqzone.network.callback.IAndroidView;
import lifeng.gankqzone.network.presenter.AndroidPresenter;
import lifeng.gankqzone.util.DateFormatUtil;
import lifeng.gankqzone.util.glide.GlideUtils;
import lifeng.gankqzone.view.RecyclerViewDecoration;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description
 */
public class AndroidGankFragment extends BaseFragment implements IAndroidView<AndroidListRes> {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private AndroidPresenter mAndroidPresenter;
    private QuickAdapter mAdapter;
    private ArrayList<AndroidGankBean> mAndroidGankBeanList = new ArrayList<>();
    private boolean isFirst = true;

    private int page = 1;
    private List<OnedayRes.Gank> mAndroidListResResults;

    @Override
    protected void initPresenter() {
        mAndroidPresenter = new AndroidPresenter();
        mAndroidPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_android_gank, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        isShowLoadingDialog = false;

        initRecyclerView();
        initSmartRefreshLayout(mRefreshLayout);
    }

    private void initData() {
        mAdapter = new QuickAdapter();
        /*** 设置Item加载动画 ***/
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mAdapter);

        if (isFirst) {
            mRefreshLayout.autoRefresh();
        } else {
            if (mAndroidGankBeanList.size() > 0) {
                mAdapter.replaceData(mAndroidGankBeanList);
            }
        }
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (isFirst) {
                    page = 1;
                    isFirst = false;
                    mAndroidPresenter.getAndroidListData(GankApiConfig.ANDROID_TYPE,page);
                } else {
                    stopRefresh();
                }
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                mAndroidPresenter.getAndroidListData(GankApiConfig.ANDROID_TYPE,page);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AndroidGankBean androidGankBean = mAndroidGankBeanList.get(position);
                String desc = androidGankBean.getGank().getDesc();
                String url = androidGankBean.getGank().getUrl();
                WebViewActivity.startIntent(getContext(), desc, url);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewDecoration dividerLine = new RecyclerViewDecoration(RecyclerViewDecoration.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0xFFDDDDDD);
        dividerLine.setMarginLeft(10);
        mRecyclerView.addItemDecoration(dividerLine);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void rxOnError(Throwable e) {
        super.rxOnError(e);
        page--;
        stopRefresh();
    }

    @Override
    public void getAndroidList(AndroidListRes androidListRes) {
        mAndroidListResResults = androidListRes.getResults();
        if (mAndroidListResResults.size() > 0) {
            mAndroidPresenter.getFreshNews(page);
        } else {
            stopRefresh();
        }
    }

    @Override
    public void getFreshNewsList(FreshNewsBean freshNewsBean) {
        if (freshNewsBean.getStatus().equals("ok")) {
            List<FreshNewsBean.PostsBean> posts = freshNewsBean.getPosts();
            if (mAndroidListResResults != null && mAndroidListResResults.size() > 0) {
                AndroidGankBean androidGankBean;
                for (int i = 0; i < mAndroidListResResults.size(); i++) {
                    androidGankBean = new AndroidGankBean();
                    androidGankBean.setGank(mAndroidListResResults.get(i));
                    String imageUrl = posts.get(i).getCustom_fields().getThumb_c().get(0);
                    androidGankBean.setImageUrl(imageUrl);
                    mAndroidGankBeanList.add(androidGankBean);
                }
                mAdapter.replaceData(mAndroidGankBeanList);
            }
        }
        stopRefresh();
    }

    public class QuickAdapter extends BaseQuickAdapter<AndroidGankBean, BaseViewHolder> {

        public QuickAdapter() {
            super(R.layout.item_freshnews);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, AndroidGankBean item) {
            OnedayRes.Gank gank = item.getGank();
            viewHolder.setText(R.id.tv_title, gank.getDesc());
            viewHolder.setText(R.id.tv_title, gank.getDesc());

            //时间格式转换
            viewHolder.setText(R.id.tv_commnetsize, formatStr(gank.getPublishedAt()));
            if (gank.getWho() != null) {
                viewHolder.setText(R.id.tv_info, "来源:" + gank.getWho());
            } else {
                viewHolder.setText(R.id.tv_info, R.string.gank_name);
            }

            ImageView ivLogo = viewHolder.getView(R.id.iv_logo);
            GlideUtils.LoadImage(getContext(), item.getImageUrl(), ivLogo);
        }
    }

    private String formatStr(String date) {
        Date dateFromStr = DateFormatUtil.formatDateFromStr(date);
        return DateFormatUtil.getFormatDateStr(dateFromStr);
    }


    @Override
    public void rxOnNext(AndroidListRes androidListRes) {

    }

    private void stopRefresh() {
        if (mRefreshLayout == null) return;
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }

        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAndroidPresenter != null) {
            mAndroidPresenter.onDetach();
        }
        if (mAndroidGankBeanList.size() > 0) {
            mAndroidGankBeanList.clear();
            mAndroidGankBeanList = null;
        }
    }
}
