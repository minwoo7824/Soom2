package com.kmw.soom2.Home.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Items.KoListItem;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.Home.HomeActivity.CalendarActivity;
import com.kmw.soom2.Home.HomeActivity.DustRecordActivity;
import com.kmw.soom2.Home.HomeActivity.FilterActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.SymptomRecord;
import com.kmw.soom2.Home.HomeAdapter.RecyclerViewAdapter;
import com.kmw.soom2.Home.HomeAdapter.ViewPagerAdapter;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.EtcItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.Home.HomeItem.ZoomAnimation;
import com.kmw.soom2.InsertActivity.InsertActivity.HomeActivity;
import com.kmw.soom2.InsertActivity.InsertActivity.SplashActivity;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.kmw.soom2.Views.LinearLayoutManagerWithSmoothScroller;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;
import static com.kmw.soom2.Common.Utils.StartActivity;
import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;


public class HomeFragment extends Fragment implements View.OnTouchListener,View.OnClickListener{

    private String TAG = "HomeFragment";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter = null;
    ArrayList<RecyclerViewItemList> mList = new ArrayList<>();
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ArrayList<BannerItem> arrayList;
    Handler handler = new Handler();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    ImageView calendarImageView, filterImageView, alarmImageView;
    BottomSheetDialog measureBottomSheetDialog;
    ImageView measureImageView;
    String date;
    TabLayout tabLayout;
    LinearLayout tabStip;
    TextView txtPeriod;
    LinearLayout linNoList;

    LinearLayout linMedicine,linSymptom,linBreath,linMemo,linMeasure;

    boolean mLockScrollView = false;
    boolean actIsPossible = true;

    int paging = 1;
    int searchTotalPage = 0;

    ProgressDialog progressDialog;
    public static ArrayList<String> filterTextList = new ArrayList<>();
    ArrayList<HistoryItems> hisItems = new ArrayList<>();

    public static boolean categoryAct = false;

    public HomeFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        paging = 1;
        searchTotalPage = 0;
        mLockScrollView = false;
        mList = new ArrayList<>();
        hisItems = new ArrayList<>();
        if (Utils.userItem == null){
            new LoginProcessNetWork().execute();
        }else{
            try {
                Log.i(TAG,"몇일차1 : " + (calDateBetweenAandB(format1.format(format2.parse(Utils.userItem.getCreateDt())),format1.format(new Date(System.currentTimeMillis())))+1));
                txtPeriod.setText("숨 관리, " + (calDateBetweenAandB(format1.format(format2.parse(Utils.userItem.getCreateDt())),format1.format(new Date(System.currentTimeMillis())))+1) + "일차");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new SelectHomeFeedHistoryListNetWork().execute("1");
        }
//        new SelectActHistoryListNetWork().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout_fragment_home);
        tabStip = ((LinearLayout)tabLayout.getChildAt(0));

        filterTextList.add("1");
        filterTextList.add("11");
        filterTextList.add("12");
        filterTextList.add("13");
        filterTextList.add("14");
        filterTextList.add("21");
        filterTextList.add("22");
        filterTextList.add("23");
        filterTextList.add("30");

        progressDialog = new ProgressDialog(getActivity());

        recyclerView = v.findViewById(R.id.recycler_view_fragment_home);
        adapter = new RecyclerViewAdapter(getContext(), mList,this);

        linMedicine = (LinearLayout)v.findViewById(R.id.lin_home_medicine);
        linSymptom = (LinearLayout)v.findViewById(R.id.lin_home_symptom);
        linBreath = (LinearLayout)v.findViewById(R.id.lin_home_breath);
        linMemo = (LinearLayout)v.findViewById(R.id.lin_home_memo);
        linMeasure = (LinearLayout)v.findViewById(R.id.lin_home_measure);
        linNoList = (LinearLayout)v.findViewById(R.id.lin_home_fragment_symptom_no_list);

        alarmImageView = v.findViewById(R.id.alarm_fragment_home);
        calendarImageView = (ImageView) v.findViewById(R.id.calendar_fragment_home);
        filterImageView = (ImageView) v.findViewById(R.id.filter_fragment_home);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager_fragment_home);
        arrayList = new ArrayList();
        txtPeriod = (TextView)v.findViewById(R.id.day_count_fragment_home);

        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewPager.getWidth(), (int) (viewPager.getWidth() * 0.41));
                viewPager.setLayoutParams(params);

                viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
        recyclerView.setAdapter(adapter);

        measureImageView = (ImageView) v.findViewById(R.id.measure_fragment_home);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCnt = recyclerView.getAdapter().getItemCount();

                Log.i(TAG,"1 : " + lastVisibleItemPosition + " 2 : " + itemTotalCnt);

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && paging < searchTotalPage && mLockScrollView){
                    Log.i(TAG,"aaaaaaaa");
                    mLockScrollView = false;
                    new SelectHomeFeedHistoryListNetWork().execute(""+(++paging));
                }
            }
        });

        measureBottomSheetDialog = new BottomSheetDialog(getActivity());
        final View bottomSheetDialogMeasuer = getLayoutInflater().inflate(R.layout.fragment_measure_dialog, null);
        measureBottomSheetDialog.setContentView(bottomSheetDialogMeasuer);
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        bottomSheetDialogMeasuer.getLayoutParams().height = (int) (height * 0.31f);

        Button btnBreath = (Button)measureBottomSheetDialog.findViewById(R.id.asthma_control_button_fragment_measure_dialog);
        Button btnDust = (Button)measureBottomSheetDialog.findViewById(R.id.dust_button_fragment_measure_dialog);

        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
//                getActivity().startActivityForResult(intent, 1111);
                startActivity(intent);
            }
        });

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                getActivity().startActivityForResult(intent,1111);
            }
        });

        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putStringArrayListExtra("filter",filterTextList);
                getActivity().startActivityForResult(intent,2222);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                handler.postDelayed(this, 3000);
            }
        }, 3000);

        linMedicine.setOnClickListener(this);
        linSymptom.setOnClickListener(this);
        linBreath.setOnClickListener(this);
        linMemo.setOnClickListener(this);
        linMeasure.setOnClickListener(this);

        btnBreath.setOnClickListener(this);
        btnDust.setOnClickListener(this);

        new SelectBannerListNetWork().execute();

        return v;
    }


    public void setTabLayout() {
        for (int i = 0; i < arrayList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            tabStip.getChildAt(i).setOnTouchListener(this);
        }
    }

    String response;

    public class SelectHomeFeedHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("USER_NO", ""+Utils.userItem.getUserNo()).add("Search_ShowCNT","20").add("Search_Page",strings[0]).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedList(), body);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (!jsonObject.has("list")){
                    recyclerView.setVisibility(View.GONE);
                    linNoList.setVisibility(View.VISIBLE);
                }

                JSONArray jsonArray = jsonObject.getJSONArray("list");

                searchTotalPage = jsonObject.getInt("Search_TotalPage");

                final ArrayList<String> registerDtList = new ArrayList<>();
                ArrayList<ArrayList<String>> koList = new ArrayList<>();

                ArrayList<EtcItem> etcItemArrayList = new ArrayList<>();

                ArrayList<KoListItem> koStringArr = new ArrayList<>();

                int size = jsonArray.length();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                        HistoryItems hisItem = new HistoryItems();

                        hisItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                        hisItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                        hisItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                        hisItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                        hisItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                        hisItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                        hisItem.setAge(JsonIntIsNullCheck(object, "AGE"));
                        hisItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        hisItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                        hisItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                        hisItem.setAliveFlag(JsonIntIsNullCheck(object, "ALIVE_FLAG"));

                        /// 복약 관련
                        hisItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                        hisItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                        hisItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                        hisItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                        hisItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                        hisItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                        hisItem.setEndDt(JsonIsNullCheck(object, "END_DT"));

                        if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") != 1) {
                            hisItem.setKo(JsonIsNullCheck(object, "KO")+"[응급]");
                        }else {
                            hisItem.setKo(JsonIsNullCheck(object, "KO"));
                        }

                        /// 증상
                        hisItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                        /// 메모
                        hisItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                        /// 미세먼지
                        hisItem.setLatitude(JsonIsNullCheck(object, "LATITUDE"));
                        hisItem.setLongitute(JsonIsNullCheck(object, "LONGITUDE"));
                        hisItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                        hisItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                        hisItem.setDustState(JsonIntIsNullCheck(object, "DUST_STATE"));
                        hisItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                        hisItem.setUltraDustState(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                        /// PEF
                        hisItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                        hisItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                        /// ACT
                        hisItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                        hisItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                        hisItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                        hisItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));

                        hisItems.add(hisItem);
                    }
                }

                categoryAct = false;

                HashMap<String, ArrayList<HistoryItems>> map = setupHistoryData(hisItems);

                TreeMap<String, ArrayList<HistoryItems>> treeMap = new TreeMap<>(map);

                TreeMap<String, TreeMap<String, ArrayList<HistoryItems>>> finalMap = new TreeMap<>();

                for (Map.Entry<String, ArrayList<HistoryItems>> entry : treeMap.descendingMap().entrySet()) {

                    String key = entry.getKey();
                    ArrayList<HistoryItems> value = entry.getValue();

                    TreeMap<String, ArrayList<HistoryItems>> subMapItems = new TreeMap<>(setupHistoryDataWithRegiDt(value));

                    finalMap.put(key, subMapItems);
                }

                for (int i = 0; i < size; i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){

                        ArrayList<String> koChildList = new ArrayList<>();

                        EtcItem etcItem = new EtcItem();

                        if (filterTextList.contains(JsonIsNullCheck(object,"CATEGORY"))){
                            if (JsonIntIsNullCheck(object,"CATEGORY") == 1){

                                if (registerDtList.contains(JsonIsNullCheck(object,"REGISTER_DT"))){
                                    if (JsonIntIsNullCheck(object,"EMERGENCY_FLAG") == 1){
                                        koList.get(registerDtList.indexOf(JsonIsNullCheck(object,"REGISTER_DT"))).add(JsonIsNullCheck(object,"KO"));
                                    }else{
                                        koList.get(registerDtList.indexOf(JsonIsNullCheck(object,"REGISTER_DT"))).add(JsonIsNullCheck(object,"KO")+"[응급]");
                                    }
                                }else{
                                    registerDtList.add(JsonIsNullCheck(object,"REGISTER_DT"));
                                    if (JsonIntIsNullCheck(object,"EMERGENCY_FLAG") == 1){
                                        koChildList.add(JsonIsNullCheck(object,"KO"));
                                    }else{
                                        koChildList.add(JsonIsNullCheck(object,"KO")+"[응급]");
                                    }
                                    koList.add(koChildList);


                                    etcItem.setActScore(JsonIntIsNullCheck(object,"ACT_SCORE"));
                                    etcItem.setActSelected(JsonIsNullCheck(object,"ACT_SELECTED"));
                                    etcItem.setActState(JsonIntIsNullCheck(object,"ACT_STATE"));
                                    etcItem.setActType(JsonIntIsNullCheck(object,"ACT_TYPE"));
                                    etcItem.setCause(JsonIsNullCheck(object,"CAUSE"));
                                    etcItem.setDust(JsonIntIsNullCheck(object,"DUST"));
                                    etcItem.setDustStatus(JsonIntIsNullCheck(object,"DUST_STATE"));
                                    etcItem.setUltraDust(JsonIntIsNullCheck(object,"ULTRA_DUST"));
                                    etcItem.setUltraDustStatus(JsonIntIsNullCheck(object,"ULTRA_DUST_STATE"));
                                    etcItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                                    etcItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                                    etcItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                                    etcItem.setImageFile(JsonIsNullCheck(object,"IMAGE_FILE"));
                                    etcItem.setInspiratorFlag(JsonIntIsNullCheck(object,"INSPIRATOR_FLAG"));
                                    etcItem.setLat(JsonIsNullCheck(object,"LATITUDE"));
                                    etcItem.setLng(JsonIsNullCheck(object,"LONGITUDE"));
                                    etcItem.setLocation(JsonIsNullCheck(object,"LOCATION"));
                                    etcItem.setMemo(JsonIsNullCheck(object,"MEMO"));
                                    etcItem.setPefScore(JsonIntIsNullCheck(object,"PEF_SCORE"));
                                    etcItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                                    etcItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                                    etcItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                                    etcItem.setRegisterDt(JsonIsNullCheck(object,"REGISTER_DT"));
                                    etcItem.setMedicineNo(JsonIsNullCheck(object,"MEDICINE_NO"));
                                    etcItem.setUserHistoryNo(JsonIsNullCheck(object,"USER_HISTORY_NO"));
                                    etcItem.setKo(JsonIsNullCheck(object,"KO"));
                                    etcItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));

                                    etcItemArrayList.add(etcItem);

                                    KoListItem koItem = new KoListItem();
                                    koItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                                    koItem.setKo(JsonIsNullCheck(object,"KO"));
                                    koStringArr.add(koItem);
                                }
                            }else{
                                if (paging == 1){
                                    if (JsonIntIsNullCheck(object,"CATEGORY") == 21 && JsonIsNullCheck(object,"REGISTER_DT").substring(0,10).equals(formatYYYYMMDD.format(new Date(System.currentTimeMillis())))){
                                        categoryAct = true;
                                    }
                                }

                                registerDtList.add(JsonIsNullCheck(object,"REGISTER_DT"));
                                koChildList.add(""+JsonIntIsNullCheck(object,"CATEGORY"));
                                koList.add(koChildList);

                                etcItem.setActScore(JsonIntIsNullCheck(object,"ACT_SCORE"));
                                etcItem.setActSelected(JsonIsNullCheck(object,"ACT_SELECTED"));
                                etcItem.setActState(JsonIntIsNullCheck(object,"ACT_STATE"));
                                etcItem.setActType(JsonIntIsNullCheck(object,"ACT_TYPE"));
                                etcItem.setCause(JsonIsNullCheck(object,"CAUSE"));
                                etcItem.setDust(JsonIntIsNullCheck(object,"DUST"));
                                etcItem.setDustStatus(JsonIntIsNullCheck(object,"DUST_STATE"));
                                etcItem.setUltraDust(JsonIntIsNullCheck(object,"ULTRA_DUST"));
                                etcItem.setUltraDustStatus(JsonIntIsNullCheck(object,"ULTRA_DUST_STATE"));
                                etcItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                                etcItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                                etcItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                                etcItem.setImageFile(JsonIsNullCheck(object,"IMAGE_FILE"));
                                etcItem.setInspiratorFlag(JsonIntIsNullCheck(object,"INSPIRATOR_FLAG"));
                                etcItem.setLat(JsonIsNullCheck(object,"LATITUDE"));
                                etcItem.setLng(JsonIsNullCheck(object,"LONGITUDE"));
                                etcItem.setLocation(JsonIsNullCheck(object,"LOCATION"));
                                etcItem.setMemo(JsonIsNullCheck(object,"MEMO"));
                                etcItem.setPefScore(JsonIntIsNullCheck(object,"PEF_SCORE"));
                                etcItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                                etcItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                                etcItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                                etcItem.setRegisterDt(JsonIsNullCheck(object,"REGISTER_DT"));
                                etcItem.setMedicineNo(JsonIsNullCheck(object,"MEDICINE_NO"));
                                etcItem.setUserHistoryNo(JsonIsNullCheck(object,"USER_HISTORY_NO"));
                                etcItem.setKo(JsonIsNullCheck(object,"KO"));
                                etcItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));

                                etcItemArrayList.add(etcItem);
                            }
                        }
                    }
                }

                Log.i(TAG,"koList size : " + koList.size());

                for (int i = 0; i < koList.size(); i++){
                    if (i != 0){
                        if (registerDtList.get(i - 1).substring(0,10).equals(registerDtList.get(i).substring(0,10))){
                            mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                        }else{

//                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10)).get(registerDtList.get(i).substring(0, 10))));
                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10))));
//                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE));
                            mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));

                        }
                    }else{
                        if (mList.size() > 0) {
                            if (mList.get(mList.size() - 1).getEtcItem() != null) {
                                if (mList.get(mList.size() - 1).getEtcItem().getRegisterDt().contains(registerDtList.get(i).substring(0, 10))) {
                                    mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i),RecyclerViewItemList.ITEM_TYPE));
                                }
                            }
                        }else {
                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10))));
                            mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                        }
                    }
                }

                if (paging == 1){
                    adapter = new RecyclerViewAdapter(getActivity(), mList,HomeFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
                    recyclerView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();

                if (adapter.getItemCount() == 0){
                    recyclerView.setVisibility(View.GONE);
                    linNoList.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    linNoList.setVisibility(View.GONE);
                }
            }catch (JSONException e){

            }
            mLockScrollView = true;
            progressDialog.dismiss();
        }
    }
    public HashMap<String, ArrayList<HistoryItems>> setupHistoryDataWithRegiDt(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt())) {
                map.get(datas.get(i).getRegisterDt()).add(datas.get(i));
            }else {
                map.put(datas.get(i).getRegisterDt(), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt()).add(datas.get(i));
            }
        }

        return map;
    }
    public HashMap<String, ArrayList<HistoryItems>> setupHistoryData(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt().substring(0, 10))) {
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }else {
                map.put(datas.get(i).getRegisterDt().substring(0, 10), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }
        }

        return map;
    }
    public class SelectBannerListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectBannerList(), body);

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

            try {
             JSONObject jsonObject = new JSONObject(s);

             JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = jsonArray.length()-1; i >= 0; i--){
                    JSONObject object = jsonArray.getJSONObject(i);

                    BannerItem bannerItem = new BannerItem();

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                        bannerItem.setBannerNo(JsonIsNullCheck(object,"BANNER_NO"));
                        bannerItem.setBannerType(JsonIsNullCheck(object,"BANNER_TYPE"));
                        bannerItem.setBannerLink(JsonIsNullCheck(object,"BANNER_LINK"));
                        bannerItem.setImageFile(JsonIsNullCheck(object,"IMAGE_FILE"));
                        bannerItem.setPriority(JsonIntIsNullCheck(object,"PRIORITY"));

                        arrayList.add(bannerItem);
                    }
                }

                Collections.sort(arrayList, UpComparator);

                viewPagerAdapter = new ViewPagerAdapter(getContext(), arrayList);
                viewPager.setOffscreenPageLimit(1);

                setTabLayout();

                viewPager.setAdapter(viewPagerAdapter);
                final int FIRST_PAGE = 60006 * arrayList.size() / 2;
                viewPager.setCurrentItem(FIRST_PAGE);
                viewPager.setPageTransformer(true, new ZoomAnimation());

                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        TabLayout.Tab tab =  tabLayout.getTabAt(Integer.MAX_VALUE%(Integer.MAX_VALUE - position)%arrayList.size());
                        tab.select();

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }catch (JSONException e){

            }
        }
    }

    public class LoginProcessNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String stEmail = pref.getString("USER_ID","");
            String stPassword = pref.getString("USER_PASSWORD","");
            String stToken = pref.getString("DEVICE_CODE", "");

            FormBody body;

            if (pref.getInt("LOGIN_TYPE", 0) == 1 || pref.getInt("LOGIN_TYPE", 0) == 0) {
                body = (new FormBody.Builder()).add("ID", stEmail).add("PASSWORD", stPassword).add("DEVICE_CODE", stToken).add("OS_TYPE", "1").build();
            }else {
                body = (new FormBody.Builder()).add("ID", stEmail).add("DEVICE_CODE", stToken).add("OS_TYPE", "1").build();
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body);

                Log.d("Response", response);
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
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {

                    JSONObject object = jsonArray.getJSONObject(0);
                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                        UserItem userItem = new UserItem();

                        userItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                        userItem.setLv(JsonIntIsNullCheck(object, "LV"));
                        userItem.setId(JsonIsNullCheck(object, "ID"));
                        userItem.setEmail(JsonIsNullCheck(object, "EMAIL"));
                        userItem.setPassword(JsonIsNullCheck(object, "PASSWORD"));
                        userItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                        userItem.setName(JsonIsNullCheck(object, "NAME"));
                        userItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                        userItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                        userItem.setDiagnosisFlag(JsonIntIsNullCheck(object, "DIAGNOSIS_FLAG"));  // 확진 여부
                        userItem.setOutbreakDt(JsonIsNullCheck(object, "OUTBREAK_DT")); // 발병일
                        userItem.setProfileImg(JsonIsNullCheck(object, "PROFILE_IMG"));  // 프로필 사진
                        userItem.setDeviceCode(JsonIsNullCheck(object, "DEVICE_CODE"));  // fcm토큰
                        userItem.setLoginType(JsonIntIsNullCheck(object, "LOGIN_TYPE"));  //  1: 일반 이메일, 2 : 네이버, 3 : 카카오, 4 : 애플
                        userItem.setOsType(JsonIntIsNullCheck(object, "OS_TYPE")); // 1 : android, 2 : ios
                        userItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        userItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                        userItem.setPhone(JsonIsNullCheck(object,"PHONE"));

                        Utils.userItem = userItem;

                        pref = getActivity().getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("DEVICE_CODE",JsonIsNullCheck(object, "DEVICE_CODE"));
                        editor.putString("USER_ID", JsonIsNullCheck(object, "ID"));
                        editor.putString("USER_PASSWORD", JsonIsNullCheck(object, "PASSWORD"));
                        editor.putInt("LOGIN_TYPE", JsonIntIsNullCheck(object, "LOGIN_TYPE"));
                        editor.putInt("OS_TYPE", JsonIntIsNullCheck(object, "OS_TYPE"));

                        editor.putInt("IS_LOGIN_FLAG", 1);  // 1 : 로그인 됨
                        editor.apply();

                        try {
                            Log.i(TAG,"몇일차2 : " + (calDateBetweenAandB(format1.format(format2.parse(Utils.userItem.getCreateDt())),format1.format(new Date(System.currentTimeMillis())))+1));
                            txtPeriod.setText("숨 관리, " + (calDateBetweenAandB(format1.format(format2.parse(Utils.userItem.getCreateDt())),format1.format(new Date(System.currentTimeMillis())))+1) + "일차");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        new SelectHomeFeedHistoryListNetWork().execute("1");
                    }else {

                    }
                }else {

                }
            }catch (JSONException e){

            }
        }
    }

    private Comparator<BannerItem> UpComparator = new Comparator<BannerItem>() {
        @Override
        public int compare(BannerItem bannerItem, BannerItem t1) {
            int reset;
            if (bannerItem.getPriority() > t1.getPriority()){
                reset = -1;
            }else if (bannerItem.getPriority() == t1.getPriority()) {
                reset = 0;
            }else
                reset = 1;
            return reset;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_home_medicine : {
                Intent i = new Intent(getActivity(), MedicinRecordActivity.class);
                startActivityForResult(i,1111);
                break;
            }
            case R.id.lin_home_symptom : {
                Intent i = new Intent(getActivity(), SymptomRecord.class);
                startActivityForResult(i,1111);
                break;
            }
            case R.id.lin_home_breath : {
                Intent i = new Intent(getActivity(), BreathRecordActivity.class);
                startActivityForResult(i,1111);
                break;
            }
            case R.id.lin_home_memo : {
                Intent i = new Intent(getActivity(), MemoActivity.class);
                startActivityForResult(i,1111);
                break;
            }
            case R.id.lin_home_measure : {
                measureBottomSheetDialog.show();
                break;
            }
            case R.id.asthma_control_button_fragment_measure_dialog : {
                if (!categoryAct) {
                    Intent i = new Intent(getActivity(), AsthmaControlActivity.class);
                    i.putExtra("homeFragment",true);
                    startActivityForResult(i, 1111);
                }else {
                    Log.d(TAG, "측정 한도 넘어 섬");
                    OneBtnPopup(getActivity(),"1일 검사 횟수 초과","이미 검사를 완료하셨습니다.\n천식조절검사(ACT)는\n하루에 한번 할 수 있습니다.","확인");
                }
                break;
            }
            case R.id.dust_button_fragment_measure_dialog : {
                Intent i = new Intent(getActivity(), DustRecordActivity.class);
                startActivityForResult(i,1111);
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (measureBottomSheetDialog.isShowing()){
            measureBottomSheetDialog.dismiss();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
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
                Log.i(TAG,"성공");
         
//                new SelectHomeFeedHistoryListNetWork().execute("1");
            }
        }else if (requestCode == 2222){
            if (resultCode == RESULT_OK){

            }
        }
    }


}
