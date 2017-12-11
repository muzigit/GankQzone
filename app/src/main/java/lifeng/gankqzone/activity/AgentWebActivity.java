package lifeng.gankqzone.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lifeng.gankqzone.R;
import lifeng.gankqzone.base.BaseActivity;
import lifeng.gankqzone.util.T;

/**
 * Created by lifeng on 2017/11/15.
 *
 * @description 使用的第三方WebView封装库
 */
public class AgentWebActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private static final String TITLE_FLAG = "TITLE_FLAG";
    private static final String URL_FLAG = "URL_FLAG";
    @BindView(R.id.container)
    LinearLayout mContainer;

    private String mTitle;
    private String mUrl;
    private Toast mTsTitle;
    private PopupMenu mPopupMenu;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);
        ButterKnife.bind(this);

        initIntentData();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra(TITLE_FLAG);
            mUrl = intent.getStringExtra(URL_FLAG);
            if (mUrl == null) {
                Toast.makeText(this, "无效链接!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                initView();
            }
        }
    }

    private void initView() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mContainer, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()
                .defaultProgressBarColor()
                /*** 标题回调 ***/
                .setReceivedTitleCallback(mCallback)
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()//
                .ready()
                .go(mUrl);
    }

    @OnClick({
            R.id.iv_back,
            R.id.iv_finish,
            R.id.iv_more
    })
    public void clickView(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                if (!mAgentWeb.back())
                    this.finish();

                break;
            case R.id.iv_finish:
                this.finish();
                break;
            case R.id.iv_more:
                showPoPup(view);
                break;

        }
    }

    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mToolbarTitle != null)
                mToolbarTitle.setText(title);
        }
    };

    protected WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://") && url.contains("com.youku.phone"))
                return true;
            return false;
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
//            Log.i("Info","progress:"+newProgress);
        }
    };

    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this, view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }

    /**
     * 跳转方法
     *
     * @param context
     * @param title
     * @param url
     */
    public static void startIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, AgentWebActivity.class);
        intent.putExtra(TITLE_FLAG, title);
        intent.putExtra(URL_FLAG, url);
        context.startActivity(intent);
    }

    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.refresh:
                    if (mAgentWeb != null)
                        mAgentWeb.getLoader().reload();
                    return true;

                case R.id.copy:
                    if (mAgentWeb != null)
                        toCopy(AgentWebActivity.this, mAgentWeb.getWebCreator().get().getUrl());
                    return true;
                case R.id.default_browser:
                    if (mAgentWeb != null)
                        openBrowser(mAgentWeb.getWebCreator().get().getUrl());
                    return true;
                case R.id.default_clean:
                    toCleanWebCache();
                    return true;
                default:
                    return false;
            }
        }
    };

    private void toCopy(Context context, String text) {
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));

    }

    private void toCleanWebCache() {
        if (this.mAgentWeb != null) {
            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            Toast.makeText(this, "已清理缓存", Toast.LENGTH_SHORT).show();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
//            AgentWebConfig.clearDiskCache(this.getContext());
        }

    }

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            Toast.makeText(this, targetUrl + " 该链接无法使用浏览器打开。", Toast.LENGTH_SHORT).show();
            return;
        }
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri url = Uri.parse(targetUrl);
//        intent.setData(url);
//        startActivity(intent);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(mUrl);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            T.showLong("打开失败，没有找到可以打开该链接的其它应用");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Info", "result:" + requestCode + " result:" + resultCode);
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
