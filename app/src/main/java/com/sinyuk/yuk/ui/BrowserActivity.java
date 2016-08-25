package com.sinyuk.yuk.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.tencent.smtt.utils.TbsLog;

import java.net.URL;

public class BrowserActivity extends Activity {
    private static final String mHomeUrl = "https://dribbble.com/shots";
    private static final String TAG = "SdkDemo";
    private static final int MAX_LENGTH = 14;
    private static final long MAX_CACHE_SIZE = 1024 * 1024 * 50;
    private final float disable = 0.5f;
    private final float enable = 1f;
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private WebView mWebView;
    private ViewGroup mViewParent;
    private boolean mNeedTestPage = false;

    private URL mIntentUrl;
    private TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getData().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //
        try {
            getWindow().setFlags(
                    android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

		/*getWindow().addFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_browser);
        mViewParent = (ViewGroup) findViewById(R.id.web_view_container);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null) { return; }
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) { mWebView.destroy(); }
        super.onDestroy();
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

/*    private void changGoForwardButton(WebView view) {
        if (view.canGoBack()) { mBack.setAlpha(enable); } else { mBack.setAlpha(disable); }
        if (view.canGoForward()) { mForward.setAlpha(enable); } else { mForward.setAlpha(disable); }
        if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
            mHome.setAlpha(disable);
            mHome.setEnabled(false);
        } else {
            mHome.setAlpha(enable);
            mHome.setEnabled(true);
        }
    }*/

/*    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
        // ProgressBar(getApplicationContext(),
        // null,
        // android.R.attr.progressBarStyleHorizontal);
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));
    }*/

    private void init() {

        //
        //mWebView = new DemoWebView(this);
        mWebView = (WebView) findViewById(R.id.web_view);
        mTitleTv = (TextView) findViewById(R.id.title_tv);

        Log.w("grass", "Current SDK_INT:" + Build.VERSION.SDK_INT);

//        initProgressBar();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
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
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                // TODO Auto-generated method stub

                Log.e("should", "request.getUrl().toString() is " + request.getUrl().toString());

                return super.shouldInterceptRequest(view, request);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
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
                TbsLog.d(TAG, "title: " + title);
                if (mTitleTv == null) { return; }
                if (!mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                    if (title != null && title.length() > MAX_LENGTH) {
                        mTitleTv.setText(title.subSequence(0, MAX_LENGTH) + "...");
                    } else { mTitleTv.setText(title); }
                } else {
                    mTitleTv.setText("");
                }
            }
        });

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

        long time = System.currentTimeMillis();

        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

}
