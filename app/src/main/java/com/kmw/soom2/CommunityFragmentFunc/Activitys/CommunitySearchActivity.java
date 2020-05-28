package com.kmw.soom2.CommunityFragmentFunc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.CommunityAdapter;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommunityItems;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class CommunitySearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "CommunitySearchActivity";
    TextView txtBack;
    EditText edtSearch;
    LinearLayout linTagVisible,linTagParent;
    ListView listView;

    String[] tagList = new String[]{"#증상","#흡입기","#관리법","#병원","#코로나","#약","#질문","#일상","#N212332"};

    CommunityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_search);

        FindViewById();

//        new SelectCommunityListNetWork().execute(edtSearch.getText().toString());

        for (int i = 0; i < tagList.length; i++){
            UnSelectTagList(tagList[i]);
        }
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_community_search_back);
        edtSearch = (EditText)findViewById(R.id.edt_community_search);
        linTagVisible = (LinearLayout)findViewById(R.id.lin_community_search_category);
        linTagParent = (LinearLayout)findViewById(R.id.lin_community_search_category_parent);
        listView = (ListView)findViewById(R.id.list_view_community_search);

        txtBack.setOnClickListener(this);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {

                    case EditorInfo.IME_ACTION_DONE:
                        if (edtSearch.getText().length() != 0) {
                            linTagVisible.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);

                            adapter = new CommunityAdapter(CommunitySearchActivity.this,null);

                            for (int i = 0; i < Utils.itemsArrayList.size(); i++){
                                if (Utils.itemsArrayList.get(i).getHashTag().contains(edtSearch.getText().toString().toUpperCase())){
                                    adapter.addItem(Utils.itemsArrayList.get(i));
                                }
                            }
                            listView.setAdapter(adapter);
                        }else{
                            linTagVisible.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    void UnSelectTagList(final String name){
        View listView = new View(getApplicationContext());
        listView = getLayoutInflater().inflate(R.layout.view_un_select_tag_list_item,null);
        final TextView txtTag = (TextView)listView.findViewById(R.id.txt_un_select_tag_list_item);
        final LinearLayout linearTag = (LinearLayout)listView.findViewById(R.id.linear_un_select_tag_list_item);

        txtTag.setText(name);

        linTagParent.addView(listView);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommunitySearchActivity.this,CommunityTagSearchActivity.class);
                if (name.contains("#")){
                    i.putExtra("hashtag",name.replace("#",""));
                }else{
                    i.putExtra("hashtag",name);
                }
                startActivity(i);
            }
        });
    }

    public class SelectCommunityListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", Utils.Server.selectCommunityList());
            http.addOrReplace("Search_ShowCNT",10);
            http.addOrReplace("Search_Page",1);
            http.addOrReplace("HASHTAG",strings[0]);
            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            Log.i(TAG, "SelectCommunityListNetWork check : " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                        CommunityItems communityItems = new CommunityItems();
                        communityItems.setNo(JsonIsNullCheck(object,"COMMUNITY_NO"));
                        communityItems.setProfile(JsonIsNullCheck(object,"PROFILE_IMG"));
                        communityItems.setName(JsonIsNullCheck(object,"NICKNAME"));
                        if (JsonIsNullCheck(object,"UPDATE_DT").length() == 0){
                            communityItems.setDate(JsonIsNullCheck(object,"CREATE_DT"));
                        }else{
                            communityItems.setDate(JsonIsNullCheck(object,"UPDATE_DT"));
                        }
                        communityItems.setImgListPath(JsonIsNullCheck(object,"IMAGE_FILE"));
                        communityItems.setContents(JsonIsNullCheck(object,"CONTENTS"));
                        communityItems.setHashTag(JsonIsNullCheck(object,"HASHTAG"));
                        communityItems.setLikeCnt(JsonIntIsNullCheck(object,"LIKE_CNT"));
                        communityItems.setCommentCnt(JsonIntIsNullCheck(object,"COMMENT_CNT"));
                        communityItems.setLv(JsonIsNullCheck(object,"LV"));
                        communityItems.setUserNo(JsonIsNullCheck(object,"USER_NO"));

                        adapter.addItem(communityItems);
                    }
                }

                listView.setAdapter(adapter);

            }catch (JSONException e){

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_community_search_back : {
                onBackPressed();
                break;
            }
        }
    }
}
