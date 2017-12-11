package lifeng.gankqzone.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daimajia.numberprogressbar.NumberProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import lifeng.gankqzone.R;
import lifeng.gankqzone.base.BaseActivity;
import lifeng.gankqzone.util.T;

import static lifeng.gankqzone.R.id.webView;

/**
 * Created by lifeng on 2017/10/30.
 *
 * @description
 */
public class WebViewActivity extends BaseActivity {
    private static final String TITLE_FLAG = "title";
    private static final String URL_FLAG = "url";
    @BindView(R.id.tsTitle)
    TextSwitcher mTsTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.progressbar)
    NumberProgressBar mProgressbar;
    @BindView(webView)
    WebView mWebView;

    private String mTitle;
    private String mUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        initTextSwitcher();
        getIntentData();
    }

    private void initTextSwitcher() {
        mTsTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView textView = new TextView(WebViewActivity.this);
                textView.setTextAppearance(WebViewActivity.this, R.style.WebTitle);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setSelected(true);
                    }
                }, 1738);
                return textView;
            }
        });

        mTsTitle.setInAnimation(this, android.R.anim.fade_in);
        mTsTitle.setOutAnimation(this, android.R.anim.fade_out);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra(TITLE_FLAG);
            mUrl = intent.getStringExtra(URL_FLAG);
            if (mTitle != null) {
                mTsTitle.setText(mTitle);
            } else {
                mTsTitle.setText(getString(R.string.gank_name));
            }
            if (mUrl == null) {
                Toast.makeText(WebViewActivity.this, "无效链接!", Toast.LENGTH_SHORT).show();
                return;
            }

            initWebView();
        }
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        /*** 设置WebView支持自带的缩放功能 ***/
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        /*** 不显示WebView缩放按钮 ***/
        settings.setDisplayZoomControls(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setDefaultTextEncodingName("utf-8");//这句话去掉也没事。。只是设置了编码格式
        settings.setDomStorageEnabled(true);//这句话必须保留。。否则无法播放优酷视频网页。。其他的可以

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setBlockNetworkImage(false);

        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new LoveClient());

        mWebView.loadUrl(mUrl);
    }

    /*** 创建菜单栏 ***/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            /*** 刷新 ***/
            case R.id.action_refresh:
                //重新加载
                mWebView.reload();
                return true;
            /*** 复制链接 ***/
            case R.id.action_copy_url:
                ClipData clipData = ClipData.newPlainText("GankQzone_copy", mWebView.getUrl());
                ClipboardManager manager = (ClipboardManager) this.getSystemService(
                        Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(clipData);
                T.showShort("复制成功!");
                return true;
            /*** 打开链接 ***/
            case R.id.action_open_url:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(mUrl);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    T.showLong("打开失败，没有找到可以打开该链接的其它应用");
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
            } else {
                mProgressbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    private class LoveClient extends WebViewClient {

        //        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url != null) view.loadUrl(url);
//            return true;
//        }
        public boolean shouldOverrideUrlLoading(WebView view, String url) {//这个方法必须重写。否则会出现优酷视频周末无法播放。周一-周五可以播放的问题
            if (url.startsWith("intent") || url.startsWith("youku")) {
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }


    }

    /**
     * 跳转方法
     *
     * @param context
     * @param title
     * @param url
     */
    public static void startIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(TITLE_FLAG, title);
        intent.putExtra(URL_FLAG, url);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
