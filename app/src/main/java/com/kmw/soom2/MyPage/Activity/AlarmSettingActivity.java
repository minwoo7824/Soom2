package com.kmw.soom2.MyPage.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.MyPage.Item.AlarmSettingItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener {
    Switch allSelectSwitch, asthmaAlarmSwitch, drugEnrollmentSwitch, noticeSwitch, likeSwitch, commentSwitch,
            commentReplySwitch;

    final String TAG = "AlarmSettingActivity";
    String response;

    AlarmSettingItem alarmSettingItem;

    TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        btnBack = findViewById(R.id.back_text_view_activity_attending_hospital);
        allSelectSwitch = (Switch) findViewById(R.id.all_select_switch_activity_alarm_setting);
        asthmaAlarmSwitch = (Switch) findViewById(R.id.asthma_control_alarm_switch_activity_alarm_setting);
        drugEnrollmentSwitch = (Switch) findViewById(R.id.drug_enrollment_switch_activity_alarm_setting);
        noticeSwitch = (Switch) findViewById(R.id.notice_switch_activity_alarm_setting);
        likeSwitch = (Switch) findViewById(R.id.like_switch_activity_alarm_setting);
        commentSwitch = (Switch) findViewById(R.id.comment_switch_activity_alarm_setting);
        commentReplySwitch = (Switch) findViewById(R.id.comment_reply_switch_activity_alarm_setting);

        new SelectAlarmSettingNetwork().execute();

        btnBack.setOnClickListener(this);
//        allSelectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                allSelectSwitch.setChecked(isChecked);
//            }
//        });
        allSelectSwitch.setOnClickListener(this);
        asthmaAlarmSwitch.setOnClickListener(this);
        drugEnrollmentSwitch.setOnClickListener(this);
        noticeSwitch.setOnClickListener(this);
        likeSwitch.setOnClickListener(this);
        commentSwitch.setOnClickListener(this);
        commentReplySwitch.setOnClickListener(this);

//        asthmaAlarmSwitch.setOnCheckedChangeListener(this);
//        drugEnrollmentSwitch.setOnCheckedChangeListener(this);
//        noticeSwitch.setOnCheckedChangeListener(this);
//        likeSwitch.setOnCheckedChangeListener(this);
//        commentSwitch.setOnCheckedChangeListener(this);
//        commentReplySwitch.setOnCheckedChangeListener(this);
    }
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()) {
//            case R.id.asthma_control_alarm_switch_activity_alarm_setting:
//                alarmSettingItem.setSymptomFlag(isChecked ? 1 : -1);
//                new UpdateAlarmInfoNetwork().execute(isChecked ? "1" : "-1", "SYMPTOM_FLAG");
//                break;
//            case R.id.drug_enrollment_switch_activity_alarm_setting:
//                alarmSettingItem.setDosingFlag(isChecked ? 1 : -1);
//                new UpdateAlarmInfoNetwork().execute(isChecked ? "1" : "-1", "DOSING_FLAG");
//                break;
//            case R.id.notice_switch_activity_alarm_setting:
//                alarmSettingItem.setNoticeFlag(isChecked ? 1 : -1);
//                new UpdateAlarmInfoNetwork().execute(isChecked ? "1" : "-1", "NOTICE_FLAG");
//                break;
//            case R.id.like_switch_activity_alarm_setting:
//                alarmSettingItem.setCommunityLikeFlag(isChecked ? 1 : -1);
//                new UpdateAlarmInfoNetwork().execute(isChecked ? "1" : "-1", "COMMUNITY_LIKE_FLAG");
//                break;
//            case R.id.comment_switch_activity_alarm_setting:
//                alarmSettingItem.setCommunityCommentFlag(isChecked ? 1 : -1);
//                new UpdateAlarmInfoNetwork().execute(isChecked ? "1" : "-1", "COMMUNITY_COMMENT_FLAG");
//                break;
//            case R.id.comment_reply_switch_activity_alarm_setting:
//                alarmSettingItem.setCommunityCommentReplyFlag(isChecked ? 1 : -1);
//                new UpdateAlarmInfoNetwork().execute(isChecked ? "1" : "-1", "COMMUNITY_COMMENT_REPLY_FLAG");
//                break;
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_text_view_activity_attending_hospital: {
                onBackPressed();
                break;
            }
            case R.id.asthma_control_alarm_switch_activity_alarm_setting:
                alarmSettingItem.setSymptomFlag(asthmaAlarmSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(asthmaAlarmSwitch.isChecked() ? "1" : "-1", "SYMPTOM_FLAG");
                break;
            case R.id.drug_enrollment_switch_activity_alarm_setting:
                alarmSettingItem.setDosingFlag(drugEnrollmentSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(drugEnrollmentSwitch.isChecked() ? "1" : "-1", "DOSING_FLAG");

                break;
            case R.id.notice_switch_activity_alarm_setting:
                alarmSettingItem.setNoticeFlag(noticeSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(noticeSwitch.isChecked() ? "1" : "-1", "NOTICE_FLAG");
                break;
            case R.id.like_switch_activity_alarm_setting:
                alarmSettingItem.setCommunityLikeFlag(likeSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(likeSwitch.isChecked() ? "1" : "-1", "COMMUNITY_LIKE_FLAG");
                break;
            case R.id.comment_switch_activity_alarm_setting:
                alarmSettingItem.setCommunityCommentFlag(commentSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(commentSwitch.isChecked() ? "1" : "-1", "COMMUNITY_COMMENT_FLAG");
                break;
            case R.id.comment_reply_switch_activity_alarm_setting:
                alarmSettingItem.setCommunityCommentReplyFlag(commentReplySwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(commentReplySwitch.isChecked() ? "1" : "-1", "COMMUNITY_COMMENT_REPLY_FLAG");
                break;
            case R.id.all_select_switch_activity_alarm_setting:
                allSelectSwitch.setChecked(allSelectSwitch.isChecked());
                setAllSwitchCheck(allSelectSwitch.isChecked());
//                alarmSettingItem.checkSettingFlag();

//                new UpdateAlarmAllInfoNetwork().execute(allSelectSwitch.isChecked() ? "1" : "-1");

                break;
        }

    }
    void setAllSwitchCheck(boolean flag) {
        asthmaAlarmSwitch.setChecked(flag);
        drugEnrollmentSwitch.setChecked(flag);
        likeSwitch.setChecked(flag);
        noticeSwitch.setChecked(flag);
        commentSwitch.setChecked(flag);
        commentReplySwitch.setChecked(flag);

        if (flag) {
            new UpdateAlarmAllInfoNetwork().execute("1");
        }else {
            new UpdateAlarmAllInfoNetwork().execute("-1");
        }
    }
    public class UpdateAlarmAllInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add("SYMPTOM_FLAG", strings[0]);
            body.add("DOSING_FLAG", strings[0]);
            body.add("NOTICE_FLAG", strings[0]);
            body.add("COMMUNITY_LIKE_FLAG", strings[0]);
            body.add("COMMUNITY_COMMENT_FLAG", strings[0]);
            body.add("COMMUNITY_COMMENT_REPLY_FLAG", strings[0]);

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateAlarmInfo(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                    Log.d(TAG, "가입 에러");
                }else {
//                    allSelectSwitch.setChecked((alarmSettingItem.checkSettingFlag() == 1));
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
    public class UpdateAlarmInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add(strings[1], strings[0]);

            Log.d(TAG," test " + strings[0] + ", " + strings[1]);

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateAlarmInfo(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                    Log.d(TAG, "가입 에러");
                }else {
                    allSelectSwitch.setChecked((alarmSettingItem.checkSettingFlag() == 1));
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
    public class SelectAlarmSettingNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectAlarmInfo(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                    Log.d(TAG, "가입 에러");
                }else {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.length() > 0) {
                        JSONObject object = jsonArray.getJSONObject(0);

                        alarmSettingItem = new AlarmSettingItem();

                        alarmSettingItem.setSymptomFlag(JsonIntIsNullCheck(object, "SYMPTOM_FLAG"));
                        alarmSettingItem.setDosingFlag(JsonIntIsNullCheck(object, "DOSING_FLAG"));
                        alarmSettingItem.setNoticeFlag(JsonIntIsNullCheck(object, "NOTICE_FLAG"));
                        alarmSettingItem.setCommunityLikeFlag(JsonIntIsNullCheck(object, "COMMUNITY_LIKE_FLAG"));
                        alarmSettingItem.setCommunityCommentFlag(JsonIntIsNullCheck(object, "COMMUNITY_COMMENT_FLAG"));
                        alarmSettingItem.setCommunityCommentReplyFlag(JsonIntIsNullCheck(object, "COMMUNITY_COMMENT_REPLY_FLAG"));

                        alarmSettingItem.checkSettingFlag();

                        asthmaAlarmSwitch.setChecked((alarmSettingItem.getSymptomFlag() == 1));
                        drugEnrollmentSwitch.setChecked((alarmSettingItem.getDosingFlag() == 1));
                        noticeSwitch.setChecked((alarmSettingItem.getNoticeFlag() == 1));
                        likeSwitch.setChecked((alarmSettingItem.getCommunityLikeFlag() == 1));
                        commentSwitch.setChecked((alarmSettingItem.getCommunityCommentFlag() == 1));
                        commentReplySwitch.setChecked((alarmSettingItem.getCommunityCommentReplyFlag() == 1));

                        allSelectSwitch.setChecked((alarmSettingItem.checkSettingFlag() == 1));
                    }



                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}
