package de.djuelg.vostore.Mappa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import de.djuelg.vostore.R;

public class Site3 extends AppCompatActivity {

    private ImageButton voltar;

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site3);

        // android.support.v7.app.ActionBar bar = getSupportActionBar();
        // bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8F00")));
        Bundle extras = getIntent().getExtras();
        voltar = findViewById(R.id.voltarapp);
        mWebView = (WebView) findViewById(R.id.site);
        //Recebendo informação de outra Activity

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        String url = "https://sacolaodasoportunidades.com/";
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new HelloWebViewClient());



        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Site3.this, MainActivityMappa.class);
                startActivity(intent);
                finish();
            }
        });








    }


    private class HelloWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url)
        {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            //progressBar.setVisibility(view.GONE);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    { //if back key is pressed
        if((keyCode == KeyEvent.KEYCODE_BACK)&& mWebView.canGoBack())
        {
            mWebView.goBack();
            return true;

        }

        return super.onKeyDown(keyCode, event);

    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }

}
