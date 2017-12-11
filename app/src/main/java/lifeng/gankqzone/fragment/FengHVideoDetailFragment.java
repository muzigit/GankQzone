package lifeng.gankqzone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.R;
import lifeng.gankqzone.adapter.VideoDetailAdapter;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.bean.VideoDetailRes;
import lifeng.gankqzone.bean.fengh.VideoChannelRes;
import lifeng.gankqzone.network.callback.IFengHVideoView;
import lifeng.gankqzone.network.presenter.FengHVideoPresenter;
import lifeng.gankqzone.view.RecyclerViewDecoration;

/**
 * Created by lifeng on 2017/11/17.
 *
 * @description 凤凰视频详情页
 */
public class FengHVideoDetailFragment extends BaseFragment implements IFengHVideoView<VideoChannelRes> {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private int page;
    private boolean isFirst = true;

    public static final String TYPEID = "typeid";
    public static final String ITEM_POSITION = "ITEM_POSITION";
    public static final String ITEM_LIST = "ITEM_LIST";
    private int mItemPosition;
    private String mTypeId;
    private List<VideoDetailRes.ItemBean> mItemBeanList;
    private VideoDetailAdapter mDetailAdapter;
    private FengHVideoPresenter mFengHVideoPresenter;


    public static FengHVideoDetailFragment newInstance(int position, String typeid) {
        Bundle args = new Bundle();
        args.putCharSequence(TYPEID, typeid);
        args.putInt(ITEM_POSITION, position);
        FengHVideoDetailFragment fragment = new FengHVideoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static FengHVideoDetailFragment newInstance(int position, List<VideoDetailRes.ItemBean> itemBeanList) {
        Bundle bundle = new Bundle();
        bundle.putInt(ITEM_POSITION, position);
        bundle.putSerializable(ITEM_LIST, (Serializable) itemBeanList);
        FengHVideoDetailFragment fragment = new FengHVideoDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initPresenter() {
        mFengHVideoPresenter = new FengHVideoPresenter();
        mFengHVideoPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fengh_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initIntentData();
        initView();
        initData();
        setListener();
    }

    private void initIntentData() {
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mItemPosition = bundle.getInt(ITEM_POSITION);
        Log.i("TTT", "mItemPosition:" + mItemPosition);

        if (mItemPosition != 0) {
//            mItemBeanList = (List<VideoDetailRes.ItemBean>) bundle.getSerializable(ITEM_LIST);

            Log.i("TTT", "mTypeId:" + mTypeId);
            mTypeId = bundle.getString(TYPEID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {
        mDetailAdapter = new VideoDetailAdapter(mItemBeanList, new WeakReference<Context>(getContext()));
        mRecyclerView.setAdapter(mDetailAdapter);
    }

    private void initView() {
        isShowLoadingDialog = false;

        initRecyclerView();
        initSmartRefreshLayout(mRefreshLayout);
//        mRefreshLayout.autoLoadmore();
        mRefreshLayout.autoRefresh();
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

    private void setListener() {
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                if (mItemPosition == 0) {
                    mFengHVideoPresenter.getVideoChannelList(page);
                } else {
                    mFengHVideoPresenter.getVidelDetailList(page, mTypeId);
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                Log.i("TTT", "onRefresh mItemPosition:" + mItemPosition);
                if (mItemPosition == 0) {
                    mFengHVideoPresenter.getVideoChannelList(page);
                } else {
                    mFengHVideoPresenter.getVidelDetailList(page, mTypeId);
                }
            }
        });
    }


    @Override
    public void rxOnError(Throwable e) {
        super.rxOnError(e);
        page--;
        stopRefresh(mRefreshLayout);
    }

    @Override
    public void getVideoChannelList(List<VideoChannelRes> videoChannelRes) {
        if (videoChannelRes != null && videoChannelRes.size() > 0) {
            VideoChannelRes videoChannelList = videoChannelRes.get(0);
            mItemBeanList = videoChannelList.getItem();
            if (mItemBeanList != null && mItemBeanList.size() > 0) {
                for (int i = 0; i < mItemBeanList.size(); i++) {
                    //获取视频的分钟数
                    int duration = mItemBeanList.get(i).getDuration();
                    Log.i("TTT", "视频分钟数duration:" + duration);
                    if (duration / 60 < 2 * 60) {
                        mItemBeanList.get(i).itemType = VideoDetailRes.ItemBean.SIFT_SHORT_TYPE;
                    } else {
                        mItemBeanList.get(i).itemType = VideoDetailRes.ItemBean.SIFT_Long_TYPE;
                    }
                }
                if(page == 1) {
                    mDetailAdapter.setNewData(mItemBeanList);
                } else {
                    mDetailAdapter.addData(mItemBeanList);
                }
            }
        }
        stopRefresh(mRefreshLayout);
    }

    @Override
    public void getVidelDetailList(List<VideoDetailRes> videoDetailRes) {
        if (videoDetailRes != null && videoDetailRes.size() > 0) {
            VideoDetailRes detailResList = videoDetailRes.get(0);
            mItemBeanList = detailResList.getItem();
            if (mItemBeanList != null && mItemBeanList.size() > 0) {
                for(int i = 0; i < mItemBeanList.size(); i++) {
                    mItemBeanList.get(i).itemType = VideoDetailRes.ItemBean.NORMAL_TYPE;
                }
                if(page == 1) {
                    mDetailAdapter.setNewData(mItemBeanList);
                } else {
                    mDetailAdapter.addData(mItemBeanList);
                }
            }
        }
        stopRefresh(mRefreshLayout);
    }

    @Override
    public void rxOnNext(VideoChannelRes videoChannelRes) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFengHVideoPresenter != null) {
            mFengHVideoPresenter.onDetach();
        }
    }
}
