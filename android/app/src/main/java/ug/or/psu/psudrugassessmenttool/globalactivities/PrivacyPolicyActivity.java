package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.http.SslError;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    WebView web_view;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        progressBar = ProgressDialog.show(this, "Loading","Please wait...", true);
        progressBar.setCancelable(false);

        web_view = findViewById(R.id.web_view);
        web_view.setWebViewClient(new MyBrowser());
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //web_view.loadUrl("http://www.psucop.com/app/Privacy_Policy.pdf");
        web_view.loadUrl("https://psucop.com/psu_assessment_tool/privacy_policy.html");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PrivacyPolicyActivity.this);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
