package com.kmw.soom2.MyPage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.MyPage.Adapter.PostsViewPagerAdapter;
import com.kmw.soom2.MyPage.Fragment.ChildFragment.CommentFragment;
import com.kmw.soom2.MyPage.Fragment.ChildFragment.ScrabFragment;
import com.kmw.soom2.MyPage.Fragment.ChildFragment.WriteFragment;
import com.kmw.soom2.R;
import com.google.android.material.tabs.TabLayout;

public class PostsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener , View.OnClickListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    PostsViewPagerAdapter adapter;
    TextView txtBack;
    Intent beforeIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        beforeIntent = getIntent();

        txtBack = (TextView)findViewById(R.id.back_text_view_activity_posts);
        tabLayout = (TabLayout) findViewById(R.id.posts_tab_layout_container);
        viewPager = (ViewPager) findViewById(R.id.view_pager_activity_posts);
        tabLayout.addTab(tabLayout.newTab().setText("내가 쓴글"));
        tabLayout.addTab(tabLayout.newTab().setText("댓글 단 글"));
        tabLayout.addTab(tabLayout.newTab().setText("스크랩"));
        viewPager.setAdapter(adapter);
        setupViewPager(viewPager);

        if (beforeIntent.hasExtra("paging")){
            viewPager.setCurrentItem(beforeIntent.getIntExtra("paging",0));
            tabLayout.setScrollPosition(beforeIntent.getIntExtra("paging",0),0f,true);
            tabLayout.getTabAt(beforeIntent.getIntExtra("paging",0)).select();
        }

        txtBack.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PostsViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        adapter.addFragment(new WriteFragment());
        adapter.addFragment(new CommentFragment());
        adapter.addFragment(new ScrabFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i("tag.getPosition : ","" + tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_text_view_activity_posts : {
                onBackPressed();
                break;
            }
        }
    }
}
