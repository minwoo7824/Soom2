package com.kmw.soom2.DrugControl.Fragment.ParentFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Adapter.DrugControlViewPagerAdapter;
import com.kmw.soom2.DrugControl.Fragment.ChildFragment.DrugAlarmFragment;
import com.kmw.soom2.DrugControl.Fragment.ChildFragment.DrugListFragment;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;


public class DrugControlMainFragment extends Fragment {

    private String TAG = "DrugControlMainFragment";
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imgAlarm;

    DrugControlViewPagerAdapter adapter;

    public static ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();

    public DrugControlMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drug_control_main, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.drug_control_tab_layout_container);
        imgAlarm = (ImageView)v.findViewById(R.id.img_drug_control_alarm);
        tabLayout.addTab(tabLayout.newTab().setText("약 리스트"));
        tabLayout.addTab(tabLayout.newTab().setText("복약알람"));
        viewPager = (ViewPager) v.findViewById(R.id.view_pager_container_frag_drug_control_main);

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

        imgAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
                startActivity(intent);
            }
        });

        new SelectMedicineHistoryListNetWork().execute();

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new DrugControlViewPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        adapter.addFragment(new DrugListFragment(),"DrugListFragment");
        adapter.addFragment(new DrugAlarmFragment(),"DrugAlarmFragment");
        viewPager.setAdapter(adapter);
    }

    public class SelectMedicineHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", Utils.Server.selectMedicineHistoryList());

            http.addOrReplace("USER_NO",""+Utils.userItem.getUserNo());

            HttpClient post = http.create();
            post.request();
            String body = post.getBody();





            return body;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            Log.i(TAG,"복용중인 약 : " + s);


            medicineTakingItems = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.has("list")){
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        MedicineTakingItem medicineTakingItem = new MedicineTakingItem();
                        medicineTakingItem.setMedicineHistoryNo(JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"));
                        medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                        medicineTakingItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                        medicineTakingItem.setVolume(JsonIntIsNullCheck(object,"VOLUME"));
                        medicineTakingItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                        medicineTakingItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                        medicineTakingItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                        medicineTakingItem.setAliveFlag(JsonIntIsNullCheck(object,"ALIVE_FLAG"));
                        medicineTakingItem.setCreateDt(JsonIsNullCheck(object,"CREATE_DT"));
                        medicineTakingItem.setUpdateDt(JsonIsNullCheck(object,"UPDATE_DT"));

                        //clsMedicineBean
                        medicineTakingItem.setMedicineKo(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"KO"));
                        medicineTakingItem.setMedicineEn(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"EN"));
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

                        medicineTakingItems.add(medicineTakingItem);
                    }
                }

            }catch (JSONException e){

            }

            viewPager.setAdapter(adapter);
            setupViewPager(viewPager);
        }
    }
}
