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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lifeng.gankqzone.MyApplication;
import lifeng.gankqzone.R;
import lifeng.gankqzone.activity.ImageActivity;
import lifeng.gankqzone.activity.WebViewActivity;
import lifeng.gankqzone.base.BaseFragment;
import lifeng.gankqzone.bean.ImageRes;
import lifeng.gankqzone.bean.OnedayRes;
import lifeng.gankqzone.config.NetWorkState;
import lifeng.gankqzone.db.GankDao;
import lifeng.gankqzone.network.callback.IImaeView;
import lifeng.gankqzone.network.callback.ITodayView;
import lifeng.gankqzone.network.presenter.ImagePresenter;
import lifeng.gankqzone.network.presenter.TodayPresenter;
import lifeng.gankqzone.util.DateFormatUtil;
import lifeng.gankqzone.util.T;
import lifeng.gankqzone.util.glide.GlideImageLoader;
import lifeng.gankqzone.util.glide.GlideUtils;
import lifeng.gankqzone.view.RecyclerViewDecoration;

import static lifeng.gankqzone.R.id.recyclerView;

/**
 * Created by lifeng on 2017/10/27.
 *
 * @description 热门
 */
public class TodayGankFragment extends BaseFragment implements OnBannerListener, IImaeView<ImageRes>, ITodayView<ImageRes> {

    Unbinder unbinder;
    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private List<String> imageBannerUrls = new ArrayList<>();
    private List<String> imageBannerTitles = new ArrayList<>();

    private ImagePresenter mImagePresenter;
    private List<OnedayRes.Gank> mList = new ArrayList<>();
    private Banner mBanner;
    private QuickAdapter mAdapter;
    private TodayPresenter mTodayPresenter;
    private int mYear;
    private int mMonth;
    private int mDay;

    /*** 记录上次请求的参数 ***/
    private int preYear, preMonth, preDay;
    /*** 记录当前刷新的参数 ***/
    private int refreshYear, refreshMonth, refreshDay;
    /*** 数据库缓存数据日期 ***/
    private int saveYear, saveMonth, saveDay;

    private int reqCount;//请求次数
    private Calendar mCalendar;
    private GankDao mGankDao;

    /***
     * 缓存最新数据
     * 1.在第一次启动App时,缓存第一次请求的数据到数据库中
     * 2.非第一次启动时,展示缓存的数据 并且查看是否有更新的数据,有则缓存到数据库中,并将之前缓存的数据在OnDestroy中删除
     * ***/
    @Override
    protected void initPresenter() {
        mImagePresenter = new ImagePresenter();
        mImagePresenter.onAttach(this);

        mTodayPresenter = new TodayPresenter();
        mTodayPresenter.onAttach(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurDate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_gank, container, false);
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

    /**
     * 获取当前日期
     */
    private void getCurDate() {
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        isShowLoadingDialog = false;

        initRecyclerView();
        initSmartRefreshLayout(mRefreshLayout);

        View header = LayoutInflater.from(getContext()).inflate(R.layout.rv_head_banner, mRecyclerView, false);
        mBanner = (Banner) header;
        initBanner();
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

    private boolean isFirst = true;

    private void initData() {
        mAdapter = new QuickAdapter();
        /*** 设置Item加载动画 ***/
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.addHeaderView(mBanner);
        mRecyclerView.setAdapter(mAdapter);
        if (getCurNetWorkState() != NetWorkState.NO_NETWORK) {
            if (imageBannerUrls.size() < 5) {
                //轮播图随机获取5张图片
                mImagePresenter.getImageRandom(5);
            } else {
                if (mBanner != null) {
                    mBanner.setBannerTitles(imageBannerTitles);
                    mBanner.setImages(imageBannerUrls).start();
                }
            }
        }
        if (isFirst) {
            isFirst = false;
            mRefreshLayout.autoRefresh();
        } else {
            if (mList.size() > 0) {
                mAdapter.replaceData(mList);
            }
        }

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OnedayRes.Gank gank = mList.get(position);
                if (gank.getType().equals("福利") && gank.getUrl() != null) {
                    ImageActivity.startIntent(getActivity(), gank.getUrl(), gank.getDesc());
                } else {
                    WebViewActivity.startIntent(getContext(), gank.getDesc(), gank.getUrl());
                }
            }
        });
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (mYear != 0 && mMonth != 0 && mDay != 0) {
                    refreshYear = mYear;
                    refreshMonth = mMonth;
                    refreshDay = mDay;
                    if (mList.size() > 0) {
                        if (refreshYear == preYear && refreshMonth == preMonth && refreshDay == preDay) {
                            stopRefresh();
                            Toast.makeText(TodayGankFragment.this.getContext(), "当前已经是最新数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            mTodayPresenter.getTodayData(mYear, mMonth, mDay);
                        }
                    } else {
                        mTodayPresenter.getTodayData(mYear, mMonth, mDay);
                    }
                } else {
                    mTodayPresenter.getTodayData(mYear, mMonth, mDay);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                sendReq();
            }
        });

    }

    private void initBanner() {
        mBanner.setImageLoader(new GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setOnBannerListener(this);
    }

    /**
     * 发送网络请求
     */
    private void sendReq() {
        if (mDay == 0) {
            if (mMonth == 0) {
                mMonth = 12;
                mYear--;
            } else {
                mMonth--;
            }
            mDay = 31;
        } else {
            mDay--;
        }
        mTodayPresenter.getTodayData(mYear, mMonth, mDay);
    }

    @Override
    public void OnBannerClick(int position) {
        if (imageBannerUrls.size() > 0) {
//            Intent intent = new Intent(getContext(), ImageActivity.class);
//            intent.putStringArrayListExtra("IMAGE_URLS", (ArrayList<String>) imageBannerUrls);
//                intent.putExtra("IMAGETitle", imageBannerTitles.get(position));
//            startActivity(intent);
            ImageActivity.startIntent(getActivity(), (ArrayList<String>) imageBannerUrls, position, imageBannerTitles.get(position));
        }
    }

    @Override
    public void rxOnError(Throwable e) {
        super.rxOnError(e);
        stopRefresh();
    }

    @Override
    protected void againReq() {
        super.againReq();
        if (reqCount >= 7) {
            T.showShort("服务器异常");
            stopRefresh();
            return;
        }
        sendReq();
        reqCount++;
    }

    @Override
    public void getImageRandom(ImageRes imageRes) {
        if (!imageRes.isError()) {
            if (imageBannerUrls.size() > 0) {
                //清空以前的数据
                imageBannerUrls.clear();
            }

            List<ImageRes.ResultsBean> resultsBeanList = imageRes.getResults();
            String title;
            for (int i = 0; i < resultsBeanList.size(); i++) {
                title = null;
                ImageRes.ResultsBean resultsBean = resultsBeanList.get(i);
                if (resultsBean.getSource() != null) {
                    title = "来源:" + resultsBean.getSource();
                } else {
                    title = getString(R.string.gank_name);
                }
                if (resultsBean.getCreatedAt() != null) {
                    title += "  " + formatStr(resultsBean.getCreatedAt());
                }
                if (resultsBean.getSource() == null && resultsBean.getCreatedAt() == null) {
                    title = getString(R.string.gank_name);
                }

                imageBannerTitles.add(title);
                imageBannerUrls.add(resultsBean.getUrl());
            }
            mBanner.setBannerTitles(imageBannerTitles);
            mBanner.setImages(imageBannerUrls).start();
        }
    }

    @Override
    public void todayGank(OnedayRes onedayRes) {
        if (!onedayRes.isError()) {
            List<String> category = onedayRes.getCategory();
            if (category.size() == 0) {
                sendReq();
            } else {
                stopRefresh();

                preYear = mYear;
                preMonth = mMonth;
                preDay = mDay;
                OnedayRes.ResultsBean resultsBean = onedayRes.getResults();

                addAllData(resultsBean.getAndroidList());
                addAllData(resultsBean.getAppList());
                addAllData(resultsBean.getiOSList());
                addAllData(resultsBean.getImageList());
                addAllData(resultsBean.getRecommendList());
                addAllData(resultsBean.getResourcesList());
                addAllData(resultsBean.getVideoList());
            }
            if (mList.size() > 0) {
                mAdapter.replaceData(mList);
            }
        }
    }

    private void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }

        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore();
        }
    }

    private void addAllData(List<OnedayRes.Gank> lists) {
        if (lists != null && lists.size() > 0) {
            mList.addAll(lists);

            //只保存最新的数据
//            if (isFirst) {
//                for (int i = 0; i < lists.size(); i++) {
//                    //将数据缓存到数据库中
//                    mGankDao.addData(lists.get(i));
//                    if (i == lists.size() - 1) {
//                        //记录存储到数据库时的日期
//                        String saveTime = mYear + "-" + mMonth + "-" + mDay;
//                        Log.i("TodayGankFragment", "记录到数据库的时间:" + saveTime);
//                        SPUtil.saveData(getContext(), SPUtil.SAVE_DAY, saveTime);
//                        isFirst = false;
//                    }
//                }
//            }
        }
    }

    public class QuickAdapter extends BaseQuickAdapter<OnedayRes.Gank, BaseViewHolder> {

        public QuickAdapter() {
            super(R.layout.item_hot);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, OnedayRes.Gank item) {
            viewHolder.setText(R.id.tvTitle, item.getDesc());
//            viewHolder.setVisible(R.id.ivContentIcon, false);
            viewHolder.setVisible(R.id.pb_loading, false);

            //时间格式转换
            viewHolder.setText(R.id.tvTime, formatStr(item.getPublishedAt()));
            if (item.getWho() != null) {
                viewHolder.setText(R.id.tvSource, item.getType() + ":" + item.getWho());
            } else {
                viewHolder.setText(R.id.tvSource, item.getType());
            }

            if (item.getImages() != null && item.getImages().size() > 0) {
//                viewHolder.setVisible(R.id.ivContentIcon, true);
                viewHolder.setVisible(R.id.pb_loading, true);

                GlideUtils.disPlayShowLoading(MyApplication.getContext(), item.getImages().get(0),
                        (ImageView) viewHolder.getView(R.id.ivContentIcon), (ProgressBar) viewHolder.getView(R.id.pb_loading));
            } else {
                if (item.getUrl() != null) {
                    if (item.getUrl().contains(".jpg") || item.getUrl().contains(".png") || item.getUrl().equals(".jpeg")) {
//                        viewHolder.setVisible(R.id.ivContentIcon, true);
                        viewHolder.setVisible(R.id.pb_loading, true);

                        GlideUtils.disPlayShowLoading(MyApplication.getContext(), item.getUrl(),
                                (ImageView) viewHolder.getView(R.id.ivContentIcon), (ProgressBar) viewHolder.getView(R.id.pb_loading));
                    } else {
//                        viewHolder.setVisible(R.id.ivContentIcon, false);
//                        viewHolder.setImageResource(R.id.ivContentIcon,R.mipmap.default_pic);
                    }
                } else {
//                    viewHolder.setImageResource(R.id.ivContentIcon,R.mipmap.default_pic);
                }
            }
        }
    }

    private String formatStr(String date) {
        Date dateFromStr = DateFormatUtil.formatDateFromStr(date);
        return DateFormatUtil.getFormatDateStr(dateFromStr);
    }

    @Override
    public void rxOnNext(ImageRes imageRes) {

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
        if (mTodayPresenter != null) {
            mTodayPresenter.onDetach();
        }
        if (imageBannerUrls.size() > 0) {
            imageBannerUrls.clear();
        }
        if (imageBannerTitles.size() > 0) {
            imageBannerTitles.clear();
        }
        if (mList.size() > 0) {
            mList.clear();
        }
    }
}
