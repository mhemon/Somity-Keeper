package com.somitykeeper.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.somitykeeper.app.MainActivity;
import com.somitykeeper.app.R;
import com.somitykeeper.app.WelcomeActivity;

import java.net.CookieManager;

import im.delight.android.webview.AdvancedWebView;


public class FeedFragment extends Fragment implements AdvancedWebView.Listener{

    public AdvancedWebView mWebView;
    private ProgressBar webviewprogress;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (mWebView.canGoBack()){
                    mWebView.goBack();
                }
                remove();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed2, container, false);

        boolean isFirstStart = Prefs.getBoolean("firstStart",true);
        //Launch Main Screen
        webviewprogress = view.findViewById(R.id.webview_progress);
        // web setting
        String data = Prefs.getString("WEB", getResources().getString(R.string.defaulturl));
        mWebView = (AdvancedWebView) view.findViewById(R.id.webview);
        mWebView.setListener(getActivity(),this);
        mWebView.setMixedContentAllowed(true);
        mWebView.setCookiesEnabled(true);

        if (isFirstStart) {
            //  Launch welcome screen
            Intent i = new Intent(getActivity(), WelcomeActivity.class);
            Prefs.putBoolean("firstStart", false);
            startActivity(i);
        }else {
            mWebView.loadUrl(data);
        }

        return view;
    }



    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
//        mWebView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        //mWebView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        webviewprogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String url) {
        webviewprogress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
        webviewprogress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        webviewprogress.setVisibility(View.INVISIBLE);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onExternalPageRequest(String url) {
        webviewprogress.setVisibility(View.INVISIBLE);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}