package com.kmw.soom2.MyPage.Fragment.ChildFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.CommunityAdapter;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommunityItems;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class CommentFragment extends Fragment {

    private String TAG = "CommentFragment";
    ListView listView;
    CommunityAdapter adapter;

    public CommentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        listView = (ListView)view.findViewById(R.id.list_view_my_page_comment);

        adapter = new CommunityAdapter(getActivity(),this);

        new SelectCommunityCommentListNetWork().execute();

        return view;
    }

    String response = "";

    public class SelectCommunityCommentListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo())).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectCommunityCommentList(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                Log.i(TAG,"list : " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    Log.i(TAG,"live : i" + i + " " + JsonIntIsNullCheck(object,"ALIVE_FLAG"));

//                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){

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
                        communityItems.setContents(JsonIsNullCheck(object,"CONTENTS").replace("<br>","\n"));
                        communityItems.setHashTag(JsonIsNullCheck(object,"HASHTAG"));
                        communityItems.setLikeCnt(JsonIntIsNullCheck(object,"LIKE_CNT"));
                        communityItems.setCommentCnt(JsonIntIsNullCheck(object,"COMMENT_CNT"));
                        communityItems.setLv(JsonIsNullCheck(object,"LV"));
                        communityItems.setUserNo(JsonIsNullCheck(object,"USER_NO"));

                        adapter.addItem(communityItems);
//                    }
                }

                listView.setAdapter(adapter);

            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}
