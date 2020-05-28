package com.kmw.soom2.CommunityFragmentFunc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommentItem;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class CommunityDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "CommunityDetailActivity";
    TextView txtBack;
    LinearLayout linCommentParent,linCommentNo;
    EditText edtComment;
    Button btnCommentSend;
    Intent beforeIntent;
    String selectCommentNo = "";
    ScrollView scrollView;
    InputMethodManager imm;
    int status = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);

        beforeIntent = getIntent();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        FindViewById();

        testStack();
        Log.i(TAG,"communityNo : " + beforeIntent.getStringExtra("communityNo"));
        new SelectCommentListNetWork().execute(beforeIntent.getStringExtra("communityNo"));

    }

    void testStack(){
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
          List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(100);
           for( int i=0; i < taskList.size(); i++){
               Log.d("INFO","base="+taskList.get(i).baseActivity.getPackageName()+",top="+taskList.get(i).topActivity.getPackageName());
                Log.d("INFO","base="+taskList.get(i).baseActivity.getClassName()+",top="+taskList.get(i).topActivity.getClassName());
         }
    }
    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_community_detail_back);
        linCommentParent = (LinearLayout) findViewById(R.id.lin_community_detail_comment_parent);
        linCommentNo = (LinearLayout) findViewById(R.id.lin_community_detail_comment_no);
        edtComment = (EditText) findViewById(R.id.edt_community_detail_comment);
        btnCommentSend = (Button) findViewById(R.id.btn_community_detail_comment_send);
        scrollView = (ScrollView) findViewById(R.id.src_community_detail);

        txtBack.setOnClickListener(this);
        btnCommentSend.setOnClickListener(this);
        scrollView.setOnClickListener(this);
        linCommentParent.setOnClickListener(this);

        edtComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    selectCommentNo = "";
                }
            }
        });

    }

    void CommentView(final CommentItem commentItem, ArrayList<CommentItem> reComment){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_comment_list_item,null);

        ImageView imgProfile = (ImageView)listView.findViewById(R.id.img_comment_list_item_profile);
        TextView txtNickname = (TextView)listView.findViewById(R.id.txt_comment_list_item_name);
        ImageView imgMore = (ImageView)listView.findViewById(R.id.img_comment_list_item_more);
        TextView txtContents = (TextView)listView.findViewById(R.id.txt_comment_list_item_comment);
        TextView txtDate = (TextView)listView.findViewById(R.id.txt_comment_list_item_date);
        TextView txtReComment = (TextView)listView.findViewById(R.id.txt_comment_list_item_recomment);
        LinearLayout linRecommentParent = (LinearLayout)listView.findViewById(R.id.lin_comment_list_item_parent);

        txtNickname.setText(commentItem.getNickname());
        txtContents.setText(commentItem.getContents());
        txtDate.setText(commentItem.getDate());

        RequestOptions requestOptions = new RequestOptions();

        if (commentItem.getProfilePath().length() > 0){
            if (commentItem.getProfilePath().contains("http:")){
                Glide.with(CommunityDetailActivity.this).load(commentItem.getProfilePath()).apply(requestOptions.circleCrop()).into(imgProfile);
            }else{
                Glide.with(CommunityDetailActivity.this).load("http:" + commentItem.getProfilePath()).apply(requestOptions.circleCrop()).into(imgProfile);
            }
        }

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentItem.getUserNo().equals(""+Utils.userItem.getUserNo())){
                    Thirdpopup(2,commentItem.getCommentNo(),commentItem.getContents());
                }else{
                    Twopopup(2,commentItem.getCommentNo());
                }
            }
        });

        txtReComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCommentNo = commentItem.getCommentNo();
                edtComment.setText("");
                edtComment.setFocusableInTouchMode(true);
                edtComment.requestFocus();
                imm.showSoftInput(edtComment, 0);
            }
        });

        for (int i = 0; i < reComment.size(); i++){
            if (reComment.get(i).getCommentNo().equals(commentItem.getCommentNo())){
                RecommentView(linRecommentParent,reComment.get(i));
            }
        }

        linCommentParent.addView(listView);
    }

    void RecommentView(LinearLayout linParent, final CommentItem commentItem){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_comment_list_item,null);

        ImageView imgProfile = (ImageView)listView.findViewById(R.id.img_comment_list_item_profile);
        TextView txtNickname = (TextView)listView.findViewById(R.id.txt_comment_list_item_name);
        ImageView imgMore = (ImageView)listView.findViewById(R.id.img_comment_list_item_more);
        TextView txtContents = (TextView)listView.findViewById(R.id.txt_comment_list_item_comment);
        TextView txtDate = (TextView)listView.findViewById(R.id.txt_comment_list_item_date);
        TextView txtReComment = (TextView)listView.findViewById(R.id.txt_comment_list_item_recomment);

        txtReComment.setVisibility(View.GONE);

        txtNickname.setText(commentItem.getNickname());
        txtContents.setText(commentItem.getContents());
        txtDate.setText(commentItem.getDate());

        RequestOptions requestOptions = new RequestOptions();

        if (commentItem.getProfilePath().length() > 0){
            if (commentItem.getProfilePath().contains("http:")){
                Glide.with(CommunityDetailActivity.this).load(commentItem.getProfilePath()).apply(requestOptions.circleCrop()).into(imgProfile);
            }else{
                Glide.with(CommunityDetailActivity.this).load("http:" + commentItem.getProfilePath()).apply(requestOptions.circleCrop()).into(imgProfile);
            }
        }

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentItem.getUserNo().equals(""+Utils.userItem.getUserNo())){
                    Thirdpopup(3,commentItem.getCommentReplyNo(),commentItem.getContents());
                }else{
                    Twopopup(3,commentItem.getCommentReplyNo());
                }
            }
        });

        linParent.addView(listView);
    }

    void Thirdpopup(final int flag, final String no, final String contents){
        final Dialog dialog = new BottomSheetDialog(CommunityDetailActivity.this,R.style.SheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_comment_third_btn,null);

        TextView txtRemove = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_remove);
        TextView txtEdit = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_edit);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_cancel);

        dialog.setContentView(contentView);

        txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                TwoBtnPopup(flag,no,CommunityDetailActivity.this,"삭제하기","댓글을 삭제하시겠습니까?","취소","삭제");
            }
        });

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                status = flag;

                selectCommentNo = no;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edtComment.setText(contents);
                        edtComment.setSelection(contents.length());
                        edtComment.setFocusableInTouchMode(true);
                        edtComment.requestFocus();
                        imm.showSoftInput(edtComment, 0);
                    }
                },500);
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

    void Twopopup(final int flag, final String no){
        final Dialog dialog = new BottomSheetDialog(CommunityDetailActivity.this,R.style.SheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_comment_two_btn,null);

        TextView txtDeclaration = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_declaration);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_cancel);

        dialog.setContentView(contentView);

        txtDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(CommunityDetailActivity.this, InquiryActivity.class);
                i.putExtra("inquiry","inquiry");
                startActivity(i);
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

    public void TwoBtnPopup(final int flag , final String no , final Context context, String title, String contents, String btnLeftText, String btnRightText){

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
                if (flag == 2){
                    new DeleteCommentNetWork().execute(no);
                }else{
                    new DeleteCommentReplyNetWork().execute(no);
                }
            }
        });
    }

    String response = "";

    public class SelectCommentListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO",strings[0]).build();

            OkHttpClient client = new OkHttpClient();

            try {
                response = Utils.POST(client, Utils.Server.selectCommunityCommentList(), body);

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

            linCommentParent.removeAllViews();

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0){
                    linCommentNo.setVisibility(View.GONE);
                    linCommentParent.setVisibility(View.VISIBLE);
                }else{
                    linCommentNo.setVisibility(View.VISIBLE);
                    linCommentParent.setVisibility(View.GONE);
                }

                ArrayList<CommentItem> recommentList = new ArrayList<>();

                if (!jsonObject.isNull("sublist")){
                    JSONArray jsonArrayReply = jsonObject.getJSONArray("sublist");

                    Log.i(TAG,"jsonArray : " + jsonArray.length());

                    for (int i = 0; i < jsonArrayReply.length(); i++){
                        JSONObject object = jsonArrayReply.getJSONObject(i);

                        CommentItem commentItem = new CommentItem();
                        commentItem.setCommentReplyNo(JsonIsNullCheck(object,"COMMENT_REPLY_NO"));
                        commentItem.setCommentNo(JsonIsNullCheck(object,"COMMENT_NO"));
                        commentItem.setProfilePath(JsonIsNullCheck(object,"PROFILE_IMG"));
                        commentItem.setNickname(JsonIsNullCheck(object,"NICKNAME"));
                        commentItem.setContents(JsonIsNullCheck(object,"COMMENT"));
                        commentItem.setUserNo(JsonIsNullCheck(object,"USER_NO"));
                        if (JsonIsNullCheck(object,"UPDATE_DT").length() == 0){
                            commentItem.setDate(JsonIsNullCheck(object,"CREATE_DT"));
                        }else{
                            commentItem.setDate(JsonIsNullCheck(object,"UPDATE_DT"));
                        }
                        recommentList.add(commentItem);
                    }
                }

                for (int i = 0; i < jsonArray.length(); i++){

                    JSONObject object = jsonArray.getJSONObject(i);

                    CommentItem commentItem = new CommentItem();
                    commentItem.setCommentNo(JsonIsNullCheck(object,"COMMENT_NO"));
                    commentItem.setProfilePath(JsonIsNullCheck(object,"PROFILE_IMG"));
                    commentItem.setNickname(JsonIsNullCheck(object,"NICKNAME"));
                    commentItem.setContents(JsonIsNullCheck(object,"COMMENT"));
                    commentItem.setUserNo(JsonIsNullCheck(object,"USER_NO"));
                    if (JsonIsNullCheck(object,"UPDATE_DT").length() == 0){
                        commentItem.setDate(JsonIsNullCheck(object,"CREATE_DT"));
                    }else{
                        commentItem.setDate(JsonIsNullCheck(object,"UPDATE_DT"));
                    }
                    CommentView(commentItem,recommentList);
                }



            }catch (JSONException e){

            }
        }
    }

    //댓글 등록
    public class InsertCommentNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO",strings[0])
                    .add("USER_NO",""+Utils.userItem.getUserNo())
                    .add("COMMENT",edtComment.getText().toString())
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertCommunityComment(), body);

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
            edtComment.setText("");
            selectCommentNo = "";
            imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
            txtBack.setFocusableInTouchMode(true);
            txtBack.requestFocus();
            new SelectCommentListNetWork().execute(beforeIntent.getStringExtra("communityNo"));
        }
    }

    //대댓글 등록
    public class InsertCommentReplyNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMENT_NO",strings[0])
                    .add("USER_NO",""+Utils.userItem.getUserNo())
                    .add("COMMENT",edtComment.getText().toString())
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertCommunityCommentReply(), body);

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
            edtComment.setText("");
            selectCommentNo = "";
            imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
            txtBack.setFocusableInTouchMode(true);
            txtBack.requestFocus();
            new SelectCommentListNetWork().execute(beforeIntent.getStringExtra("communityNo"));
        }
    }

    //댓글 삭제
    public class DeleteCommentNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMENT_NO",strings[0])
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteCommunityComment(), body);

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
            new SelectCommentListNetWork().execute(beforeIntent.getStringExtra("communityNo"));
        }
    }

    //대댓글 식제
    public class DeleteCommentReplyNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMENT_REPLY_NO",strings[0])
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteCommunityCommentReply(), body);

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
            new SelectCommentListNetWork().execute(beforeIntent.getStringExtra("communityNo"));
        }
    }

    //댓글 수정
    public class UpdateCommentNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMENT_NO",strings[0])
                    .add("USER_NO",""+Utils.userItem.getUserNo())
                    .add("COMMENT",edtComment.getText().toString())
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateCommunityComment(), body);

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
            edtComment.setText("");
            selectCommentNo = "";
            status = 1;
            imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
            txtBack.setFocusableInTouchMode(true);
            txtBack.requestFocus();
            new SelectCommentListNetWork().execute(beforeIntent.getStringExtra("communityNo"));
        }
    }

    //대댓글 수정
    public class UpdateCommentReplyNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMENT_REPLY_NO",strings[0])
                    .add("USER_NO",""+Utils.userItem.getUserNo())
                    .add("COMMENT",edtComment.getText().toString())
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateCommunityCommentReply(), body);

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
            edtComment.setText("");
            selectCommentNo = "";
            status = 1;
            imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
            txtBack.setFocusableInTouchMode(true);
            txtBack.requestFocus();
            new SelectCommentListNetWork().execute(beforeIntent.getStringExtra("communityNo"));
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("community",true);
        i.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_community_detail_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_community_detail_comment_send : {
                if (status == 1){
                    if (selectCommentNo.length() == 0){
                        if (edtComment.getText().length() > 0){
                            new InsertCommentNetWork().execute(beforeIntent.getStringExtra("communityNo"));
                        }
                    }else{
                        if (edtComment.getText().length() > 0){
                            new InsertCommentReplyNetWork().execute(selectCommentNo);
                        }
                    }
                }else if (status == 2){
                    if (edtComment.getText().length() > 0){
                        new UpdateCommentNetWork().execute(selectCommentNo);
                    }
                }else if (status == 3){
                    if (edtComment.getText().length() > 0){
                        new UpdateCommentReplyNetWork().execute(selectCommentNo);
                    }
                }
                Log.i(TAG,"select : " + selectCommentNo);
                break;
            }
            case R.id.src_community_detail : {
                selectCommentNo = "";
                edtComment.setText("");
                status = 1;
                scrollView.setFocusableInTouchMode(true);
                scrollView.requestFocus();
                imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
                break;
            }
            case R.id.lin_community_detail_comment_parent : {
                selectCommentNo = "";
                edtComment.setText("");
                status = 1;
                linCommentParent.setFocusableInTouchMode(true);
                linCommentParent.requestFocus();
                imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
                break;
            }
        }
    }
}
