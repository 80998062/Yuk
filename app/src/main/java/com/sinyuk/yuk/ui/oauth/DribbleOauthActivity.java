package com.sinyuk.yuk.ui.oauth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinyuk.yuk.App;
import com.sinyuk.yuk.R;
import com.sinyuk.yuk.api.AccountManager;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.api.oauth.OauthModule;
import com.sinyuk.yuk.data.user.User;
import com.sinyuk.yuk.ui.BaseActivity;
import com.sinyuk.yuk.utils.BetterViewAnimator;
import com.sinyuk.yuk.utils.StringUtils;
import com.sinyuk.yuk.widgets.NestedWebView;

import java.net.URL;

import javax.inject.Inject;

import butterknife.BindView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;
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
    @BindView(R.id.failded_layout)
    RelativeLayout mFailedLayout;
    @BindView(R.id.favicon)
    ImageView mFavicon;
    @BindView(R.id.progress_bar)
    SmoothProgressBar mProgressBar;
    @BindView(R.id.root_view)
    CoordinatorLayout mRootView;
    @Inject
    AccountManager accountManager;
    private URL mIntentUrl;
    private TextView messageTv;

    private final Action1<User> handleAuthResult = new Action1<User>() {
        @Override
        public void call(User user) {
            mViewAnimator.setDisplayedChildId(R.id.dribble_oauth_succeed_layout);
        }
    };

    private final Action1<Throwable> handleAuthError = throwable -> {
        if (throwable instanceof HttpException) {
            HttpException error = (HttpException) throwable;
            handleMillionsOfErrors(error.message(), error.code());
        } else {
            handleMillionsOfErrors(throwable.getLocalizedMessage(), -1);
        }
    };

    public static Intent getAuthIntent(Context context) {
        final Intent intent = new Intent(context, DribbleOauthActivity.class);
        intent.setData(Uri.parse(DribbleApi.LOGIN_URL));
        return intent;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.dribbble_oauth_activity;
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

/*
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null) {
            return;
        }
        Timber.d("on new  -> %s",intent.getData().toString());
        mWebView.loadUrl(intent.getData().toString());
    }
*/

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
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    private void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    private boolean isAuthCallback(Uri data) {
        return data != null && data.getScheme().equals(DribbleApi.REDIRECT_SCHEMA)
                && data.getAuthority().equals(DribbleApi.REDIRECT_AUTHORITY);
    }

    private void handleAuthCallback(Uri data) {
//        yuk://oauth-callback?code=
        hideProgress();
        mViewAnimator.setDisplayedChildId(R.id.wait_layout);
        // use the parameter your API exposes for the code (mostly it's "code")
        String code = data.getQueryParameter("code");
        if (!TextUtils.isEmpty(code)) {
            // get access token we'll do that in a minute
            addSubscription(accountManager.getAccessToken(code)
                    .doOnNext(accessToken -> accountManager.saveAccessToken(accessToken))
                    .flatMap(accessToken -> accountManager.refreshUserProfile())
                    .doOnError(handleAuthError)
                    .doOnTerminate(this::clearCookies)
                    .subscribe(handleAuthResult));
        } else if (data.getQueryParameter("error") != null) {
            // show an error message here
            handleMillionsOfErrors(data.getQueryParameter("error"), -1);
        }
    }

    public void handleMillionsOfErrors(String msg, int code) {
        if (messageTv == null) {
            messageTv = (TextView) mFailedLayout.findViewById(R.id.message_tv);
        }
        if (messageTv == null) return;

        if (code != -1) {
            messageTv.setText(code + " <-----> ");
        }

        messageTv.append(StringUtils.valueOrDefault(msg, getString(R.string.dribble_oauth_error_message)));

        hideProgress();
        mViewAnimator.setDisplayedChildId(R.id.failded_layout);
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.progressiveStart();
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.progressiveStop();
    }

/*    private boolean isRedirectUri(final String msg, final int code) {
        Timber.d("Redirect err msg : %s", msg);
        Timber.d("Redirect err code : %d", code);
        return (DribbleApi.REDIRECT_AUTHORITY).equals(Uri.parse(msg).getAuthority()) && code == DribbleApi.REDIRECT_URL_ERROR_CODE;
    }

    private boolean isRedirectUri(final Uri uri, final int code) {
        Timber.d("Redirect err msg : %s", uri.toString());
        Timber.d("Redirect err code : %d", code);
        return (DribbleApi.REDIRECT_AUTHORITY).equals(uri.getAuthority()) && code == DribbleApi.REDIRECT_URL_ERROR_CODE;
    }*/

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Timber.d("Url -> %s", url);
                if (isAuthCallback(Uri.parse(url))) {
                    handleAuthCallback(Uri.parse(url));
                } else {
                    view.loadUrl(url);
                }
            }
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Timber.d("Url -> %s", request.getUrl());
                if (isAuthCallback(request.getUrl())) {
                    handleAuthCallback(request.getUrl());
                } else {
                    view.loadUrl(request.getUrl().toString());
                }
            }
            return false;
        }

   /*     @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (isRedirectUri(request.getUrl(), error.getErrorCode())) {
                mViewAnimator.setDisplayedChildId(R.id.wait_layout);
            } else {
                handleMillionsOfErrors(error.getDescription().toString(), error.getErrorCode());
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (isRedirectUri(failingUrl, errorCode)) {
                mViewAnimator.setDisplayedChildId(R.id.wait_layout);
            } else {
                handleMillionsOfErrors(description, errorCode);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            handleMillionsOfErrors(errorResponse.getReasonPhrase(), errorResponse.getStatusCode());
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handleMillionsOfErrors(error.getUrl(), error.getPrimaryError());
        }*/

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (favicon != null) {
                mFavicon.setImageBitmap(favicon);
            }
            showProgress();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgress();
        }
    }
}
