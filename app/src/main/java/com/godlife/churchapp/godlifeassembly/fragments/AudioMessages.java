package com.godlife.churchapp.godlifeassembly.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.godlife.churchapp.godlifeassembly.R;
import com.godlife.churchapp.godlifeassembly.activities.MainActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioMessages extends Fragment {
    private View messages_view;
    private WebView webView;
    private KProgressHUD hud;


    public AudioMessages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        messages_view= inflater.inflate(R.layout.fragment_audio_messages, container, false);


        webView = (WebView) messages_view.findViewById(R.id.audio_webview);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Loading Audio Messages...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.BLACK)
                .setAutoDismiss(true);


        webView.setWebViewClient(new MyWebViewClient());

        String url = "https://pastorchingtok.com/pastor-chingtok/";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);

        webView.loadUrl("https://pastorchingtok.com/pastor-chingtok/");


        webView.getSettings().setJavaScriptEnabled(true); // enable javascript

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);


        // Force links and redirects to open in the WebView instead of in a browser

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        return messages_view;


    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            hud.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(hud!=null){
                hud.dismiss();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        ((MainActivity) getActivity()).setActionBarTitle("Audio Messages");

    }

}
