package com.apps.trollino.utils.dialogs;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.trollino.R;

public class WebViewDialog{

    public void showWebDialog(Context context, String url) {
//        String url = "<!doctype html>".concat("<html><body>").concat(url1).concat("</body></html>");

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_web_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(width, height);

        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar_dialog_web_view);
        WebView webView = dialog.findViewById(R.id.dialog_web_view);


        Log.d("OkHttp_1", "tiktok video in dialog " + url);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.loadUrl(url);
//        webView.loadData(url, "text/html; charset=UTF-8", null);

        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("OkHttp_1", "shouldOverrideUrlLoading " + url);

//                webView.loadData(url, "text/html; charset=UTF-8", null);
//                webView.loadData(url, "text/html; charset=UTF-8", "UTF-8");
                webView.loadUrl(url);
                Log.d("OkHttp_1", "1 - !!!!!!!!!!!!!!!!!!!!!!!!! " + url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(request.getUrl().toString());
//                String header = "<!Doctype html>";
//                webView.loadData(header + url, "text/html; charset=UTF-8", null);

//                webView.loadData(url, "text/html; charset=UTF-8", null);
                webView.loadUrl(url);
                Log.d("OkHttp_1", "2 -!!!!!!!!!!!!!!!!!!!!!!!!! " + url);
                return true;
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                Log.d("OkHttp_1", "4 -!!!!!!!!!!!!!!!!!!!!!!!!! " + url);
                Toast.makeText(context, "Страница загружена!", Toast.LENGTH_SHORT).show();
            }

            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                Log.d("OkHttp_1", "3 -!!!!!!!!!!!!!!!!!!!!!!!!! " + url);
                Toast.makeText(context, "Начата загрузка страницы", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient());


        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog1.cancel();
                return true;
            }
            return false;
        });

        dialog.show();
    }

}
