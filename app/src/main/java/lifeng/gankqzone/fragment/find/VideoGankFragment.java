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
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.R;
import lifeng.gankqzone.activity.AgentWebActivity;
import lifeng.gankqzone.adapter.VideoGankAdapter;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.bean.AndroidGankBean;
import lifeng.gankqzone.bean.AndroidListRes;
import lifeng.gankqzone.bean.OnedayRes;
import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.config.FhVideoType;
import lifeng.gankqzone.network.callback.IVideoView;
import lifeng.gankqzone.network.presenter.VideoPresenter;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description
 */
public class VideoGankFragment extends BaseFragment implements IVideoView<AndroidListRes> {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private boolean isFirst = true;
    private VideoPresenter mVideoPresenter;
    private List<AndroidGankBean> mVideoLists = new ArrayList<>();
    private VideoGankAdapter mVideoGankAdapter;
    private int page;
    private List<OnedayRes.Gank> mVideoList;

    @Override
    protected void initPresenter() {
        mVideoPresenter = new VideoPresenter();
        mVideoPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_gank, container, false);
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
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initSmartRefreshLayout(mRefreshLayout);
    }

    private void initData() {
        mVideoGankAdapter = new VideoGankAdapter();
        /*** 设置Item加载动画 ***/
        mVideoGankAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mVideoGankAdapter);

        if (isFirst) {
            mRefreshLayout.autoRefresh();
        } else {
            if (mVideoLists.size() > 0) {
                mVideoGankAdapter.replaceData(mVideoLists);
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
                    mVideoPresenter.getVideoList(page);
                } else {
                    stopRefresh();
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                mVideoPresenter.getVideoList(page);
            }
        });

        mVideoGankAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.videoplayer) {
                    AndroidGankBean gankBean = mVideoLists.get(position);
                    AgentWebActivity.startIntent(getContext(), gankBean.getGank().getDesc(), gankBean.getGank().getUrl());
                }
            }
        });
    }

    @Override
    public void rxOnError(Throwable e) {
        super.rxOnError(e);
        page--;
        stopRefresh();
    }

    @Override
    public void getGankVideo(AndroidListRes videoList) {
        mVideoList = videoList.getResults();
        if (mVideoList != null && mVideoList.size() > 0) {
            mVideoPresenter.getVideoImageList(page, FhVideoType.PIECE);
        } else {
            stopRefresh();
        }
    }

    @Override
    public void getVideoImage(List<VideoDetailRes> videoDetailRes) {
        if (videoDetailRes != null && videoDetailRes.size() > 0) {
            VideoDetailRes detailRes = videoDetailRes.get(0);
            List<VideoDetailRes.ItemBean> beanList = detailRes.getItem();
            if (beanList != null && beanList.size() > 0) {
                for(int i = 0; i < mVideoList.size(); i++) {
                    AndroidGankBean videoGankBean = new AndroidGankBean();
                    OnedayRes.Gank gank = mVideoList.get(i);
                    videoGankBean.setGank(gank);
                    String thumbnail = beanList.get(i).getThumbnail();
                    videoGankBean.setImageUrl(thumbnail);
                    mVideoLists.add(videoGankBean);
                }
                mVideoGankAdapter.replaceData(mVideoLists);
            }
        }
        stopRefresh();
    }

    @Override
    public void rxOnNext(AndroidListRes androidListRes) {

    }

    private void stopRefresh() {
        if(mRefreshLayout==null) return;

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
        if (mVideoPresenter != null) {
            mVideoPresenter.onDetach();
        }
    }


}
