package com.kmw.soom2.Home.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.CommunityAdapter;
import com.kmw.soom2.CommunityFragmentFunc.CommunityFragment;
import com.kmw.soom2.CommunityFragmentFunc.Fragments.CommunityLeftFragment;
import com.kmw.soom2.DrugControl.Fragment.ParentFragment.DrugControlMainFragment;
import com.kmw.soom2.Home.Fragment.HomeFragment;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.Home.HomeAdapter.ContentViewPagerAdapter;
import com.kmw.soom2.MyPage.Fragment.ParentFragment.MyPageFragment;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.StaticsFragment;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    ViewPager viewPager;
    TabLayout tabLayout;
    ContentViewPagerAdapter adapter;

    Intent beforeIntent;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mainActivity = new MainActivity();
        beforeIntent = getIntent();

        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.fragment_layout_activity_main);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getFragment(position);
                if (fragment != null&& (position ==2||position ==4)) {
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (beforeIntent.hasExtra("scoreRetry")){
            if (beforeIntent.getBooleanExtra("scoreRetry",false)){
                Intent i = new Intent(MainActivity.this, AsthmaControlActivity.class);
                startActivity(i);
            }
        }
        if (beforeIntent.hasExtra("newbieBack")) {
            if (beforeIntent.getBooleanExtra("newbieBack", false)) {
                viewPager.setCurrentItem(1);
            }
        }

        if (beforeIntent.hasExtra("community")){
            viewPager.setCurrentItem(2);
        }

        if (beforeIntent.hasExtra("notice")){
            viewPager.setCurrentItem(4);
        }

        if (beforeIntent.hasExtra("medicineInsert")){
            viewPager.setCurrentItem(1);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ContentViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "HomeFragment");
        adapter.addFragment(new DrugControlMainFragment(), "DrugControlMainFragment");
        adapter.addFragment(new CommunityFragment(MainActivity.this), "CommunityFragment");
        adapter.addFragment(new StaticsFragment(), "StaticsFragment");
        adapter.addFragment(new MyPageFragment(), "MyPageFragment");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        View first_view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        ImageView first_imageView = first_view.findViewById(R.id.img_tab);
        TextView first_text = first_view.findViewById(R.id.txt_tab);
        first_imageView.setImageResource(R.drawable.home_icon);
        first_text.setText("홈");

        View second_view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        ImageView second_imageView = second_view.findViewById(R.id.img_tab);
        TextView second_text = second_view.findViewById(R.id.txt_tab);
        second_imageView.setImageResource(R.drawable.drug_icon);
        second_text.setText("약관리");

        View third_view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        ImageView third_imageView = third_view.findViewById(R.id.img_tab);
        TextView third_text = third_view.findViewById(R.id.txt_tab);
        third_imageView.setImageResource(R.drawable.comunity_icon);
        third_text.setText("커뮤니티");

        third_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                viewPager.getChildAt(2).invalidate();
            }
        });

        View fourth_view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        ImageView fourth_imageView = fourth_view.findViewById(R.id.img_tab);
        TextView fourth_text = fourth_view.findViewById(R.id.txt_tab);
        fourth_imageView.setImageResource(R.drawable.report_icon);
        fourth_text.setText("보고서");

        View fifth_view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        ImageView fifth_imageView = fifth_view.findViewById(R.id.img_tab);
        TextView fifth_text = fifth_view.findViewById(R.id.txt_tab);
        fifth_imageView.setImageResource(R.drawable.menu_icon);
        fifth_text.setText("마이페이지");

        tabLayout.getTabAt(0).setCustomView(first_view);
        tabLayout.getTabAt(1).setCustomView(second_view);
        tabLayout.getTabAt(2).setCustomView(third_view);
        tabLayout.getTabAt(3).setCustomView(fourth_view);
        tabLayout.getTabAt(4).setCustomView(fifth_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }

    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }
}
