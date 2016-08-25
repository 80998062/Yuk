package com.sinyuk.yuk.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sinyuk.yuk.R;
import com.sinyuk.yuk.utils.NetWorkUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.net.URL;

import butterknife.BindView;
import timber.log.Timber;

public class BrowserActivity extends BaseActivity {
    private static final String mHomeUrl = "https://dribbble.com/shots";
    private static final int MAX_LENGTH = 14;
    private static final long MAX_CACHE_SIZE = 1024 * 1024 * 50;

    @BindView(R.id.root_view)
    CoordinatorLayout mRootView;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.title_tv)
    TextView mTitleTv;

    private URL mIntentUrl;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_browser;
    }

    @Override
    protected void beforeInflating() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        try {
            getWindow().setFlags(
                    android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getData().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finishInflating(Bundle savedInstanceState) {
        if (savedInstanceState == null) { initWebViewSettings(); }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) { mWebView.onPause(); }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null) { return; }
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) { mWebView.onResume(); }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else { return super.onKeyDown(keyCode, event); }
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {

//        initProgressBar();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        // 注入一个Cache path 跟Okhttp一起
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setAllowContentAccess(true);
        webSetting.enableSmoothTransition();
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);

        // Zoom
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        // 设置缓存
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCacheMaxSize(MAX_CACHE_SIZE);
        webSetting.setAppCachePath(this.getDir("webview_cache", 0).getPath());
        if (!NetWorkUtils.isNetworkConnection(this)) {
            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }

        // 设置数据库
        webSetting.setDatabaseEnabled(false);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);

        webSetting.setGeolocationEnabled(false);
        //webSetting.setGeolocationDatabasePath(this.getDir("webview_geolocation", 0).getPath());


        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        //webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSetting.setSaveFormData(true);
        webSetting.setSavePassword(true);

        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /*  This method is called only for main frame.
         *  When onPageFinished() is called,
         *  the rendering picture may not be updated yet
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
         /*       moreMenuClose();
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
                    changGoForwardButton(view);
                }
				*//* mWebView.showLog("test Log"); */
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

            Timber.d("request.getUrl().toString() is %s", request.getUrl().toString());

            return super.shouldInterceptRequest(view, request);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO Auto-generated method stub
             /*   mPageLoadingProgressBar.setProgress(newProgress);
                if (mPageLoadingProgressBar != null && newProgress != 100) {
                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                } else if (mPageLoadingProgressBar != null) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
                }*/
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mTitleTv == null) { return; }
            if (!mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                if (title != null && title.length() > MAX_LENGTH) {
                    mTitleTv.setText(title.subSequence(0, MAX_LENGTH) + "...");
                } else { mTitleTv.setText(title); }
            } else {
                mTitleTv.setText("");
            }
        }
    }

}
