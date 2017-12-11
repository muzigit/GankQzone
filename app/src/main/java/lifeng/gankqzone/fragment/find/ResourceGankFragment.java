package lifeng.gankqzone.fragment.find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.R;
import lifeng.gankqzone.activity.AgentWebActivity;
import lifeng.gankqzone.adapter.ResourceGankAdapter;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.bean.AndroidGankBean;
import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.OnedayRes;
import lifeng.gankqzone.bean.jiandan.FreshNewsBean;
import lifeng.gankqzone.config.GankApiConfig;
import lifeng.gankqzone.network.callback.IAndroidView;
import lifeng.gankqzone.network.presenter.AndroidPresenter;
import lifeng.gankqzone.view.RecyclerViewDecoration;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description 拓展资源
 */
public class ResourceGankFragment extends BaseFragment implements IAndroidView<AndroidListRes> {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private ResourceGankAdapter mResourceGankAdapter;
    private boolean isFirst = true;
    private AndroidPresenter mPresenter;
    private List<OnedayRes.Gank> mResorceResults;
    private Random mRandom = new Random();
    private List<AndroidGankBean> mListResResults = new ArrayList<>();

    @Override
    protected void initPresenter() {
        mPresenter = new AndroidPresenter();
        mPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_gank, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initData();
        setListener();
    }

    private void initView() {
        isShowLoadingDialog = false;

        initRecyclerView();
        initSmartRefreshLayout(mRefreshLayout);
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

    private void initData() {
        mResourceGankAdapter = new ResourceGankAdapter();
        mResourceGankAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mResourceGankAdapter);

        if (isFirst) {
            mRefreshLayout.autoRefresh();
        } else {
            if (mListResResults.size() > 0) {
                mResourceGankAdapter.replaceData(mListResResults);
            }
        }
    }

    private void setListener() {
        mRefreshLayout.setOnRefreshListener(mOnRefreshLoadmoreListener);
        mRefreshLayout.setOnLoadmoreListener(mOnRefreshLoadmoreListener);
        mResourceGankAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    private int page;

    public OnRefreshLoadmoreListener mOnRefreshLoadmoreListener = new OnRefreshLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            page++;
            mPresenter.getAndroidListData(GankApiConfig.RESOURCE_TYPE, page);
        }

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            if (isFirst) {
                page = 1;
                isFirst = false;
                mPresenter.getAndroidListData(GankApiConfig.RESOURCE_TYPE, page);
            } else {
                stopRefresh();
            }
        }
    };

    private BaseQuickAdapter.OnItemClickListener mOnItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            AndroidGankBean androidGankBean = mListResResults.get(position);
            String desc = androidGankBean.getGank().getDesc();
            String url = androidGankBean.getGank().getUrl();
            AgentWebActivity.startIntent(getContext(), desc, url);
        }
    };

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
    public void rxOnError(Throwable e) {
        super.rxOnError(e);
        page--;
        stopRefresh();
    }

    @Override
    public void getAndroidList(AndroidListRes androidListRes) {
        mResorceResults = androidListRes.getResults();
        if (mResorceResults != null && mResorceResults.size() > 0) {
            reqFreshImage(2621);
        } else {
            stopRefresh();
        }
    }

    @Override
    public void getFreshNewsList(FreshNewsBean freshNewsBean) {
        if (freshNewsBean.getStatus().equals("ok")) {
            List<FreshNewsBean.PostsBean> posts = freshNewsBean.getPosts();
            if (posts != null && posts.size() > 0) {
                AndroidGankBean androidGankBean;
                for (int i = 0; i < mResorceResults.size(); i++) {
                    androidGankBean = new AndroidGankBean();
                    androidGankBean.setGank(mResorceResults.get(i));
                    String imageUrl = posts.get(i).getCustom_fields().getThumb_c().get(0);
                    androidGankBean.setImageUrl(imageUrl);
                    mListResResults.add(androidGankBean);
                }
                mResourceGankAdapter.replaceData(mListResResults);
            } else {
                reqFreshImage(100);
            }
        } else {
            mResourceGankAdapter.replaceData(mListResResults);
        }
        stopRefresh();
    }

    /**
     * 从煎蛋网随机页中取10张图片
     * @param count
     */
    private void reqFreshImage(int count){
        int i = mRandom.nextInt(count);
        mPresenter.getFreshNews(i);
    }

    @Override
    public void rxOnNext(AndroidListRes androidListRes) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
    }
}
