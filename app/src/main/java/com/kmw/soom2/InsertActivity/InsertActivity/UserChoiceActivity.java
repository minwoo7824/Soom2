package com.kmw.soom2.InsertActivity.InsertActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;


public class UserChoiceActivity extends AppCompatActivity {
    ImageView mChoiceMeImageView,mChoiceParentImageView;
    String lv;
    Intent beforeIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choice);

        beforeIntent = getIntent();

        mChoiceMeImageView = (ImageView) findViewById(R.id.choice_me_activity_user_choice);
        mChoiceParentImageView = (ImageView) findViewById(R.id.choice_parent_activity_user_choice);



        View.OnClickListener choiceUser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.choice_me_activity_user_choice: {
                        lv = "11";
                        break;
                    }
                    case R.id.choice_parent_activity_user_choice: {
                        lv = "22";
                        break;
                    }
                }
                Intent intent = new Intent(UserChoiceActivity.this, UserInfoActivity.class);

                intent.putExtra("LV", lv);
                intent.putExtra("EMAIL", beforeIntent.getStringExtra("EMAIL"));
                if (beforeIntent.hasExtra("PASSWORD")) {
                    intent.putExtra("PASSWORD", beforeIntent.getStringExtra("PASSWORD"));
                }
                intent.putExtra("NICKNAME", beforeIntent.getStringExtra("NICKNAME"));
                intent.putExtra("LOGIN_TYPE", beforeIntent.getIntExtra("LOGIN_TYPE", 0));
                intent.putExtra("ID", beforeIntent.getStringExtra("ID"));
                startActivity(intent);
            }
        };

       mChoiceMeImageView.setOnClickListener(choiceUser);
       mChoiceParentImageView.setOnClickListener(choiceUser);

    }
}
