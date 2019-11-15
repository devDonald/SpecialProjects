package com.godlife.churchapp.godlifeassembly.live_service;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.godlife.churchapp.godlifeassembly.R;

public class YoutubeLive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_live);

        String frameVideo = "<html><body>Youtube Live loading... <br> <iframe width=\"400\" height=\"290\" src=\"https://www.youtube.com/embed/live_stream?channel=UCXTMFK4-CSliIT1tXuRXpkg\" frameborder=\"0\" allowfullscreen=\"true\"></iframe></body></html>";

        WebView webView = (WebView)findViewById(R.id.youtube_webview);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadData(frameVideo, "text/html", "utf-8");
    }
}
