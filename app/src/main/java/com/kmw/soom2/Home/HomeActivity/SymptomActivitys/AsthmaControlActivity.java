package com.kmw.soom2.Home.HomeActivity.SymptomActivitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Home.HomeActivity.AdultActivitys.AdultCheckFirstActivity;
import com.kmw.soom2.Home.HomeActivity.KidsActivitys.KidsCheckFirstActivity;
import com.kmw.soom2.R;

public class AsthmaControlActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AsthmaControlActivity";
    TextView txtBack;
    Button btnAdult,btnKids;
    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asthma_control);

        beforeIntent = getIntent();

        FindViewById();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_asthma_first_back);
        btnAdult = (Button)findViewById(R.id.btn_asthma_first_adult);
        btnKids = (Button)findViewById(R.id.btn_asthma_first_kids);

        txtBack.setOnClickListener(this);
        btnAdult.setOnClickListener(this);
        btnKids.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_asthma_first_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_asthma_first_adult : {
                Intent i = new Intent(this,AdultCheckFirstActivity.class);
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("homeFragment")){
                        i.putExtra("homeFragment",true);
                    }
                }
                startActivity(i);
                break;
            }
            case R.id.btn_asthma_first_kids : {
                Intent i = new Intent(this,KidsCheckFirstActivity.class);
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("homeFragment")){
                        i.putExtra("homeFragment",true);
                    }
                }
                startActivity(i);
                break;
            }
        }
    }
}
