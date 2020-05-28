package com.kmw.soom2.InsertActivity.InsertActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;

public class YKActivity extends AppCompatActivity {
    TextView mBackTextView;
    WebView provisionWebView, personalWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yk);

        mBackTextView = (TextView) findViewById(R.id.back_text_view_activity_yk);

        provisionWebView =  findViewById(R.id.text_view1_activity_yk);
        personalWebView =  findViewById(R.id.text_view2_activity_yk);
        provisionWebView.loadUrl("file:///android_asset/service_provision.html");
        personalWebView.loadUrl("file:///android_asset/personal_info.html");


        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
