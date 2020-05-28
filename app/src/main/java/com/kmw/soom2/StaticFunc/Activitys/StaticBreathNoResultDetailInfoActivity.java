package com.kmw.soom2.StaticFunc.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

public class StaticBreathNoResultDetailInfoActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btnClose;
    Button btnMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_breath_no_result_detail_info);

        btnClose = findViewById(R.id.img_breath_detail_popup_close);
        btnMore = findViewById(R.id.btn_breath_detail_popup_more);

        btnMore.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_breath_detail_popup_close: {
                onBackPressed();
                break;
            }
            case R.id.btn_breath_detail_popup_more: {
                if (Utils.linkKeys != null) {
                    for (int i = 0; i < Utils.linkKeys.size(); i++) {
                        if (Utils.linkKeys.get(i).getTitle().equals("PEFreport")) {
                            if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.linkKeys.get(i).getLinkUrl()));
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}