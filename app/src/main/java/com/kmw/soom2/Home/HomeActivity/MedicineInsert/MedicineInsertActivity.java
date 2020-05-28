package com.kmw.soom2.Home.HomeActivity.MedicineInsert;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.breathItem;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.DrugControl.Fragment.ParentFragment.DrugControlMainFragment.medicineTakingItems;

public class MedicineInsertActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MedicineInsertActivity";
    ImageView imgBack;
    TextView txtMedicineName;
    TextView txtSetting,txtInfo,txtReview;
    LinearLayout linSettingLine,linInfoLine,linReviewLine;
    LinearLayout linSettingVisible,linInfoVisible,linReviewVisible;
    ImageView imgMedicine;
    ImageView imgRemove;

    //setting
    EditText edtAmount;
    TextView txtUnit,txtCount,txtStartDt,txtEndDt;
    //info
    TextView txtEfficacy,txtInstructions,txtInformation,txtStability,txtPrecautions;
    //review
    LinearLayout linReviewInsert;
    LinearLayout linReviewList,linReviewNoList;
    Button btnReviewMore;


    Button btnFinish;

    Calendar calendar = Calendar.getInstance();;
    Date date = new Date(System.currentTimeMillis());

    String[] unitStrings = new String[]{"개","회","ml","g","mg","방울"};
    String[] cntStrings = new String[]{"필요시","하루 1번","하루 2번","하루 3번","하루 4번","하루 5번","하루 6번","하루 7번","하루 8번"};

    int frequency = 1;

    Intent beforeIntent;

    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");

    ArrayList<Integer> userNoList = new ArrayList<>();
    ArrayList<Integer> reviewNoList = new ArrayList<>();
    ArrayList<Integer> effectList = new ArrayList<>();
    ArrayList<String> nicknameList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> lvList = new ArrayList<>();
    ArrayList<String> periodList = new ArrayList<>();
    ArrayList<String> contentsList = new ArrayList<>();
    ArrayList<String> birthList = new ArrayList<>();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_insert);

        beforeIntent = getIntent();

        FindViewById();
        imgRemove.setVisibility(View.INVISIBLE);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("historyNo")){

                imgRemove.setVisibility(View.VISIBLE);

                txtMedicineName.setText(beforeIntent.getStringExtra("name"));
                edtAmount.setText(""+beforeIntent.getIntExtra("volume",0));
                txtUnit.setText(beforeIntent.getStringExtra("unit"));

                frequency = beforeIntent.getIntExtra("frequency",0);

                if (beforeIntent.getIntExtra("frequency",0) == -1){
                    txtCount.setText(cntStrings[0]);
                }else{
                    txtCount.setText(cntStrings[beforeIntent.getIntExtra("frequency",0)]);
                }

                try {
                    txtStartDt.setText(dateFormat2.format(dateFormat1.parse(beforeIntent.getStringExtra("startDt"))));
                    txtEndDt.setText(dateFormat2.format(dateFormat1.parse(beforeIntent.getStringExtra("endDt"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (beforeIntent.getStringExtra("medicineImg").length() > 0){
                    Glide.with(MedicineInsertActivity.this).load("http:"+beforeIntent.getStringExtra("medicineImg")).into(imgMedicine);
                }

                txtEfficacy.setText(beforeIntent.getStringExtra("efficacy"));
                txtInstructions.setText(beforeIntent.getStringExtra("instructions"));
                txtInformation.setText(beforeIntent.getStringExtra("information"));
                txtStability.setText(beforeIntent.getStringExtra("stabilityRating"));
                txtPrecautions.setText(beforeIntent.getStringExtra("precautions"));

            }else if (beforeIntent.hasExtra("medicineNo")){
                txtMedicineName.setText(beforeIntent.getStringExtra("medicineKo"));
                Log.i(TAG,"medicineImg : " + beforeIntent.getStringExtra("medicineImg"));
                if (beforeIntent.getStringExtra("medicineImg").length() > 0){
                    Glide.with(MedicineInsertActivity.this).load("http:"+beforeIntent.getStringExtra("medicineImg")).into(imgMedicine);
                }
                txtEfficacy.setText(beforeIntent.getStringExtra("efficacy"));
                txtInstructions.setText(beforeIntent.getStringExtra("instructions"));
                txtInformation.setText(beforeIntent.getStringExtra("information"));
                txtStability.setText(beforeIntent.getStringExtra("stabilityRating"));
                txtPrecautions.setText(beforeIntent.getStringExtra("precautions"));
            }
        }
    }

    void FindViewById(){
        imgBack = (ImageView) findViewById(R.id.img_medicine_insert_back);
        txtMedicineName = (TextView)findViewById(R.id.txt_medicine_insert_title);
        txtSetting = (TextView)findViewById(R.id.txt_medicine_insert_first);
        txtInfo = (TextView)findViewById(R.id.txt_medicine_insert_second);
        txtReview = (TextView)findViewById(R.id.txt_medicine_insert_third);
        linSettingLine = (LinearLayout)findViewById(R.id.lin_medicine_insert_line_first);
        linInfoLine = (LinearLayout)findViewById(R.id.lin_medicine_insert_line_second);
        linReviewLine = (LinearLayout)findViewById(R.id.lin_medicine_insert_line_third);
        linSettingVisible = (LinearLayout)findViewById(R.id.lin_medicine_insert_first);
        linInfoVisible = (LinearLayout)findViewById(R.id.lin_medicine_insert_second);
        linReviewVisible = (LinearLayout)findViewById(R.id.lin_medicine_insert_third);
        imgMedicine = (ImageView)findViewById(R.id.img_medicine_insert_img);
        imgRemove = (ImageView)findViewById(R.id.img_medicine_insert_remove);

        //setting
        edtAmount = (EditText)findViewById(R.id.edt_medicine_insert_first_amount);
        txtUnit = (TextView)findViewById(R.id.txt_medicine_insert_first_unit);
        txtCount = (TextView)findViewById(R.id.txt_medicine_insert_first_cnt);
        txtStartDt = (TextView)findViewById(R.id.txt_medicine_insert_first_start_dt);
        txtEndDt = (TextView)findViewById(R.id.txt_medicine_insert_first_end_dt);
        //info
        txtEfficacy     = (TextView)findViewById(R.id.txt_medicine_insert_second_efficacy);
        txtInstructions = (TextView)findViewById(R.id.txt_medicine_insert_second_instructions);
        txtInformation  = (TextView)findViewById(R.id.txt_medicine_insert_second_information);
        txtStability    = (TextView)findViewById(R.id.txt_medicine_insert_second_stability);
        txtPrecautions  = (TextView)findViewById(R.id.txt_medicine_insert_second_precautions);
        //review
        linReviewInsert = (LinearLayout)findViewById(R.id.lin_medicine_insert_third_review_insert);
        linReviewList = (LinearLayout)findViewById(R.id.lin_medicine_insert_third_review_list_parent);
        linReviewNoList = (LinearLayout)findViewById(R.id.lin_medicine_insert_third_review_no_list);
        btnReviewMore = ( Button)findViewById(R.id.btn_medicine_insert_review_more);

        btnFinish = (Button)findViewById(R.id.btn_medicine_insert_finish);

        imgBack.setOnClickListener(this);
        txtSetting.setOnClickListener(this);
        txtInfo.setOnClickListener(this);
        txtReview.setOnClickListener(this);
        imgRemove.setOnClickListener(this);

        //setting
        txtUnit.setOnClickListener(this);
        txtCount.setOnClickListener(this);
        txtStartDt.setOnClickListener(this);
        txtEndDt.setOnClickListener(this);
        //review
        linReviewInsert.setOnClickListener(this);
        btnReviewMore.setOnClickListener(this);

        txtCount.setText(cntStrings[1]);

        btnFinish.setOnClickListener(this);

    }

    void DateTimePicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(MedicineInsertActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false);

        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                if (flag == 1){
                    txtStartDt.setText(formatYYYYMMDD.format(date));
                }else{
                    if (Integer.parseInt(txtStartDt.getText().toString().replace("-","")) >= Integer.parseInt(formatYYYYMMDD.format(date).replace("-",""))){
                        OneBtnPopup(1,MedicineInsertActivity.this,"복약기간 오류","시작일 이후로 설정해주세요.","확인");
                    }else{
                        txtEndDt.setText(formatYYYYMMDD.format(date));
                    }
                }
                dateTimeDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateTimeDialog.dismiss();
            }
        });
    }

    void DayCntPicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.dialog_number_picker,null);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_dialog_number_picker_title);
        Button btnCancel = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_cancel);
        Button btnDone = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_done);
        final NumberPicker numberPicker = (NumberPicker)dateTimeView.findViewById(R.id.number_picker);
        final Dialog dateTimeDialog = new Dialog(MedicineInsertActivity.this);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        numberPicker.setWrapSelectorWheel(false);

        txtTitle.setText(title);

        numberPicker.setMinValue(0);

        if (flag == 1){

            numberPicker.setMaxValue(unitStrings.length-1);
            numberPicker.setDisplayedValues(unitStrings);

            for (int i = 0; i < unitStrings.length; i++){
                if (unitStrings[i].equals(txtUnit.getText().toString())){
                    numberPicker.setValue(i);
                }
            }
        }else{

            numberPicker.setMaxValue(cntStrings.length-1);
            numberPicker.setDisplayedValues(cntStrings);

            for (int i = 0; i < cntStrings.length; i++){
                if (cntStrings[i].equals(txtCount.getText().toString())){
                    numberPicker.setValue(i);
                }
            }
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = numberPicker.getValue();
                if (flag == 1){
                    txtUnit.setText(unitStrings[idx]);
                }else{
                    txtCount.setText(cntStrings[idx]);
                    if (idx == 0){
                        frequency = -1;
                    }else{
                        frequency = idx;
                    }
                }
                dateTimeDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });
    }

    String[] staticsList = new String[]{"전체","기침","천명음","호흡곤란","가슴답답함"};

    void StaticPicker(String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.dialog_number_picker,null);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_dialog_number_picker_title);
        Button btnCancel = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_cancel);
        Button btnDone = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_done);
        final NumberPicker numberPicker = (NumberPicker)dateTimeView.findViewById(R.id.number_picker);
        final Dialog dateTimeDialog = new Dialog(MedicineInsertActivity.this);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        numberPicker.setWrapSelectorWheel(false);

        txtTitle.setText(title);

        numberPicker.setMinValue(0);

        numberPicker.setMaxValue(staticsList.length-1);
        numberPicker.setDisplayedValues(staticsList);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = numberPicker.getValue();

                txtCount.setText(staticsList[idx]);

                dateTimeDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });
    }

    void ReviewList(final int userNo, final int reviewNo, String name, String date, String lv, final String contents, final int sideEffectFlag, final String dosePeriod, String birth){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_review_list_item,null);

        TextView txtName = (TextView)listView.findViewById(R.id.txt_review_list_item_name);
        TextView txtDate = (TextView)listView.findViewById(R.id.txt_review_list_item_date);
        TextView txtLv = (TextView)listView.findViewById(R.id.txt_review_list_item_lv);
        TextView txtContents = (TextView)listView.findViewById(R.id.txt_review_list_item_contents);
        TextView txtPeriod = (TextView)listView.findViewById(R.id.txt_review_list_item_period);
        TextView txtEffect = (TextView)listView.findViewById(R.id.txt_review_list_item_effect);

        txtName.setText(name);
        txtDate.setText(date.substring(0,10));

        if (lv.equals("11")){
            txtLv.setText("" + (Integer.parseInt(dateFormat3.format(new Date(System.currentTimeMillis()))) - Integer.parseInt(birth.substring(0,4)) + 1) + "세/" + "본인");
        }else{
            txtLv.setText("" + (Integer.parseInt(dateFormat3.format(new Date(System.currentTimeMillis()))) - Integer.parseInt(birth.substring(0,4)) + 1) + "세/" +"보호자");
        }

        txtPeriod.setText("사용기간 " + dosePeriod);
        if (sideEffectFlag == 1){
            txtEffect.setText("부작용 있었어요");
        }else if (sideEffectFlag == 2){
            txtEffect.setText("부작용 없었어요");
        }else if (sideEffectFlag == 3){
            txtEffect.setText("부작용 모르겠어요");
        }

        setReadMore(txtContents,contents,4);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNo == Utils.userItem.getUserNo()){
                    Intent i = new Intent(MedicineInsertActivity.this, MedicineReviewActivity.class);
                    i.putExtra("reviewNo",reviewNo);
                    i.putExtra("medicineNo",beforeIntent.getIntExtra("medicineNo",0));
                    i.putExtra("contents",contents);
                    i.putExtra("sideEffectFlag",sideEffectFlag);
                    i.putExtra("dosePeriod",dosePeriod);
                    startActivity(i);
                }
            }
        });
        linReviewList.addView(listView);
    }

    public static void setReadMore(final TextView view, final String text, final int maxLine) {
        final Context context = view.getContext();
        final String expanedText = " ... 더보기";

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
                                lessText += item.substring(0, item.length() - (expanedText.length())) + expanedText;
                            } else {
                                lessText += item + expanedText;
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

    public void OneBtnPopup(int flag, Context context,String title, String contents, String btnText){

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
                if (flag == 2){
                    Intent i = new Intent(MedicineInsertActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("medicineInsert",true);
                    setResult(RESULT_OK,i);
                    startActivity(i);
                    btnFinish.setClickable(true);
                    onBackPressed();
                }
            }
        });

    }

    public void TwoBtnPopup(int flag, Context context, String title, String contents, String btnLeftText, String btnRightText){

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
                if (flag == 1){
                    Intent i = new Intent(MedicineInsertActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("medicineInsert",true);
                    setResult(RESULT_OK,i);
                    startActivity(i);
                    btnFinish.setClickable(true);
                    onBackPressed();
                }else{

                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
                if (flag == 1){
                    btnFinish.setClickable(true);
                    if (beforeIntent.hasExtra("newvieCheck")){
                        if (beforeIntent.getBooleanExtra("newvieCheck",false)){
                            Intent i = new Intent(MedicineInsertActivity.this, MainActivity.class);
                            i.putExtra("medicineInsert",true);
                            setResult(RESULT_OK,i);
                            startActivity(i);
                            onBackPressed();
                        }
                    }else{
                        onBackPressed();
                    }
                }else{
//                    medicineTakingItems.get(beforeIntent.getIntExtra("historyNo",0)).setAliveFlag(0);
                    new DeleteMedicineHistoryNetWork().execute();
                }
            }
        });
    }

    String response = "";

    public class InsertMedicineHistoryNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo()))
                    .add("MEDICINE_NO",""+beforeIntent.getIntExtra("medicineNo",0))
                    .add("FREQUENCY",""+frequency)
                    .add("VOLUME",edtAmount.getText().toString())
                    .add("UNIT",txtUnit.getText().toString())
                    .add("START_DT",txtStartDt.getText().toString().replace("-",""))
                    .add("END_DT",txtEndDt.getText().toString().replace("-","")).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertMedicineHistory(), body);

                return response;
            } catch (IOException e) {
                btnFinish.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"insert medicine : " + s);
            medicineTakingItems.get(beforeIntent.getIntExtra("historyNo",0)).setAliveFlag(0);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TwoBtnPopup(1,MedicineInsertActivity.this,"추가 완료!",beforeIntent.getStringExtra("medicineKo")+"가\n현재 복용중인 약에 추가되었습니다.\n약을 더 추가 하시겠습니까?","아니요","더 추가하기");
                }
            },500);
        }
    }

    public class UpdateMedicineHistoryNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("MEDICINE_HISTORY_NO", ""+beforeIntent.getIntExtra("historyNo",0))
                    .add("MEDICINE_NO",""+beforeIntent.getIntExtra("medicineNo",0))
                    .add("FREQUENCY",""+frequency)
                    .add("VOLUME",edtAmount.getText().toString())
                    .add("UNIT",txtUnit.getText().toString())
                    .add("START_DT",txtStartDt.getText().toString().replace("-",""))
                    .add("END_DT",txtEndDt.getText().toString().replace("-","")).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateMedicineHistory(), body);

                return response;
            } catch (IOException e) {
                btnFinish.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"insert medicine : " + s);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OneBtnPopup(2,MedicineInsertActivity.this,"복약 정보","수정완료","확인");
                }
            },500);
        }
    }

    public class DeleteMedicineHistoryNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("MEDICINE_HISTORY_NO", ""+beforeIntent.getIntExtra("historyNo",0))
                    .add("USER_NO",""+Utils.userItem.getUserNo())
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteMedicineHistory(), body);

                return response;
            } catch (IOException e) {
                btnFinish.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"delete medicine : " + s);
            Intent i = new Intent(MedicineInsertActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("medicineInsert",true);
            setResult(RESULT_OK,i);
            startActivity(i);
            btnFinish.setClickable(true);
            onBackPressed();
        }
    }

    public class SelectReviewHistoryListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("MEDICINE_NO", ""+beforeIntent.getIntExtra("medicineNo",0)).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectMedicineReviewList(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"select review : " + s);

            userNoList = new ArrayList<>();
            reviewNoList = new ArrayList<>();
            effectList = new ArrayList<>();
            nicknameList = new ArrayList<>();
            dateList = new ArrayList<>();
            lvList = new ArrayList<>();
            periodList = new ArrayList<>();
            contentsList = new ArrayList<>();
            birthList = new ArrayList<>();

            linReviewList.removeAllViews();

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.has("list")){
                    linReviewNoList.setVisibility(View.GONE);
                    linReviewList.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        userNoList.add(JsonIntIsNullCheck(object,"USER_NO"));
                        reviewNoList.add(JsonIntIsNullCheck(object,"REVIEW_NO"));
                        effectList.add(JsonIntIsNullCheck(object,"SIDE_EFFECT_FLAG"));
                        nicknameList.add(JsonIsNullCheck(object,"NICKNAME"));
                        dateList.add(JsonIsNullCheck(object,"UPDATE_DT").length() != 0 ? JsonIsNullCheck(object,"UPDATE_DT") : JsonIsNullCheck(object,"CREATE_DT"));
                        lvList.add(JsonIsNullCheck(object,"LV"));
                        periodList.add(JsonIsNullCheck(object,"DOSE_PERIOD"));
                        contentsList.add(JsonIsNullCheck(object,"CONTENTS"));
                        birthList.add(JsonIsNullCheck(object,"BIRTH"));
                    }

                    if (userNoList.size() > 4){
                        btnReviewMore.setVisibility(View.VISIBLE);
                        for (int i = 0; i < 4; i++){
                            ReviewList(userNoList.get(i),reviewNoList.get(i),
                                    nicknameList.get(i), dateList.get(i),
                                    lvList.get(i),contentsList.get(i),
                                    effectList.get(i),periodList.get(i),
                                    birthList.get(i));
                        }
                    }else{
                        btnReviewMore.setVisibility(View.GONE);
                        for (int i = 0; i < userNoList.size(); i++){
                            ReviewList(userNoList.get(i),reviewNoList.get(i),
                                    nicknameList.get(i), dateList.get(i),
                                    lvList.get(i),contentsList.get(i),
                                    effectList.get(i),periodList.get(i),
                                    birthList.get(i));
                        }
                    }

                }else{
                    linReviewNoList.setVisibility(View.VISIBLE);
                    linReviewList.setVisibility(View.GONE);
                }

            }catch (JSONException e){

            }
            progressDialog.dismiss();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_medicine_insert_back : {
                onBackPressed();
                break;
            }
            case R.id.img_medicine_insert_remove : {
                TwoBtnPopup(2,this,"약 삭제","복용중인 약을 삭제하시겠습니까?","취소","삭제");
                break;
            }
            case R.id.txt_medicine_insert_first : {
                linSettingVisible.setVisibility(View.VISIBLE);
                linInfoVisible.setVisibility(View.GONE);
                linReviewVisible.setVisibility(View.GONE);

                linSettingLine.setVisibility(View.VISIBLE);
                linInfoLine.setVisibility(View.INVISIBLE);
                linReviewLine.setVisibility(View.INVISIBLE);
                break;
            }
            case R.id.txt_medicine_insert_second : {
                linSettingVisible.setVisibility(View.GONE);
                linInfoVisible.setVisibility(View.VISIBLE);
                linReviewVisible.setVisibility(View.GONE);

                linSettingLine.setVisibility(View.INVISIBLE);
                linInfoLine.setVisibility(View.VISIBLE);
                linReviewLine.setVisibility(View.INVISIBLE);
                break;
            }
            case R.id.txt_medicine_insert_third : {
                linSettingVisible.setVisibility(View.GONE);
                linInfoVisible.setVisibility(View.GONE);
                linReviewVisible.setVisibility(View.VISIBLE);

                linSettingLine.setVisibility(View.INVISIBLE);
                linInfoLine.setVisibility(View.INVISIBLE);
                linReviewLine.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.txt_medicine_insert_first_unit : {
                DayCntPicker(1,"투약 단위 설정");
                break;
            }
            case R.id.txt_medicine_insert_first_cnt : {
                DayCntPicker(2,"일일 투약 횟수 설정");
                break;
            }
            case R.id.txt_medicine_insert_first_start_dt : {
                DateTimePicker(1,"투약 시작일");
                break;
            }
            case R.id.txt_medicine_insert_first_end_dt : {
                if (txtStartDt.getText().length() == 0){
                    OneBtnPopup(1,this,"투약 종료일","시작일을 먼저 선택해주세요.","확인");
                }else{
                    DateTimePicker(2,"투약 종료일");
                }
                break;
            }
            case R.id.lin_medicine_insert_third_review_insert : {
                Intent i = new Intent(this, MedicineReviewActivity.class);
                i.putExtra("medicineNo",beforeIntent.getIntExtra("medicineNo",0));
                startActivity(i);
                break;
            }
            case R.id.btn_medicine_insert_finish : {

                if (edtAmount.getText().length() != 0 && txtUnit.getText().length() != 0 && txtStartDt.getText().length() != 0 && txtEndDt.getText().length() != 0){
                    btnFinish.setClickable(false);
                    if (beforeIntent.hasExtra("historyNo")){
                        new UpdateMedicineHistoryNetWork().execute();
                    }else{
                        new InsertMedicineHistoryNetWork().execute();
                    }
                }else{
                    Utils.OneBtnPopup(this,"약 등록","설정 정보를 확인해주세요.","확인");
                }
                break;
            }
            case R.id.btn_medicine_insert_review_more : {
                btnReviewMore.setVisibility(View.GONE);
                for (int i = 4; i < userNoList.size(); i++){
                    ReviewList(userNoList.get(i),reviewNoList.get(i),
                            nicknameList.get(i), dateList.get(i),
                            lvList.get(i),contentsList.get(i),
                            effectList.get(i),periodList.get(i),
                            birthList.get(i));
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog = new ProgressDialog(MedicineInsertActivity.this);

        new SelectReviewHistoryListNetWork().execute();
    }
}
