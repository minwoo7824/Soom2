package com.kmw.soom2.CommunityFragmentFunc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunitySearchActivity;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunityWriteActivity;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.CommunityFragmentAdapter;
import com.kmw.soom2.CommunityFragmentFunc.Fragments.CommunityLeftFragment;
import com.kmw.soom2.CommunityFragmentFunc.Fragments.CommunityRightFragment;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommunityItems;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommunityLikeScrapItem;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.itemsArrayList;
import static com.kmw.soom2.Common.Utils.likeItemArrayList;
import static com.kmw.soom2.Common.Utils.scrapItemArrayList;


public class CommunityFragment extends Fragment implements View.OnClickListener {

    private String TAG = "CommunityFragment";
    ImageView imgCommunityWrite,imgCommunitySearch,imgAlarm;
    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;
    public CommunityFragmentAdapter adapter;
    ProgressDialog progressDialog;
    public static boolean firstCheck = false;

    public CommunityFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void sendGetRequest(){
        new SelectLikeListNetWork().execute(""+ Utils.userItem.getUserNo());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community, container, false);
        v.setTag("communityF");
        tabLayout = (TabLayout) v.findViewById(R.id.tab_community);
        imgCommunityWrite = (ImageView)v.findViewById(R.id.img_community_write);
        imgCommunitySearch = (ImageView)v.findViewById(R.id.img_community_search);
        imgAlarm = (ImageView)v.findViewById(R.id.img_community_alarm);

        tabLayout.addTab(tabLayout.newTab().setText("전체보기"));
        tabLayout.addTab(tabLayout.newTab().setText("공지사항"));
        viewPager = (ViewPager) v.findViewById(R.id.view_pager_community);
        viewPager.setAdapter(adapter);

        imgCommunityWrite.setOnClickListener(this);
        imgCommunitySearch.setOnClickListener(this);
        imgAlarm.setOnClickListener(this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override

            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return v;
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new CommunityFragmentAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        adapter.addFragment(new CommunityLeftFragment(),"CommunityLeftFragment");
        adapter.addFragment(new CommunityRightFragment(),"CommunityRightFragment");
        viewPager.setAdapter(adapter);
    }

    public class SelectCommunityListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", Utils.Server.selectCommunityList());
            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            Log.i(TAG, "SelectCommunityListNetWork check : " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            itemsArrayList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                int size = jsonArray.length()-1;

                for (int i = size; i >= 0; i--){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Log.i(TAG,"tag : " + JsonIsNullCheck(object,"IMAGE_FILE"));
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
                        communityItems.setContents(JsonIsNullCheck(object,"CONTENTS").replace("<br>","\n"));
                        communityItems.setHashTag(JsonIsNullCheck(object,"HASHTAG"));
                        communityItems.setLikeCnt(JsonIntIsNullCheck(object,"LIKE_CNT"));
                        communityItems.setCommentCnt(JsonIntIsNullCheck(object,"COMMENT_CNT"));
                        communityItems.setLv(JsonIsNullCheck(object,"LV"));
                        communityItems.setUserNo(JsonIsNullCheck(object,"USER_NO"));

                        itemsArrayList.add(communityItems);
                    }
                }

                Collections.sort(itemsArrayList, new Comparator<CommunityItems>() {
                    @Override
                    public int compare(CommunityItems o1, CommunityItems o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });

                setupViewPager(viewPager);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());

            }catch (JSONException e){

            }
            progressDialog.dismiss();
        }
    }

    public class SelectLikeListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG,"userNo : " + strings[0]);

            RequestBody body = new FormBody.Builder().add("USER_NO",strings[0]).build();

            OkHttpClient client = new OkHttpClient();

            try {
                response = Utils.POST(client, Utils.Server.selectCommunityLikeList(), body);

                Log.d("Like Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            likeItemArrayList = new ArrayList<>();
            scrapItemArrayList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("list");

                Log.i(TAG,"jsonArray : " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (JsonIntIsNullCheck(object,"FLAG") == 1){
                        CommunityLikeScrapItem communityLikeScrapItem = new CommunityLikeScrapItem();
                        communityLikeScrapItem.setFlag(1);
                        communityLikeScrapItem.setCommunityNo(JsonIsNullCheck(object,"COMMUNITY_NO"));
                        likeItemArrayList.add(communityLikeScrapItem);
                    }else if (JsonIntIsNullCheck(object,"FLAG") == 2){
                        CommunityLikeScrapItem communityLikeScrapItem = new CommunityLikeScrapItem();
                        communityLikeScrapItem.setFlag(2);
                        communityLikeScrapItem.setCommunityNo(JsonIsNullCheck(object,"COMMUNITY_NO"));
                        scrapItemArrayList.add(communityLikeScrapItem);
                    }
                }
            }catch (JSONException e){

            }

            new SelectCommunityListNetWork().execute();
        }
    }

    public static void TwoBtnPopup(final String no, final Context context, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dateTimeDialog.getWindow();

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
                //게시물 삭제
                new DeleteCommunityNetWork().execute(no);
            }
        });
    }

    public static void Thirdpopup(final Context context, final String no){
        final Dialog dialog = new BottomSheetDialog(context, R.style.SheetDialog);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_comment_third_btn,null);

        TextView txtRemove = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_remove);
        TextView txtEdit = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_edit);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_cancel);

        dialog.setContentView(contentView);

        txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TwoBtnPopup(no,context,"삭제하기","게시물을 삭제하시겠습니까?","취소","삭제");
            }
        });

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(context, CommunityWriteActivity.class);
                i.putExtra("communityNo",no);
                ((MainActivity)context).startActivityForResult(i,1111);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void Twopopup(final Context context){
        final Dialog dialog = new BottomSheetDialog(context, R.style.SheetDialog);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_comment_two_btn,null);

        TextView txtDeclaration = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_declaration);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_cancel);

        dialog.setContentView(contentView);

        txtDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(context, InquiryActivity.class);
                i.putExtra("inquiry","inquiry");
                context.startActivity(i);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static String response = "";

    public static class DeleteCommunityNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO",strings[0])
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteCommunity(), body);

                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    //좋아요 스크랩 추가
    public static class InsertCommunityLikeNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO",strings[0])
                    .add("USER_NO",""+ Utils.userItem.getUserNo())
                    .add("FLAG",strings[1])
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertCommunityLike(), body);

                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    //좋아요 스크랩 삭제
    public static class DeleteCommunityLikeNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO",strings[0])
                    .add("USER_NO",""+ Utils.userItem.getUserNo())
                    .add("FLAG",strings[1])
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteCommunityLike(), body);

                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_community_write : {
                Intent i = new Intent(getActivity(), CommunityWriteActivity.class);
                startActivityForResult(i,1111);
                break;
            }
            case R.id.img_community_search : {
                Intent i = new Intent(getActivity(), CommunitySearchActivity.class);
                startActivity(i);
                break;
            }
            case R.id.img_community_alarm : {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111){
            if (resultCode == RESULT_OK){

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");

        new SelectLikeListNetWork().execute(""+ Utils.userItem.getUserNo());
    }
}
