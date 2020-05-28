package com.kmw.soom2.Home.HomeActivity.MedicineInsert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.Adapters.MedicineSelectAdapter;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.logLargeString;
import static com.kmw.soom2.DrugControl.Activity.DrugCompleteListActivity.medicineNoList;

public class MedicineSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MedicineSelectActivity";
    TextView txtBack;
    TextView txtSearch;

    RecyclerView recyclerView;
    MedicineSelectAdapter adapter;
    ArrayList<RecyclerViewItemList> mList = new ArrayList<>();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_select);

        progressDialog = new ProgressDialog(MedicineSelectActivity.this);

        FindViewById();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_medicine_select_back);
        txtSearch = (TextView)findViewById(R.id.txt_medicine_select_search);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_medicine_select);

        txtBack.setOnClickListener(this);
        txtSearch.setOnClickListener(this);

        new SelectMedicineHistoryListNetWork().execute();

    }
    private ArrayList<RecyclerViewItemList> arrayList;

    public class SelectMedicineHistoryListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", Utils.Server.selectMedicineHistoryList());

            http.addOrReplace("USER_NO",""+Utils.userItem.getUserNo());

            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            logLargeString(body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);

                mList.add(new RecyclerViewItemList("최근 추가한 약", RecyclerViewItemList.HEADER_TYPE));

                if (jsonObject.has("list")){
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    ArrayList<MedicineTakingItem> medicineTakingItemArrayList = new ArrayList<>();
                    arrayList = new ArrayList<RecyclerViewItemList>();
                    Log.i(TAG,"최근 추가 길이: " + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);

                        Log.i(TAG,"최근 추가 : " + object.toString());
                        MedicineTakingItem medicineTakingItem = new MedicineTakingItem();

                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                            for(int j = 0; j < medicineNoList.size();j++){
                                if(!medicineNoList.get(j).equals(JsonIntIsNullCheck(object,"MEDICINE_NO"))){
                                    medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                                    medicineTakingItem.setMedicineKo(JsonIsNullCheck(object,"KO"));
                                    medicineTakingItem.setAliveFlag(JsonIntIsNullCheck(object,"ALIVE_FLAG"));

                                    medicineTakingItem.setEndDt(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"END_DT"));
                                    medicineTakingItem.setManufacturer(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"MANUFACTURER"));
                                    medicineTakingItem.setIngredient(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"INGREDIENT"));
                                    medicineTakingItem.setForm(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"FORM"));
                                    medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"),"MEDICINE_TYPE_NO"));
                                    medicineTakingItem.setStorageMethod(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"STORAGE_METHOD"));
                                    medicineTakingItem.setEfficacy(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"EFFICACY"));
                                    medicineTakingItem.setInformation(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"INFORMATION"));
                                    medicineTakingItem.setStabiltyRationg(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"STABILITY_RATING"));
                                    medicineTakingItem.setPrecaution(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"PRECAUTIONS"));
                                    medicineTakingItem.setBookMark(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"),"BOOKMARK"));
                                    medicineTakingItem.setMedicineImg(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"MEDICINE_IMG"));
                                    medicineTakingItem.setInstructions(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"INSTRUCTIONS"));
                                    medicineTakingItemArrayList.add(medicineTakingItem);
                                }
                            }
                        }

                        if (jsonArray.length() > 4){
                            if (i == 4){
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < medicineTakingItemArrayList.size(); i++){
                        mList.add(new RecyclerViewItemList(medicineTakingItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                    }
                }

                mList.add(new RecyclerViewItemList("자주 찾는 천식약", RecyclerViewItemList.HEADER_TYPE));
                for (int i = 0; i < MEDICINE_LIST.size(); i++){
                    if (MEDICINE_LIST.get(i).getBookMark() == 1){
                        mList.add(new RecyclerViewItemList(MEDICINE_LIST.get(i), RecyclerViewItemList.ITEM_TYPE));
                    }
                }

                adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, mList);

                recyclerView.setLayoutManager(new LinearLayoutManager(MedicineSelectActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
                recyclerView.setAdapter(adapter);

            }catch (JSONException e){

            }
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_medicine_select_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_medicine_select_search : {
                Intent i = new Intent(this,MedicineSearchActivity.class);
                startActivity(i);
                break;
            }
        }
    }
}
