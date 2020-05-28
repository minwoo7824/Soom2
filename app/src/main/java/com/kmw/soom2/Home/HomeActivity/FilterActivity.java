package com.kmw.soom2.Home.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Home.Fragment.HomeFragment;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.OneBtnPopup;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "FilterActivity";

    ImageView imgBack;
    CheckBox drugCheckedChecBox, asthmaCheckedChecBox, symptomCheckedChecBox, dustCheckedChecBox, lungsCheckedChecBox, memoCheckedChecBox;
    LinearLayout drugCheckedChecBoxParent, asthmaCheckedChecBoxParent, symptomCheckedChecBoxParent, dustCheckedChecBoxParent, lungsCheckedChecBoxParent, memoCheckedChecBoxParent;
    Button successButton;
    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        beforeIntent = getIntent();

        FindViewById();
    }

    void FindViewById() {
        imgBack = (ImageView) findViewById(R.id.img_filter_back);
        drugCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_drug_activity_filter);
        asthmaCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_asthma_activity_filter);
        symptomCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_symptom_activity_filter);
        dustCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_dust_activity_filter);
        lungsCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_lungs_activity_filter);
        memoCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_memo_activity_filter);
        drugCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_drug_activity_filter_parent);
        asthmaCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_asthma_activity_filter_parent);
        symptomCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_symptom_activity_filter_parent);
        dustCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_dust_activity_filter_parent);
        lungsCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_lungs_activity_filter_parent);
        memoCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_memo_activity_filter_parent);
        successButton = (Button) findViewById(R.id.success_btn_activity_filter);

        if (HomeFragment.filterTextList.contains("1")) {
            drugCheckedChecBox.setChecked(true);
        }
        if (HomeFragment.filterTextList.contains("11") || HomeFragment.filterTextList.contains("12") ||
                HomeFragment.filterTextList.contains("13") || HomeFragment.filterTextList.contains("14")) {
            symptomCheckedChecBox.setChecked(true);
        }
        if (HomeFragment.filterTextList.contains("21")) {
            asthmaCheckedChecBox.setChecked(true);
        }
        if (HomeFragment.filterTextList.contains("22")) {
            lungsCheckedChecBox.setChecked(true);
        }
        if (HomeFragment.filterTextList.contains("23")) {
            dustCheckedChecBox.setChecked(true);
        }
        if (HomeFragment.filterTextList.contains("30")) {
            memoCheckedChecBox.setChecked(true);
        }
        drugCheckedChecBoxParent.setOnClickListener(this);
        asthmaCheckedChecBoxParent.setOnClickListener(this);
        symptomCheckedChecBoxParent.setOnClickListener(this);
        dustCheckedChecBoxParent.setOnClickListener(this);
        lungsCheckedChecBoxParent.setOnClickListener(this);
        memoCheckedChecBoxParent.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        successButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.checkbox_drug_activity_filter_parent: {
                drugCheckedChecBox.performClick();
                break;
            }
            case R.id.checkbox_asthma_activity_filter_parent: {
                asthmaCheckedChecBox.performClick();
                break;
            }
            case R.id.checkbox_symptom_activity_filter_parent: {
                symptomCheckedChecBox.performClick();
                break;
            }
            case R.id.checkbox_dust_activity_filter_parent: {
                dustCheckedChecBox.performClick();
                break;
            }
            case R.id.checkbox_lungs_activity_filter_parent: {
                lungsCheckedChecBox.performClick();
                break;
            }
            case R.id.checkbox_memo_activity_filter_parent: {
                memoCheckedChecBox.performClick();
                break;
            }
            case R.id.img_filter_back: {
                onBackPressed();
                break;
            }
            case R.id.success_btn_activity_filter: {
                if (drugCheckedChecBox.isChecked() || asthmaCheckedChecBox.isChecked() || symptomCheckedChecBox.isChecked() || dustCheckedChecBox.isChecked()
                        || lungsCheckedChecBox.isChecked() || memoCheckedChecBox.isChecked()) {
                    if (drugCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("1")) {
                            HomeFragment.filterTextList.add("1");
                            Log.i(TAG,"여기 1");
                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("1")) {
                            HomeFragment.filterTextList.remove("1");
                            Log.i(TAG,"여기 2");

                        }
                    }
                    if (asthmaCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("21")) {
                            HomeFragment.filterTextList.add("21");
                            Log.i(TAG,"여기 3");

                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("21")) {
                            HomeFragment.filterTextList.remove("21");
                            Log.i(TAG,"여기 4");

                        }
                    }
                    if (symptomCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("11")) {
                            HomeFragment.filterTextList.add("11");
                            Log.i(TAG,"여기 5");

                        }
                        if (!HomeFragment.filterTextList.contains("12")) {
                            HomeFragment.filterTextList.add("12");
                            Log.i(TAG,"여기 6");

                        }
                        if (!HomeFragment.filterTextList.contains("13")) {
                            HomeFragment.filterTextList.add("13");
                            Log.i(TAG,"여기 7");

                        }
                        if (!HomeFragment.filterTextList.contains("14")) {
                            HomeFragment.filterTextList.add("14");
                            Log.i(TAG,"여기 8");

                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("11")) {
                            HomeFragment.filterTextList.remove("11");
                            Log.i(TAG,"여기 9");

                        }
                        if (HomeFragment.filterTextList.contains("12")) {
                            HomeFragment.filterTextList.remove("12");
                            Log.i(TAG,"여기 10");

                        }
                        if (HomeFragment.filterTextList.contains("13")) {
                            HomeFragment.filterTextList.remove("13");
                            Log.i(TAG,"여기 11");

                        }
                        if (HomeFragment.filterTextList.contains("14")) {
                            HomeFragment.filterTextList.remove("14");
                            Log.i(TAG,"여기 12");

                        }
                    }
                    if (dustCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("23")) {
                            HomeFragment.filterTextList.add("23");
                            Log.i(TAG,"여기 13");

                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("23")) {
                            HomeFragment.filterTextList.remove("23");
                            Log.i(TAG,"여기 14");

                        }
                    }
                    if (lungsCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("22")) {
                            HomeFragment.filterTextList.add("22");
                            Log.i(TAG,"여기 15");

                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("22")) {
                            HomeFragment.filterTextList.remove("22");
                            Log.i(TAG,"여기 16");

                        }
                    }
                    if (memoCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("30")) {
                            HomeFragment.filterTextList.add("30");
                            Log.i(TAG,"여기 17");

                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("30")) {
                            HomeFragment.filterTextList.remove("30");
                            Log.i(TAG,"여기 18");

                        }
                    }

                    onBackPressed();
                } else {
                    OneBtnPopup(this, "필터", "최소 1개 이상 선택되어 있어야 합니다.", "확인");
                }
                break;
            }
        }
    }
}
