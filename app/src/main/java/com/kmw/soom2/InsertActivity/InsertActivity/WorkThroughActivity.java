package com.kmw.soom2.InsertActivity.InsertActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineSearchActivity;
import com.kmw.soom2.InsertActivity.Adapter.WorkThroughViewPagerAdapter;
import com.kmw.soom2.InsertActivity.Fragment.WorkThroughA;
import com.kmw.soom2.InsertActivity.Fragment.WorkThroughB;
import com.kmw.soom2.InsertActivity.Fragment.WorkThroughC;
import com.kmw.soom2.InsertActivity.Fragment.WorkThroughD;
import com.kmw.soom2.InsertActivity.Fragment.WorkThroughE;
import com.kmw.soom2.InsertActivity.Fragment.WorkThroughF;
import com.kmw.soom2.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class WorkThroughActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    ViewPager viewPager;
    Button startButton;
    TabLayout tabLayout;
    ArrayList<Fragment> arrayList;
    WorkThroughViewPagerAdapter adapter;
    LinearLayout tabStip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_through);

        viewPager = (ViewPager) findViewById(R.id.view_pager_activity_work_through);
        startButton = (Button) findViewById(R.id.start_button_activity_work_through);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_activity_work_through);
        arrayList = new ArrayList<>();
        setupViewPager();
        adapter = new WorkThroughViewPagerAdapter(getSupportFragmentManager(), arrayList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabStip = ((LinearLayout)tabLayout.getChildAt(0));
        setTabLayout();

        updateFragment();
        /*tabStip.getChildAt(0).setOnTouchListener(this);
        tabStip.getChildAt(1).setOnTouchListener(this);
        tabStip.getChildAt(2).setOnTouchListener(this);
        tabStip.getChildAt(3).setOnTouchListener(this);
        tabStip.getChildAt(4).setOnTouchListener(this);*/

        startButton.setOnClickListener(this);

    }

    public void setTabLayout() {
        for (int i = 0; i < arrayList.size()-1; i++) {
            tabLayout.addTab(tabLayout.newTab());
            tabStip.getChildAt(i).setOnTouchListener(this);
        }
    }

    private void setupViewPager() {
        arrayList.add(new WorkThroughA());
        arrayList.add(new WorkThroughB());
        arrayList.add(new WorkThroughC());
        arrayList.add(new WorkThroughD());
        arrayList.add(new WorkThroughE());
        arrayList.add(new WorkThroughF());

    }

    private void updateFragment() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 5) {
                    tabLayout.setVisibility(View.GONE);
                    startButton.setText("약 추가하기");
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(WorkThroughActivity.this, MedicineSearchActivity.class);
                            i.putExtra("newbie",true);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    });
                } else {
                    tabLayout.setVisibility(View.VISIBLE);
                    startButton.setText("시작하기");
                    startButton.setOnClickListener(WorkThroughActivity.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        viewPager.setCurrentItem(5);
    }
}
