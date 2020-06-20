package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.http.SslError;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ug.or.psu.psudrugassessmenttool.R;

public class MobilePaymentsWebsiteActivity extends AppCompatActivity {

    WebView web_view;
    ProgressDialog progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_payments_website);

        progressBar = ProgressDialog.show(this, "Loading","Please wait...", true);
        progressBar.setCancelable(false);

        web_view = findViewById(R.id.payments_web_view);
        web_view.setWebViewClient(new MobilePaymentsWebsiteActivity.MyBrowser());
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_view.loadUrl("https://app.psucop.com/index_loginpay.php");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MobilePaymentsWebsiteActivity.this);
            String message = "The website you are visiting may be insecure. Do you want to continue";

            builder.setTitle("SSL Certificate Error");
            builder.setMessage(message);
            builder.setPositiveButton("continue", (dialog, which) -> {
                assert handler != null;
                handler.proceed();
            });
            builder.setNegativeButton("cancel", (dialog, which) -> {
                assert handler != null;
                handler.cancel();
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
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
