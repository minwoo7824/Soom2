package com.kmw.soom2.MyPage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;

public class NoticeDetailActivity extends AppCompatActivity {
    final String TAG = "NoticeDetailActivity";
    private WebView webView;
    TextView txtBack;

    Intent beforeIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        beforeIntent = getIntent();
        FindViewById();

    }
    void FindViewById() {
        webView = findViewById(R.id.webview_notice_detail);
        txtBack = (TextView)findViewById(R.id.txt_notice_detail_back);
//        webView.setBackgroundColor(0); //배경색
//        webView.setHorizontalScrollBarEnabled(false); //가로 스크롤
//        webView.setVerticalScrollBarEnabled(false);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (beforeIntent.hasExtra("contents")) {
            webView.loadData(beforeIntent.getStringExtra("contents"), "text/html; charset=utf-8" , "UTF-8");
        }

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
