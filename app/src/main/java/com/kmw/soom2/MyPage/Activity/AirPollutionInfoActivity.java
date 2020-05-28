package com.kmw.soom2.MyPage.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;

public class AirPollutionInfoActivity extends AppCompatActivity {
    TextView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_pollution_info);

        btnBack = (TextView)findViewById(R.id.back_text_view_activity_air_pollution_info);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
