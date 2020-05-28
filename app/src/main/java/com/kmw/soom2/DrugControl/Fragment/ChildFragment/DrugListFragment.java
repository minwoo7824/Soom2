package com.kmw.soom2.DrugControl.Fragment.ChildFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.DividerItemDecoration;
import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Activity.DrugCompleteListActivity;
import com.kmw.soom2.DrugControl.Adapter.DrugListRecyclerViewAdapter;
import com.kmw.soom2.DrugControl.Adapter.DrugListTestAdapter;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.Adapters.MedicineSelectAdapter;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineInsertActivity;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;
import static com.kmw.soom2.Common.Utils.logLargeString;
import static com.kmw.soom2.DrugControl.Fragment.ParentFragment.DrugControlMainFragment.medicineTakingItems;


public class DrugListFragment extends Fragment {

    private String TAG = "DrugListFragment";
    LinearLayout linComplete,linCurrentList;
    Toolbar linCurrentPlus;
    TextView txtDrugComplete;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");
    RecyclerView listTest;

    ArrayList<Integer> medicineNoList = new ArrayList<>();
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

    DrugListRecyclerViewAdapter drugListTestAdapter;

    public DrugListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_list, container, false);

        linComplete = (LinearLayout)view.findViewById(R.id.lin_drug_complete);
        linCurrentPlus = (Toolbar) view.findViewById(R.id.lin_drug_current_plus);
        linCurrentList = (LinearLayout)view.findViewById(R.id.lin_drug_current_parent);
        txtDrugComplete = (TextView)view.findViewById(R.id.count_taking_complete);
        listTest = (RecyclerView)view.findViewById(R.id.list_drug_test);

        linComplete.setVisibility(View.GONE);

        linComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DrugCompleteListActivity.class);
                i.putIntegerArrayListExtra("medicineNo",medicineNoList);
                i.putIntegerArrayListExtra("historyNo",historyNoList);
                i.putStringArrayListExtra("medicineKo",medicineKoList);
                i.putStringArrayListExtra("startDt",startDtList);
                i.putStringArrayListExtra("endDt",endDtList);
                i.putIntegerArrayListExtra("frequency",frequencyList);
                i.putIntegerArrayListExtra("volume",volumeList);
                i.putStringArrayListExtra("unit",unitList);

                i.putStringArrayListExtra("efficacy",efficacyList);
                i.putStringArrayListExtra("instructions",instructionList);
                i.putStringArrayListExtra("information",informationList);
                i.putStringArrayListExtra("stabilityRating",stabilityList);
                i.putStringArrayListExtra("precautions",precautionList);
                i.putIntegerArrayListExtra("medicineTypeNo",medicineTypeNoList);
                i.putStringArrayListExtra("medicineImg",medicineImgList);

                startActivity(i);
            }
        });

        linCurrentPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MedicineSelectActivity.class);
                startActivity(i);
            }
        });

        int drugComplete = 0;

        drugListTestAdapter = new DrugListRecyclerViewAdapter(getActivity());
        for (int i = 0; i < medicineTakingItems.size(); i++){
            if (Integer.parseInt(medicineTakingItems.get(i).getEndDt()) >= Integer.parseInt(dateFormatBefore.format(new Date(System.currentTimeMillis()))) && medicineTakingItems.get(i).getAliveFlag() == 1){
                drugListTestAdapter.addItem(medicineTakingItems.get(i));
//                medicineListItem(medicineTakingItems.get(i).getMedicineNo(),medicineTakingItems.get(i).getMedicineHistoryNo(),
//                        medicineTakingItems.get(i).getMedicineKo(),medicineTakingItems.get(i).getStartDt(),medicineTakingItems.get(i).getEndDt(),
//                        medicineTakingItems.get(i).getFrequency(),medicineTakingItems.get(i).getVolume(),medicineTakingItems.get(i).getUnit(),
//                        medicineTakingItems.get(i).getInstructions(),medicineTakingItems.get(i).getInformation(),medicineTakingItems.get(i).getEfficacy(),
//                        medicineTakingItems.get(i).getStabiltyRationg(),medicineTakingItems.get(i).getPrecaution(),medicineTakingItems.get(i).getMedicineTypeNo(),medicineTakingItems.get(i).getMedicineImg());
            }else if (Integer.parseInt(medicineTakingItems.get(i).getEndDt()) < Integer.parseInt(dateFormatBefore.format(new Date(System.currentTimeMillis()))) && medicineTakingItems.get(i).getAliveFlag() == 1){
                medicineNoList.add(medicineTakingItems.get(i).getMedicineNo());
                historyNoList.add(medicineTakingItems.get(i).getMedicineHistoryNo());
                medicineKoList.add(medicineTakingItems.get(i).getMedicineKo());
                startDtList.add(medicineTakingItems.get(i).getStartDt());
                endDtList.add(medicineTakingItems.get(i).getEndDt());
                frequencyList.add(medicineTakingItems.get(i).getFrequency());
                volumeList.add(medicineTakingItems.get(i).getVolume());
                unitList.add(medicineTakingItems.get(i).getUnit());
                efficacyList.add(medicineTakingItems.get(i).getEfficacy());
                instructionList.add(medicineTakingItems.get(i).getInstructions());
                informationList.add(medicineTakingItems.get(i).getInformation());
                stabilityList.add(medicineTakingItems.get(i).getStabiltyRationg());
                precautionList.add(medicineTakingItems.get(i).getPrecaution());
                medicineTypeNoList.add(medicineTakingItems.get(i).getMedicineTypeNo());
                medicineImgList.add(medicineTakingItems.get(i).getMedicineImg());
                medicineTakingItems.get(i).setAliveFlag(0);
                linComplete.setVisibility(View.VISIBLE);


                txtDrugComplete.setText(""+(++drugComplete));
            }
        }

        listTest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listTest.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.line_divider));
        listTest.setAdapter(drugListTestAdapter);

        return view;
    }

    void medicineListItem(final int medicineNo, final int historyNo, final String name, final String startDt, final String endDt, final int frequency, final int volume, final String unit,
                          final String instruction, final String information, final String efficacy, final String stabilty, final String precaution, final int medicineTypeNo,String medicineImg){
        View listView = new View(getActivity());
        listView = getLayoutInflater().inflate(R.layout.view_talking_medicine_list_item,null);
        TextView txtName = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_name);
        TextView txtDate = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_title_date);
        SeekBar seekBar = (SeekBar)listView.findViewById(R.id.seekbar_talking_medicine_list_item);
        TextView txtAmount = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_amount);
        TextView txtCnt = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_cnt);
        TextView txtPeriod = (TextView)listView.findViewById(R.id.txt_talking_medicine_list_item_vice_date);
        ImageView imgIcon = (ImageView)listView.findViewById(R.id.img_talking_medicine_list_item_icon);

        seekBar.setEnabled(false);
        seekBar.setThumb(getThumb((int) calDateBetweenAandB(startDt,dateFormatBefore.format(new Date(System.currentTimeMillis())))));
        seekBar.setMax((int) calDateBetweenAandB(startDt,endDt));
        seekBar.setProgress((int) calDateBetweenAandB(startDt,dateFormatBefore.format(new Date(System.currentTimeMillis()))));

        for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++){
            if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == medicineTypeNo){
                Log.i(TAG,"type img : " + Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg());
                if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0){
                    Glide.with(getActivity()).load("http:"+Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg()).into(imgIcon);
                }
            }
        }

        txtName.setText(name);

        try {
            txtDate.setText("시작일 "+dateFormat.format(dateFormatBefore.parse(startDt)) + " | " + "종료일 "+dateFormat.format(dateFormatBefore.parse(endDt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (frequency == 0 || frequency == -1){
            txtCnt.setText("필요시");
        }else{
            txtCnt.setText("하루 " + frequency + "번");
        }

        txtAmount.setText(""+volume + unit);

        txtPeriod.setText(""+ calDateBetweenAandB(startDt,dateFormatBefore.format(new Date(System.currentTimeMillis()))) + "/" + calDateBetweenAandB(startDt,endDt));

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MedicineInsertActivity.class);
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

        linCurrentList.addView(listView);
    }

    public Drawable getThumb(int progress) {
        View thumbView = LayoutInflater.from(getActivity()).inflate(R.layout.view_custom_seekbar_thumb, null, false);
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

}
