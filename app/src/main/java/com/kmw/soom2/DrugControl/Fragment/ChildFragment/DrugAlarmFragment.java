package com.kmw.soom2.DrugControl.Fragment.ChildFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.AlarmReceiver;
import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.Common.DividerItemDecoration;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Activity.DrugAlarmInsertActivity;
import com.kmw.soom2.DrugControl.Adapter.DrugAlarmRecyclerViewAdapter;
import com.kmw.soom2.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class DrugAlarmFragment extends Fragment implements View.OnClickListener {

    private String TAG = "DrugAlarmFragment";
    Toolbar linAlarmPlus;
    SimpleDateFormat dateTimeStatusFormat = new SimpleDateFormat("a");
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("hh:mm");
    Date date = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    DbOpenHelper mDbOpenHelper;
    Cursor iCursor;

    SimpleDateFormat formatHH = new SimpleDateFormat("HH");
    SimpleDateFormat formatMM = new SimpleDateFormat("mm");

    RecyclerView linAlarmListParent;
    DrugAlarmRecyclerViewAdapter drugAlarmRecyclerViewAdapter;

    public DrugAlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_alarm, container, false);

        pref = getActivity().getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        mDbOpenHelper = new DbOpenHelper(getActivity());
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mDbOpenHelper.create();

        iCursor = mDbOpenHelper.selectColumns();

        linAlarmPlus = (Toolbar) view.findViewById(R.id.lin_drug_alarm_plus);
        linAlarmListParent = (RecyclerView) view.findViewById(R.id.lin_drug_alarm_parent);

        linAlarmPlus.setOnClickListener(this);

        drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(getActivity(), mDbOpenHelper);

        while (iCursor.moveToNext()) {
            String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
            String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
            long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
            int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));

//            alarmList(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);

            drugAlarmRecyclerViewAdapter.addItem(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
            Log.i(TAG, "count 2: " + iCursor.getInt(iCursor.getColumnIndex("_id")) + " id : " + tempID + " name : " + tempSelectDay + " age : " + tempSelectTime + " gender : " + tempPushCheck);
            diaryNotification(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectTime);
        }

        linAlarmListParent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        linAlarmListParent.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.line_divider));
        linAlarmListParent.setAdapter(drugAlarmRecyclerViewAdapter);
        return view;
    }

    //    void alarmList(int idx, String selectDay, long selectTime, int pushCheck){
//        View listView = new View(getActivity());
//        listView = getLayoutInflater().inflate(R.layout.view_medicine_alarm_list_item,null);
//        TextView txtTimeStatus = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_time_status);
//        TextView txtTime = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_time);
//        TextView txtSun = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_sun);
//        TextView txtMon = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_mon);
//        TextView txtTue = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_tue);
//        TextView txtWed = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_wed);
//        TextView txtThur = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_thur);
//        TextView txtFir = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_fri);
//        TextView txtSat = (TextView)listView.findViewById(R.id.txt_medicine_alarm_list_item_sat);
//        Switch swhPush = (Switch)listView.findViewById(R.id.swh_medicine_alarm_list_item);
//
//        txtTimeStatus.setText(dateTimeStatusFormat.format(new Date(selectTime)));
//        if (dateTimeFormat.format(new Date(selectTime)).indexOf("0") == 0){
//            txtTime.setText(dateTimeFormat.format(new Date(selectTime)).substring(1,5));
//        }else{
//            txtTime.setText(dateTimeFormat.format(new Date(selectTime)));
//        }
//
//        swhPush.setChecked(pushCheck  == 1 ? true : false);
//
//        String[] days = selectDay.split(",");
//
//        for (int i = 0; i < days.length; i++){
//            if (days[i].equals("1")){
//                txtSun.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }else if (days[i].equals("2")){
//                txtMon.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }else if (days[i].equals("3")){
//                txtTue.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }else if (days[i].equals("4")){
//                txtWed.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }else if (days[i].equals("5")){
//                txtThur.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }else if (days[i].equals("6")){
//                txtFir.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }else if (days[i].equals("7")){
//                txtSat.setTextColor(getResources().getColor(R.color.colorPrimary));
//            }
//        }
//
//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(),DrugAlarmInsertActivity.class);
//                i.putExtra("alarmId",idx);
//                i.putExtra("alarmTime",selectTime);
//                i.putExtra("alarmDay",selectDay);
//                startActivityForResult(i,3333);
//            }
//        });
//
//        swhPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mDbOpenHelper.updateColumn(idx,"drugAlarm",selectDay,selectTime,isChecked == true ? 1 : 2);
//            }
//        });
//
//        linAlarmListParent.addView(listView);
//    }
    @Override
    public void onResume() {
        super.onResume();
        mDbOpenHelper = new DbOpenHelper(getActivity());
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mDbOpenHelper.create();

        iCursor = mDbOpenHelper.selectColumns();
        drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(getActivity(), mDbOpenHelper);

        while (iCursor.moveToNext()) {
            String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
            String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
            long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
            int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));

//            alarmList(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);

            drugAlarmRecyclerViewAdapter.addItem(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
            Log.i(TAG, "count 2: " + iCursor.getInt(iCursor.getColumnIndex("_id")) + " id : " + tempID + " name : " + tempSelectDay + " age : " + tempSelectTime + " gender : " + tempPushCheck);
            diaryNotification(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectTime);
        }

        linAlarmListParent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        linAlarmListParent.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.line_divider));
        drugAlarmRecyclerViewAdapter.notifyDataSetChanged();
        linAlarmListParent.setAdapter(drugAlarmRecyclerViewAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3333) {
            if (resultCode == RESULT_OK) {
                iCursor = mDbOpenHelper.selectColumns();
                drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(getActivity(), mDbOpenHelper);
                while (iCursor.moveToNext()) {
                    String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
                    String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
                    long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
                    int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));

                    drugAlarmRecyclerViewAdapter.addItem(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
//                    alarmList(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
                    Log.i(TAG, "count 1: " + iCursor.getInt(iCursor.getColumnIndex("_id")) + " id : " + tempID + " name : " + tempSelectDay + " age : " + tempSelectTime + " gender : " + tempPushCheck);
                    diaryNotification(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectTime);
                }
                linAlarmListParent.setAdapter(drugAlarmRecyclerViewAdapter);
            }
        }

    }

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;




    void diaryNotification(int idx, long times) {

        alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("cnt", idx);
        alarmIntent = PendingIntent.getBroadcast(getActivity(), idx, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(formatHH.format(new Date(times))));
        calendar.set(Calendar.MINUTE, Integer.parseInt(formatMM.format(new Date(times)))-1);

        if (Integer.parseInt(dateTimeFormat.format(new Date(times)).replace(":", "")) < Integer.parseInt(dateTimeFormat.format(new Date(System.currentTimeMillis())).replace(":", ""))) {
            calendar.add(Calendar.DAY_OF_MONTH, +1);
        }

//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, alarmIntent);

        setExactAndAllowWhileIdle(alarmMgr,AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmIntent);
    }

    public static void setExactAndAllowWhileIdle(AlarmManager alarmManager, int type, long triggerAtMillis, PendingIntent operation) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(type, triggerAtMillis, operation);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(type, triggerAtMillis, operation);
        } else {
            alarmManager.set(type, triggerAtMillis, operation);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_drug_alarm_plus: {
                Intent i = new Intent(getActivity(), DrugAlarmInsertActivity.class);
                startActivityForResult(i, 3333);
                break;
            }
        }
    }

}
