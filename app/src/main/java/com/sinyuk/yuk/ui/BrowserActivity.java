package com.sinyuk.yuk.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sinyuk.yuk.App;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.utils.BetterViewAnimator;
import com.sinyuk.yuk.utils.NetWorkUtils;
import com.sinyuk.yuk.utils.ToastUtils;
import com.sinyuk.yuk.widgets.NestedWebView;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;

import butterknife.BindView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import timber.log.Timber;

public class BrowserActivity extends BaseActivity {
    private static final String mHomeUrl = "https://dribbble.com/shots";
    private static final int MAX_LENGTH = 24;
    @Inject
    File mCacheFile;
    @Inject
    ToastUtils toastUtils;

    @BindView(R.id.view_animator)
    BetterViewAnimator mViewAnimator;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.web_view)
    NestedWebView mWebView;
    @BindView(R.id.menu)
    ImageView mMenu;
    @BindView(R.id.favicon)
    ImageView mFavicon;
    @BindView(R.id.progress_bar)
    SmoothProgressBar mProgressBar;
    @BindView(R.id.layout_error)
    RelativeLayout mLayoutError;
    @BindView(R.id.root_view)
    CoordinatorLayout mRootView;

    private URL mIntentUrl;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_browser;
    }

    @Override
    protected void beforeInflating() {
        App.get(this).getAppComponent().inject(this);

        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
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
        mViewAnimator.setDisplayedChildId(R.id.web_view);
        setupAppBar();
        initWebViewSettings();
    }

    private void setupAppBar() {
        mToolbar.setNavigationOnClickListener(view -> finish());

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {

//        initProgressBar();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        // 注入一个Cache path 跟Okhttp一起
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        }
        webSetting.setAllowContentAccess(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        // Zoom
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        // 设置缓存
        webSetting.setAppCacheEnabled(true);
//        webSetting.setAppCacheMaxSize(MAX_CACHE_SIZE);
        webSetting.setAppCachePath(mCacheFile.getPath());
        if (!NetWorkUtils.isNetworkConnection(this)) {
            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }

        // 设置数据库
        webSetting.setDatabaseEnabled(false);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);

        webSetting.setGeolocationEnabled(false);
        //webSetting.setGeolocationDatabasePath(this.getDir("webview_geolocation", 0).getPath());

//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
//        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSetting.setSaveFormData(true);

        if (mIntentUrl == null) {
            mViewAnimator.setDisplayedChildId(R.id.layout_error);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null) {
            return;
        }
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        clearWebView();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        clearWebView();
        super.onDestroy();
    }

    private void clearWebView() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    public void onReceivedErrors(int code) {
        if (code != -1) {
            toastUtils.toastLong("Error code : " + code);
        }
        mViewAnimator.setDisplayedChildId(R.id.layout_error);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                onReceivedErrors(error.getErrorCode());
            } else {
                onReceivedErrors(-1);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (favicon != null) {
                mFavicon.setImageBitmap(favicon);
            }
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.progressiveStart();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.progressiveStop();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Timber.d("Url : %s", request.getUrl().toString());
            }
            return super.shouldInterceptRequest(view, request);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (mToolbar == null) {
                return;
            }
            if (!mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                if (title != null && title.length() > MAX_LENGTH) {
                    mToolbar.setTitle(title.subSequence(0, MAX_LENGTH) + "...");
                } else {
                    mToolbar.setTitle(title);
                }
            } else {
                mToolbar.setTitle("");
            }
        }
    }

}
