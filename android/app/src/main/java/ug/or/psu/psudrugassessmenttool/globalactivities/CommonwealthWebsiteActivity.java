package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.http.SslError;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ug.or.psu.psudrugassessmenttool.R;

public class CommonwealthWebsiteActivity extends AppCompatActivity {

    WebView web_view;
    ProgressDialog progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonwealth_website);

        progressBar = ProgressDialog.show(this, "Loading","Please wait...", true);
        progressBar.setCancelable(false);

        web_view = findViewById(R.id.commonwealth_web_view);
        web_view.setWebViewClient(new MyBrowser());
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_view.loadUrl("https://viewer.microguide.global/CPA/CWPAMS");
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.show();
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            progressBar.dismiss();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // ignore ssl error
            progressBar.dismiss();
            if (handler != null){
                handler.proceed();
            } else {
                super.onReceivedSslError(view, null, error);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(web_view!= null && web_view.canGoBack()){
            web_view.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
