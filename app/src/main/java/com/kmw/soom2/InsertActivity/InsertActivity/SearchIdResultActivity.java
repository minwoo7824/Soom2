package com.kmw.soom2.InsertActivity.InsertActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;

public class SearchIdResultActivity extends AppCompatActivity {

    TextView mIdResultTextView;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id_result);

        mIdResultTextView = (TextView) findViewById(R.id.search_result_id_activity_search_id_result);
        btnFinish = (Button)findViewById(R.id.login_success_btn_activity_login);

        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra("email");
        mIdResultTextView.setText(email);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                onBackPressed();
            }
        });
    }
}
