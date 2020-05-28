package com.kmw.soom2.DrugControl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineInsertActivity;
import com.kmw.soom2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;

public class DrugCompleteListActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "DrugCompleteListActivity";
    TextView txtBack;
    LinearLayout linListParent;
    Intent beforeIntent;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");

    public static ArrayList<Integer> medicineNoList = new ArrayList<>();
    ArrayList<Integer> historyNoList = new ArrayList<>();
    ArrayList<String> medicineKoList = new ArrayList<>();
    ArrayList<String> startDtList = new ArrayList<>();
    ArrayList<String> endDtList = new ArrayList<>();
    ArrayList<Integer> frequencyList = new ArrayList<>();
    ArrayList<Integer> volumeList = new ArrayList<>();
    ArrayList<String> unitList = new ArrayList<>();

    ArrayList<String> efficacyList          = new ArrayList<>();
    ArrayList<String> instructionList       = new ArrayList<>();
    ArrayList<String> informationList       = new ArrayList<>();
    ArrayList<String> stabilityList         = new ArrayList<>();
    ArrayList<String> precautionList        = new ArrayList<>();
    ArrayList<Integer> medicineTypeNoList   = new ArrayList<>();
    ArrayList<String> medicineImgList       = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_complete_list);

        beforeIntent = getIntent();

        FindViewById();

        medicineNoList = beforeIntent.getIntegerArrayListExtra("medicineNo");
        historyNoList = beforeIntent.getIntegerArrayListExtra("historyNo");
        medicineKoList = beforeIntent.getStringArrayListExtra("medicineKo");
        startDtList = beforeIntent.getStringArrayListExtra("startDt");
        endDtList = beforeIntent.getStringArrayListExtra("endDt");
        frequencyList = beforeIntent.getIntegerArrayListExtra("frequency");
        volumeList = beforeIntent.getIntegerArrayListExtra("volume");
        unitList = beforeIntent.getStringArrayListExtra("unit");

        efficacyList = beforeIntent.getStringArrayListExtra("efficacy");
        instructionList = beforeIntent.getStringArrayListExtra("instructions");
        informationList = beforeIntent.getStringArrayListExtra("information");
        stabilityList = beforeIntent.getStringArrayListExtra("stabilityRating");
        precautionList = beforeIntent.getStringArrayListExtra("precautions");
        medicineTypeNoList = beforeIntent.getIntegerArrayListExtra("medicineTypeNo");

        medicineImgList = beforeIntent.getStringArrayListExtra("medicineImg");

        for (int i = 0; i < medicineNoList.size(); i++){
            Log.i(TAG,"i : " + i);
            medicineListItem(medicineNoList.get(i),historyNoList.get(i),
                    medicineKoList.get(i),startDtList.get(i),endDtList.get(i),
                    frequencyList.get(i),volumeList.get(i),unitList.get(i),
                    efficacyList.get(i),instructionList.get(i),informationList.get(i),
                    stabilityList.get(i),precautionList.get(i),medicineTypeNoList.get(i),medicineImgList.get(i));
        }
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_drug_complete_back);
        linListParent = (LinearLayout)findViewById(R.id.lin_drug_complete_list_parent);

        txtBack.setOnClickListener(this);
    }

    void medicineListItem(final int medicineNo, final int historyNo, final String name, final String startDt, final String endDt, final int frequency, final int volume, final String unit,
                          final String instruction, final String information, final String efficacy, final String stabilty, final String precaution, final int medicineTypeNo,String medicineImg){
        View listView = new View(DrugCompleteListActivity.this);
        listView = getLayoutInflater().inflate(R.layout.view_talking_medicine_list_item,null);

        TextView txtName = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_name);
        TextView txtDate = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_title_date);
        SeekBar seekBar = (SeekBar)listView.findViewById(R.id.seekbar_talking_medicine_list_item);
        TextView txtAmount = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_amount);
        TextView txtCnt = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_cnt);
        TextView txtPeriod = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_vice_date);
        ImageView imgIcon = (ImageView)listView.findViewById(R.id.img_talking_medicine_list_item_icon);

        for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++){
            if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == medicineTypeNo){
                Log.i(TAG,"type img : " + Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg());
                if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0){
                    Glide.with(DrugCompleteListActivity.this).load("http:"+Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg()).into(imgIcon);
                }
            }
        }

        seekBar.setEnabled(false);
        seekBar.setThumb(getThumb((int) calDateBetweenAandB(startDt,endDt)));
        seekBar.setMax((int) calDateBetweenAandB(startDt,endDt));
        seekBar.setProgress((int) calDateBetweenAandB(startDt,endDt));

        txtName.setText(name);
        try {
            txtDate.setText("시작일 "+dateFormat.format(dateFormatBefore.parse(startDt)) + " | " + "종료일 "+dateFormat.format(dateFormatBefore.parse(endDt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (frequency == 0){
            txtAmount.setText("필요시");
        }else{
            txtAmount.setText("1일 " + frequency);
        }
        txtCnt.setText("1회 " + volume + unit);

        txtPeriod.setText(""+ calDateBetweenAandB(startDt,endDt) + "/" + calDateBetweenAandB(startDt,endDt));

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrugCompleteListActivity.this, MedicineInsertActivity.class);
                i.putExtra("medicineNo",medicineNo);
                i.putExtra("historyNo",historyNo);
                i.putExtra("name",name);
                i.putExtra("startDt",startDt);
                i.putExtra("endDt",endDt);
                i.putExtra("frequency",frequency);
                i.putExtra("volume",volume);
                i.putExtra("unit",unit);
                i.putExtra("efficacy",efficacy);
                i.putExtra("instructions",instruction);
                i.putExtra("information",information);
                i.putExtra("stabilityRating",stabilty);
                i.putExtra("precautions",precaution);
                i.putExtra("medicineTypeNo",medicineTypeNo);
                i.putExtra("medicineImg",medicineImg);
                startActivity(i);
            }
        });

        linListParent.addView(listView);
    }

    public Drawable getThumb(int progress) {
        View thumbView = LayoutInflater.from(DrugCompleteListActivity.this).inflate(R.layout.view_custom_seekbar_thumb, null, false);
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_drug_complete_back : {
                onBackPressed();
                break;
            }
        }
    }
}
