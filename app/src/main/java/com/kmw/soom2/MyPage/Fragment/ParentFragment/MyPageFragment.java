package com.kmw.soom2.MyPage.Fragment.ParentFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.InsertActivity.InsertActivity.YKActivity;
import com.kmw.soom2.MyPage.Activity.AccountActivity;
import com.kmw.soom2.MyPage.Activity.AirPollutionInfoActivity;
import com.kmw.soom2.MyPage.Activity.AlarmSettingActivity;
import com.kmw.soom2.MyPage.Activity.AttendingHospitalActivity;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.MyPage.Activity.MypageCreateCsvActivity;
import com.kmw.soom2.MyPage.Activity.NoticeActivity;
import com.kmw.soom2.MyPage.Activity.PatientInfoActivity;
import com.kmw.soom2.MyPage.Activity.PostsActivity;
import com.kmw.soom2.MyPage.Item.HospitalItem;
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


public class MyPageFragment extends Fragment implements View.OnClickListener {
    LinearLayout writeLinearLayout,commentLinearLayout,scrabLinearLayout,userInfoLayout;
    LinearLayout hosInfoLayout,alarmSettinLayout,inquiryLayout,reportSendLayout;
    LinearLayout noticeLayout, termsLayout,appVerLayout,airPollutionInfoLayout;
    ImageView settingImageView, profileImageView, alarmImageView;

    TextView txtUserNickname, txtUserType;

    String response;
    final String TAG = "MyPageFragment";

    TextView txtWritingCnt,txtCommentCnt,txtScrapCnt;
    int writingCnt = 0, commentCnt = 0, scrapCnt = 0;

    public MyPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        writeLinearLayout = (LinearLayout) view.findViewById(R.id.write_linear_layout_fragment_my_page);
        commentLinearLayout =(LinearLayout) view.findViewById(R.id.comment_linear_layout_fragment_my_page);
        scrabLinearLayout = (LinearLayout) view.findViewById(R.id.scrap_linear_layout_fragment_my_page);
        userInfoLayout = (LinearLayout) view.findViewById(R.id.user_info_linear_layout_fragment_my_page);
        hosInfoLayout = (LinearLayout) view.findViewById(R.id.hos_info_linear_layout_fragment_my_page);
        alarmSettinLayout = (LinearLayout) view.findViewById(R.id.alarm_setting_linear_layout_fragment_my_page);
        inquiryLayout = (LinearLayout) view.findViewById(R.id.inquiry_linear_layout_fragment_my_page);
        noticeLayout = (LinearLayout) view.findViewById(R.id.notice_linear_layout_fragment_my_page);
        termsLayout = (LinearLayout) view.findViewById(R.id.terms_linear_layout_fragment_my_page);
        appVerLayout = (LinearLayout) view.findViewById(R.id.app_ver_linear_layout_fragment_my_page);
        airPollutionInfoLayout = (LinearLayout) view.findViewById(R.id.air_pollution_linear_layout_info_fragment_my_page);
        reportSendLayout = (LinearLayout) view.findViewById(R.id.report_send_linear_layout_fragment_my_page);
        settingImageView = (ImageView) view.findViewById(R.id.setting_image_view_fragment_my_page);
        //=========================
        profileImageView = view.findViewById(R.id.user_icon_image_view_fragment_my_page);
        txtUserNickname = view.findViewById(R.id.user_name_fragment_my_page);
        txtUserType = view.findViewById(R.id.user_category_fragment_my_page);
        //=========================

        txtWritingCnt = (TextView)view.findViewById(R.id.writing_count_fragment_my_page);
        txtCommentCnt = (TextView)view.findViewById(R.id.comment_count_fragment_my_page);
        txtScrapCnt = (TextView)view.findViewById(R.id.scrap_count_fragment_my_page);

        writeLinearLayout.setOnClickListener(this);
        commentLinearLayout.setOnClickListener(this);
        scrabLinearLayout.setOnClickListener(this);
        userInfoLayout.setOnClickListener(this);
        hosInfoLayout.setOnClickListener(this);
        alarmSettinLayout.setOnClickListener(this);
        inquiryLayout.setOnClickListener(this);
        noticeLayout.setOnClickListener(this);
        termsLayout.setOnClickListener(this);
        appVerLayout.setOnClickListener(this);
        airPollutionInfoLayout.setOnClickListener(this);
        reportSendLayout.setOnClickListener(this);
        settingImageView.setOnClickListener(this);
        alarmImageView = view.findViewById(R.id.alarm_fragment_my_page);

        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
//                getActivity().startActivityForResult(intent, 1111);
                startActivity(intent);
            }
        });

        new SelectHospitalInfoNetWork().execute();
        new SelectCommunityListNetWork().execute();
        new SelectCommunityCommentListNetWork().execute();
        new SelectCommunityScrapListNetWork().execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"MyPageFragmentResume");
        writingCnt = 0;
        commentCnt = 0;
        scrapCnt = 0;
        txtCommentCnt.setText(""+commentCnt);
        txtWritingCnt.setText(""+writingCnt);
        txtScrapCnt.setText(""+scrapCnt);

        new SelectHospitalInfoNetWork().execute();
        new SelectCommunityListNetWork().execute();
        new SelectCommunityCommentListNetWork().execute();
        new SelectCommunityScrapListNetWork().execute();
        txtUserNickname.setText(Utils.userItem.getNickname());
        txtUserType.setText(Utils.userItem.getLv() == 11 ? "본인" : "보호자");
        if (Utils.userItem.getProfileImg().length() != 0) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
            if (!Utils.userItem.getProfileImg().contains("http:")) {
                Glide.with(this).asBitmap().load("http:"+ Utils.userItem.getProfileImg()).apply(requestOptions).into(profileImageView);
            }else {
                Glide.with(this).asBitmap().load(Utils.userItem.getProfileImg()).apply(requestOptions).into(profileImageView);
            }
        }else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(40));
            Glide.with(this)
                    .load(R.drawable.user_icon)
                    .apply(requestOptions)
                    .into(profileImageView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.write_linear_layout_fragment_my_page:
                Intent intentWrite = new Intent(getActivity(), PostsActivity.class);
                intentWrite.putExtra("paging",0);
                startActivity(intentWrite);
                break;
            case R.id.comment_linear_layout_fragment_my_page:
                Intent intentComment = new Intent(getActivity(), PostsActivity.class);
                intentComment.putExtra("paging",1);
                startActivity(intentComment);
                break;
            case R.id.scrap_linear_layout_fragment_my_page:
                Intent intentScrap = new Intent(getActivity(), PostsActivity.class);
                intentScrap.putExtra("paging",2);
                startActivity(intentScrap);
                break;
                case R.id.user_info_linear_layout_fragment_my_page:
                Intent intentUserInfo = new Intent(getActivity(), PatientInfoActivity.class);
                startActivity(intentUserInfo);
                break;
                case R.id.hos_info_linear_layout_fragment_my_page:
                Intent intentHosInfo = new Intent(getActivity(), AttendingHospitalActivity.class);
                startActivity(intentHosInfo);
                break;
                case R.id.alarm_setting_linear_layout_fragment_my_page:
                Intent intentAlarmSetting = new Intent(getActivity(), AlarmSettingActivity.class);
                startActivity(intentAlarmSetting);
                break;
                case R.id.inquiry_linear_layout_fragment_my_page:
                Intent intentInquiry = new Intent(getActivity(), InquiryActivity.class);
                startActivity(intentInquiry);
                break;
              case R.id.notice_linear_layout_fragment_my_page:
                Intent intentNotice = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intentNotice);
                break;
                case R.id.terms_linear_layout_fragment_my_page:
                Intent intentTerms = new Intent(getActivity(), YKActivity.class);
                startActivity(intentTerms);
                break;
              /*  case R.id.app_ver_linear_layout_fragment_my_page:
                Intent intentAppVer = new Intent(getActivity(), PostsActivity.class);
                startActivity(intentAppVer);
                break;*/
                case R.id.air_pollution_linear_layout_info_fragment_my_page:
                Intent intentAirPollution = new Intent(getActivity(), AirPollutionInfoActivity.class);
                startActivity(intentAirPollution);
                break;
                case R.id.setting_image_view_fragment_my_page:
                    Intent intentSetting = new Intent(getActivity(), AccountActivity.class);
                    startActivity(intentSetting);
                break;
               case R.id.report_send_linear_layout_fragment_my_page:
                    Intent intentReportSend = new Intent(getActivity(), MypageCreateCsvActivity.class);
                    startActivity(intentReportSend);
                break;
        }
    }

    public class SelectHospitalInfoNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
                    HospitalItem hospitalItem = new HospitalItem();

                    hospitalItem.setHospitalNo(JsonIntIsNullCheck(object, "HOSPITAL_NO"));
                    hospitalItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                    hospitalItem.setName(JsonIsNullCheck(object, "NAME"));
                    hospitalItem.setAddr(JsonIsNullCheck(object, "ADDR"));
                    hospitalItem.setDepartment(JsonIsNullCheck(object, "DEPARTMENT"));
                    hospitalItem.setDoctor(JsonIsNullCheck(object, "DOCTOR"));
                    hospitalItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                    hospitalItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));

                    Utils.hospitalItem = hospitalItem;
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo())).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHospitalInfo(), body);

                Log.d(TAG + " Response", response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class SelectCommunityListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo())).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectCommunityList(), body);

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

                writingCnt = jsonArray.length();

                txtWritingCnt.setText(""+writingCnt);
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }

//    public class SelectCommunityListNetWork extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo())).build();
//
//            OkHttpClient client = new OkHttpClient();
//            try {
//                response = Utils.POST(client, Utils.Server.selectCommunityList(), body);
//
//                return response;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray jsonArray = jsonObject.getJSONArray("list");
//
//                Log.i(TAG,"list : " + jsonArray.length());
//
//                txtWritingCnt.setText(""+jsonArray.length());
//            }catch (JSONException e){
//                Log.i(TAG, e.getLocalizedMessage());
//            }
//        }
//    }

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
            System.out.println("selectCommunityCommentList : " + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                Log.i(TAG,"length, length : " +jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
//                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                        commentCnt++;
//                    }
                }

                txtCommentCnt.setText(""+commentCnt);
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }

    public class SelectCommunityScrapListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo()))
                    .add("FLAG","2").build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectCommunityScrapList(), body);

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

                Log.i(TAG,"Scrap_list : " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                        scrapCnt++;
                    }
                }

                txtScrapCnt.setText(""+scrapCnt);

            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}
