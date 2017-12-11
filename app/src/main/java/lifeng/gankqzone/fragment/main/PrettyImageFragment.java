package lifeng.gankqzone.fragment.main;

import android.content.ComponentCallbacks2;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.MyApplication;
import lifeng.gankqzone.R;
import lifeng.gankqzone.activity.ImageActivity;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.bean.ImageRes;
import lifeng.gankqzone.network.callback.IImaeView;
import lifeng.gankqzone.network.presenter.ImagePresenter;
import lifeng.gankqzone.util.T;
import lifeng.gankqzone.util.glide.GlideUtils;

/**
 * Created by lifeng on 2017/10/24.
 *
 * @description
 */
public class PrettyImageFragment extends BaseFragment implements IImaeView<ImageRes>, ComponentCallbacks2 {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private ImagePresenter mImagePresenter;
    /*** 每次请求数量 ***/
    private int pageCount = 10;
    /*** 当前页 ***/
    private int curPage;

    private ArrayList<ImageRes.ResultsBean> mImageLists = new ArrayList<>();
    private ImageAdapter mImageAdapter;

    @Override
    protected void initPresenter() {
        mImagePresenter = new ImagePresenter();
        mImagePresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pretty_image, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initListener();
    }

    private void initData() {
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);
        mImageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ImageRes.ResultsBean resultsBean = mImageLists.get(position);
                ImageActivity.startIntent(getActivity(), resultsBean.getUrl(), null);

//                Intent intent = new Intent(getContext(), ImageActivity.class);
//                intent.putExtra("IMAGEURL", resultsBean.getUrl());
//                intent.putExtra("IMAGEURLS", null);
//                startActivity(intent);

            }
        });
    }

    private boolean isFirst = true;

    private void initListener() {
        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (isFirst) {
                    curPage = 1;
                    sendReq();
                    isFirst = false;
                } else {
                    T.showShort("当前已经是最新数据了");
                    stopRefresh();
                }
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                curPage++;
                sendReq();
            }
        });
    }

    private void sendReq() {
        mImagePresenter.getImage(pageCount, curPage);
    }

    private void initView() {
        isShowLoadingDialog = false;
        initRecyclerView();
        initSmartRefreshLayout(mRefreshLayout);
        if (isFirst) {
            mRefreshLayout.autoRefresh();
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void stopRefresh() {
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
        curPage--;
        stopRefresh();
    }

    @Override
    public void rxOnNext(ImageRes imageRes) {
        if (!imageRes.isError()) {
            List<ImageRes.ResultsBean> imageResResults = imageRes.getResults();
            if (imageResResults != null && imageResResults.size() > 0) {
                for (int i = 0; i < imageResResults.size(); i++) {
                    mImageLists.add(imageResResults.get(i));
                }

                mImageAdapter.replaceData(mImageLists);
            } else {
                curPage--;
            }
            stopRefresh();
        }
    }

    @Override
    public void getImageRandom(ImageRes imageRes) {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    public class ImageAdapter extends BaseQuickAdapter<ImageRes.ResultsBean, BaseViewHolder> {

        public ImageAdapter() {
            super(R.layout.item_image_stream);
        }

        @Override
        protected void convert(final BaseViewHolder viewHolder, final ImageRes.ResultsBean item) {


            final ImageView imageView = viewHolder.getView(R.id.imageView);

            GlideUtils.LoadImages(MyApplication.getContext(), item.getUrl(), new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    int intrinsicHeight = resource.getIntrinsicHeight();
                    int intrinsicWidth = resource.getIntrinsicWidth();
                    Log.i("TTT", "intrinsicHeight:" + intrinsicHeight + " intrinsicWidth:" + intrinsicWidth);
//                    int width = ((Activity) imageView.getContext()).getWindowManager().getDefaultDisplay().getWidth();
                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    //设置图片的相对于图片的宽高比`
//                    params.width = width / 2;
//                    params.height = (int) (200 + Math.random() * 400);

                    params.width = intrinsicWidth / 2;
                    params.height = intrinsicHeight / 2;
                    imageView.setLayoutParams(params);

                    imageView.setImageDrawable(resource);
                }
            });
            viewHolder.addOnClickListener(R.id.imageView);
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
        if (mImagePresenter != null) {
            mImagePresenter.onDetach();
        }
    }
}
