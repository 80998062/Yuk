package com.sinyuk.yuk.ui.oauth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
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
import com.sinyuk.yuk.api.AccountManager;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.api.oauth.OauthModule;
import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.ui.feeds.FeedsAdapter;
import com.sinyuk.yuk.utils.BetterViewAnimator;
import com.sinyuk.yuk.widgets.NestedWebView;

import java.net.URL;

import javax.inject.Inject;

import butterknife.BindView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import timber.log.Timber;

/**
 * Created by Sinyuk on 16/8/27.
 */
public class DribbleOauthActivity extends BaseActivity {
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
    @Inject
    AccountManager accountManager;
    private URL mIntentUrl;

    public static Intent getAuthIntent(Context context) {
        final Intent intent = new Intent(context, DribbleOauthActivity.class);
        intent.setData(Uri.parse(DribbleApi.LOGIN_URL));
        return intent;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.dribble_oauth_activity;
    }

    @Override
    protected void beforeInflating() {
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
        Timber.tag("DribbleOauthActivity");
        App.get(this).getAppComponent().plus(new OauthModule()).inject(this);
    }

    @Override
    protected void finishInflating(Bundle savedInstanceState) {
        mViewAnimator.setDisplayedChildId(R.id.web_view);
        setupAppBar();
        initWebViewSettings();
    }

    private void setupAppBar() {
        mToolbar.setTitle(R.string.dribbble_oauth_activity_toolbar_title);
        mToolbar.setNavigationOnClickListener(view -> finish());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {

//        initProgressBar();
        mWebView.setWebViewClient(new MyWebViewClient());

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
        webSetting.setAppCacheEnabled(false);

        // 设置数据库
        webSetting.setDatabaseEnabled(false);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);

        webSetting.setGeolocationEnabled(false);
        //webSetting.setGeolocationDatabasePath(this.getDir("webview_geolocation", 0).getPath());

        webSetting.setSaveFormData(false);

        mWebView.loadUrl(mIntentUrl.toString());

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

    private boolean isAuthCallback(Uri data) {
        return data != null && data.getScheme().equals(DribbleApi.REDIRECT_SCHEMA)
                && data.getAuthority().equals(DribbleApi.REDIRECT_AUTHORITY);
    }

    private void handleAuthCallback(Uri data) {
//        yuk://oauth-callback?code=
        Timber.w("handleAuthCallback -> %s", data.toString());
        Timber.d("authority : %s", data.getAuthority());
        Timber.d("path : %s", data.getPath());
        Timber.d("host : %s", data.getHost());

        // use the parameter your API exposes for the code (mostly it's "code")
        String code = data.getQueryParameter("code");
        if (!TextUtils.isEmpty(code)) {
            // get access token
            // we'll do that in a minute
            accountManager.onReceiveRequestCode(code);
            // 切换到那个
            mViewAnimator.setDisplayedChildId(R.id.dribble_oauth_succeed_layout);
            clearWebView();
            Timber.d("code -> %s", code);
        } else if (data.getQueryParameter("error") != null) {
            // show an error message here
            Timber.e("error -> %s", data.getQueryParameter("error"));
        }
    }


    public void onReceivedErrors(int code) {
        if (code != -1) {

        }
        mViewAnimator.setDisplayedChildId(R.id.layout_error);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Timber.d("Url -> %s", url);
            if (isAuthCallback(Uri.parse(url))) {
                handleAuthCallback(Uri.parse(url));
                return false;
            } else {
                view.loadUrl(url);
                return true;
            }

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Timber.d("Url -> %s", request.getUrl().toString());
            if (isAuthCallback(request.getUrl())) {
                handleAuthCallback(request.getUrl());
                return false;
            } else {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
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
}
