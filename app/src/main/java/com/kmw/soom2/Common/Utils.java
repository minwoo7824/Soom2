package com.kmw.soom2.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kmw.soom2.Common.Item.ForeignKeys;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommunityItems;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommunityLikeScrapItem;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.Items.MedicineTypeItem;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.MyPage.Item.HospitalItem;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.StaticBreathItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {

    public static SimpleDateFormat formatDD = new SimpleDateFormat("dd일");
    public static SimpleDateFormat formatYYYYMM = new SimpleDateFormat("yyyy년 MM월");
    public static SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat formatYYYYMMDD2 = new SimpleDateFormat("yyyy년 MM월 dd일");
    public static SimpleDateFormat formatHHMM = new SimpleDateFormat("a HH:mm");
    public static SimpleDateFormat formatHHMMSS = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat formatYYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat formatYYYYMMDDAHHMM = new SimpleDateFormat("yyyy-MM-dd a HH:mm");
    public static boolean CAMERA_PERMISSION ;
    public static boolean WRITE_PERMISSION ;
    public static boolean READ_PERMISSION ;
    public static boolean AUDIO_PERMISSION ;
    public static boolean LOCATION_PERMISSION ;
    public static Uri PICK_URI;
    public static String FILE_PATH;
    public static ArrayList<CommunityItems> itemsArrayList;
    public static ArrayList<CommunityLikeScrapItem> likeItemArrayList = new ArrayList<>();
    public static ArrayList<CommunityLikeScrapItem> scrapItemArrayList = new ArrayList<>();

    public static StaticBreathItems breathItem;
    public static UserItem userItem;
    public static ArrayList<ForeignKeys> linkKeys;
    public static String preferencesName = "preferences";

    public static String TOKEN;
    public static HospitalItem hospitalItem;

    public static float LAT;
    public static float LNG;
    public static String LOCATION;
    public static String SIDO_NAME;
    public static String GUNGU_NAME;
    public static int DUST;
    public static int ULTRA_DUST;
    public static ArrayList<MedicineTakingItem> MEDICINE_LIST;
    public static ArrayList<MedicineTypeItem> MEDICINE_TYPE_LIST;

    public static void StartActivity(Context context,Class nextActivity){
        Intent intent = new Intent(context,nextActivity);
        context.startActivity(intent);
    }

    public static class Server{

        public static String server = "http://103.55.190.193:8080";
//        public static String server = "http://192.168.0.7:8080";

        public static String selectBannerList() {
            String str = server + "/admin/selectBannerList.do";
            return str;
        }

        public static String selectCommunityList(){
            String str = server + "/admin/selectCommunityList.do";
            return str;
        }

        public static String selectNoticeList(){
            String str = server + "/admin/selectNoticeList.do";
            return str;
        }

        public static String selectMedicineHistoryList(){
            String str = server + "/admin/selectMedicineHistoryList.do";
            return str;
        }

        public static String selectHomeFeedList() {
            String str = server + "/admin/selectHomeFeedHistoryList.do";
            return str;
        }

        public static String updateSymptomHomeFeedHistory() {
            String str = server + "/admin/updateSymptomHomeFeedHistoryList.do";
            return str;
        }

        public static String insertSymptomHomeFeedHistory() {
            String str = server + "/admin/insertSymptomHomeFeedHistoryList.do";
            return str;
        }

        public static String deleteHomeFeedHistoryList() {
            String str = server + "/admin/deleteHomeFeedHistoryList.do";
            return str;
        }

        public static String insertPefHomeFeedHistoryList() {
            String str = server + "/admin/insertPefHomeFeedHistoryList.do";
            return str;
        }

        public static String updatePefHomeFeedHistoryList() {
            String str = server + "/admin/updatePefHomeFeedHistoryList.do";
            return str;
        }

        public static String loginProcess() {
            String str = server + "/loginProcess.do";
            return str;
        }
        public static String overlapUserId() {
            String str = server + "/common/overlapUserIdCheck.do";
            return str;
        }
        public static String overlapUserEmail() {
            String str = server + "/common/overlapUserEmailCheck.do";
            return str;
        }
        public static String overlapUserNickname() {
            String str = server + "/common/overlapUserNicknameCheck.do";
            return str;
        }
        public static String signupUser() {
            String str = server + "/common/signupUser.do";
            return str;
        }
        public static String insertAlarmSetting() {
            String str = server + "/admin/insertAlarmSetting.do";
            return str;
        }

        public static String imageUpload() {
            String str = server + "/admin/upload_img.do?StoreDir=soom2&UploadDir=app&ShowURL=//103.55.190.193/img";
            return str;
        }

        public static String insertActHomeFeedHistoryList() {
            String str = server + "/admin/insertActHomeFeedHistoryList.do";
            return str;
        }

        public static String updateActHomeFeedHistoryList() {
            String str = server + "/admin/updateActHomeFeedHistoryList.do";
            return str;
        }

        public static String insertMemoHomeFeedHistoryList() {
            String str = server + "/admin/insertMemoHomeFeedHistoryList.do";
            return str;
        }

        public static String updateMemoHomeFeedHistoryList() {
            String str = server + "/admin/updateMemoHomeFeedHistoryList.do";
            return str;
        }

        public static String insertImage() {
            String str = server + "/admin/insertImages.do";
            return str;
        }

        public static String updateImage() {
            String str = server + "/admin/updateImages.do";
            return str;
        }

        public static String insertDustHomeFeedHistoryList() {
            String str = server + "/admin/insertFeedHistory.do";
            return str;
        }

        public static String updateDustHomeFeedHistoryList() {
            String str = server + "/admin/updateDustHomeFeedHistoryList.do";
            return str;
        }

        public static String selectHospitalInfo() {
            String str = server + "/admin/selectHospitalList.do";
            return str;
        }
        public static String updateUserInfo() {
            String str = server + "/common/updateUserInfo.do";
            return str;
        }
        public static String logoutProcess() {
            String str = server + "/common/logoutProcess.do";
            return str;
        }
        public static String deleteUserInfo() {
            String str = server + "/common/deleteUserInfo.do";
            return str;
        }
        public static String selectUserInfo() {
            String str = server + "/common/selectUserInfo.do";
            return str;
        }
        public static String selectAlarmInfo() {
            String str = server + "/admin/selectAlarmSetting.do";
            return str;
        }
        public static String updateAlarmInfo() {
            String str = server + "/admin/updateAlarmSetting.do";
            return str;
        }
        public static String insertInquery() {
            String str = server + "/admin/insertInquery.do";
            return str;
        }
        public static String insertHospital() {
            String str = server + "/admin/insertHospital.do";
            return str;
        }
        public static String updateHospital() {
            String str = server + "/admin/updateHospital.do";
            return str;
        }
        public static String selectPushAlarmList() {
            String str = server + "/admin/selectPushAlarmList.do";
            return str;
        }
        public static String selectForeignKeyList() {
            String str = server + "/admin/selectForeignLinkList.do";
            return str;
        }

        public static String selectMedicineList(){
            String str = server + "/admin/selectMedicineList.do";
            return str;
        }

        public static String insertCommunity() {
            String str = server + "/admin/insertCommunity.do";
            return str;
        }
        public static String updateCommunity() {
            String str = server + "/admin/updateCommunity.do";
            return str;
        }

        public static String deleteCommunity() {
            String str = server + "/admin/deleteCommunity.do";
            return str;
        }

        public static String selectCommunityLikeList() {
            String str = server + "/admin/selectCommunityLikeList.do";
            return str;
        }

        public static String selectCommunityScrapList() {
            String str = server + "/admin/selectCommunityScrapList.do";
            return str;
        }

        public static String selectCommunityCommentList() {
            String str = server + "/admin/selectCommunityCommentList.do";
            return str;
        }

        public static String insertCommunityComment() {
            String str = server + "/admin/insertCommunityComment.do";
            return str;
        }

        public static String insertCommunityCommentReply() {
            String str = server + "/admin/insertCommunityCommentReply.do";
            return str;
        }

        public static String deleteCommunityComment() {
            String str = server + "/admin/deleteCommunityComment.do";
            return str;
        }

        public static String deleteCommunityCommentReply() {
            String str = server + "/admin/deleteCommunityCommentReply.do";
            return str;
        }

        public static String updateCommunityComment() {
            String str = server + "/admin/updateCommunityComment.do";
            return str;
        }

        public static String updateCommunityCommentReply() {
            String str = server + "/admin/updateCommunityCommentReply.do";
            return str;
        }

        public static String insertCommunityLike() {
            String str = server + "/admin/insertCommunityLike.do";
            return str;
        }

        public static String deleteCommunityLike() {
            String str = server + "/admin/deleteCommunityLike.do";
            return str;
        }

        public static String selectMedicineReviewList() {
            String str = server + "/admin/selectMedicineReviewList.do";
            return str;
        }

        public static String insertMedicineReview() {
            String str = server + "/admin/insertMedicineReview.do";
            return str;
        }

        public static String updateMedicineReview() {
            String str = server + "/admin/updateMedicineReview.do";
            return str;
        }

        public static String deleteMedicineReview() {
            String str = server + "/admin/deleteMedicineReview.do";
            return str;
        }

        public static String insertMedicineHomeFeedHistoryList() {
            String str = server + "/admin/insertMedicineHomeFeedHistoryList.do";
            return str;
        }
        public static String insertMedicineHistory(){
            String str = server + "/admin/insertMedicineHistory.do";
            return str;
        }
        public static String updateMedicineHistory(){
            String str = server + "/admin/updateMedicineHistory.do";
            return str;
        }

        public static String deleteMedicineHistory(){
            String str = server + "/admin/deleteMedicineHistory.do";
            return str;
        }

        public static String selectMedicineType(){
            String str = server + "/admin/selectMedicineType.do";
            return str;
        }

        public static String insertMedicineApplication(){
            String str = server + "/admin/insertMedicineApplication.do";
            return str;
        }

        public static String findUserId(){
            String str = server + "/common/findUserId.do";
            return str;
        }

        public static String sendUserPasswordToEmail(){
            String str = server + "/common/sendUserPasswordToEmail.do";
            return str;
        }
    }

    public static void logLargeString(String str) {
        if (str.length() > 3000) {
            Log.i("e", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("e", str); // continuation
        }
    }

    public static String POST(OkHttpClient client, String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void Expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void Collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void OneBtnPopup(Context context,String title, String contents, String btnText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.one_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_one_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_one_btn_popup_contents);
        Button btnOk = (Button)layout.findViewById(R.id.btn_one_btn_popup_ok);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnOk.setText(btnText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dateTimeDialog.getWindow();

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

    }

    public static Button TwoBtnPopup(Context context,String title, String contents, String btnLeftText, String btnRightText){

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
                
            }
        });

        return btnRight;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static long calDateBetweenAandB(String startDt, String endDt)
    {
        String date1 = endDt;
        String date2 = startDt;

        try{ // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = format.parse(date1);
            Date SecondDate = format.parse(date2);

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            long calDateDays = calDate / ( 24*60*60*1000);

            calDateDays = Math.abs(calDateDays);

            System.out.println("두 날짜의 날짜 차이: "+calDateDays);

            return calDateDays;
        }
        catch(ParseException e)
        {
            // 예외 처리
        }

        return 0;
    }

    public static void setReadMore(final TextView view, final String text, final int maxLine) {
        final Context context = view.getContext();
        final String expanedText = "더보기";

        if (view.getTag() != null && view.getTag().equals(text)) { //Tag로 전값 의 text를 비교하여똑같으면 실행하지 않음.
            return;
        }
        view.setTag(text); //Tag에 text 저장
        view.setText(text); // setText를 미리 하셔야  getLineCount()를 호출가능
        view.post(new Runnable() { //getLineCount()는 UI 백그라운드에서만 가져올수 있음
            @Override
            public void run() {
                if (view.getLineCount() >= maxLine) { //Line Count가 설정한 MaxLine의 값보다 크다면 처리시작

                    int lineEndIndex = view.getLayout().getLineVisibleEnd(maxLine - 1); //Max Line 까지의 text length

                    String[] split = text.split("\n"); //text를 자름
                    int splitLength = 0;

                    String lessText = "";
                    for (String item : split) {
                        splitLength += item.length() + 1;
                        if (splitLength >= lineEndIndex) { //마지막 줄일때!
                            if (item.length() >= expanedText.length()) {
                                lessText += item.substring(0, item.length() - (expanedText.length())) + "\n\n" + expanedText;
                            } else {
                                lessText += item + "\n\n"  + expanedText;
                            }
                            break; //종료
                        }
                        lessText += item + "\n";
                    }
                    SpannableString spannableString = new SpannableString(lessText);
                    spannableString.setSpan(new ClickableSpan() {//클릭이벤트
                        @Override
                        public void onClick(View v) {
                            view.setText(text);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) { //컬러 처리
                            ds.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        }
                    }, spannableString.length() - expanedText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.setText(spannableString);
                    view.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        });
    }

    public static String JsonIsNullCheck(JSONObject object, String value){
        try{
            if (object.isNull(value)){
                return "";
            }else{
                return object.getString(value);
            }
        }catch (JSONException e){

        }
        return null;
    }

    public static int JsonIntIsNullCheck(JSONObject object, String value){
        try{
            if (object.isNull(value)){
                return 0;
            }else{
                return object.getInt(value);
            }
        }catch (JSONException e){

        }
        return 0;
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
