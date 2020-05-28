package com.kmw.soom2.CommunityFragmentFunc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.CommunityAdapter;
import com.kmw.soom2.R;

public class CommunityTagSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "CommunityTagSearchActivity";
    TextView txtBack;
    ListView listView;

    Intent beforeIntent;

    CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_tag_search);

        beforeIntent = getIntent();

        FindViewById();

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_community_tag_search_back);
        listView = (ListView)findViewById(R.id.list_view_community_tag_search);

        txtBack.setOnClickListener(this);

        adapter = new CommunityAdapter(CommunityTagSearchActivity.this,null);

        for (int i = 0; i < Utils.itemsArrayList.size(); i++){
            if (Utils.itemsArrayList.get(i).getHashTag().contains(beforeIntent.getStringExtra("hashtag"))){
                adapter.addItem(Utils.itemsArrayList.get(i));
            }
        }
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_community_tag_search_back : {
                onBackPressed();
                break;
            }
        }
    }
}
