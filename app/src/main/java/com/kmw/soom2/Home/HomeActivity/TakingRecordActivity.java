package com.kmw.soom2.Home.HomeActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.Home.HomeAdapter.TakingRecordRecyclerViewAdapter;
import com.kmw.soom2.Home.HomeItem.TakingRecordItemList;
import com.kmw.soom2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TakingRecordActivity extends AppCompatActivity implements View.OnClickListener {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy년 MM월 dd일");
    Date currentTime = new Date();
    Calendar calendar = Calendar.getInstance();
    TextView dateTextView;
    TextView prevButtonTextView, nextButtonTextView;
    String date;
    TakingRecordRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<TakingRecordItemList> arrayList;
    LinearLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_record);

        calendar.setTime(currentTime);

        date = format1.format(calendar.getTimeInMillis());

        dateTextView = (TextView) findViewById(R.id.date_text_view_activity_taking_record);
        prevButtonTextView = (TextView) findViewById(R.id.prev_day_text_view_activity_taking_record);
        prevButtonTextView.setOnClickListener(this);
        nextButtonTextView = (TextView) findViewById(R.id.next_day_text_view_activity_taking_record);
        nextButtonTextView.setOnClickListener(this);

        emptyView = (LinearLayout) findViewById(R.id.empty_view);

        dateTextView.setText(date);

        arraySet();

        recyclerView = findViewById(R.id.recycler_view_activity_taking_record);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new TakingRecordRecyclerViewAdapter(arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

        if (arrayList ==null) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_day_text_view_activity_taking_record:
                calendar.add(Calendar.DATE, -1);
                date = format1.format(calendar.getTimeInMillis());
                dateTextView.setText(date);
                Log.i("test111", date);

                break;

            case R.id.next_day_text_view_activity_taking_record:
                calendar.add(Calendar.DATE, 1);
                date = format1.format(calendar.getTimeInMillis());
                dateTextView.setText(date);
                Log.i("test111", date);
                break;
        }

    }

    private void arraySet() {
        arrayList = new ArrayList<>();

        arrayList.add(new TakingRecordItemList("버튼을 눌러 상태를 변경할 수 있습니다.", TakingRecordItemList.HEADER_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("아스마넥스트위스200μg", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("test", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("test", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("test", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("test", TakingRecordItemList.BODY_TYPE));
        arrayList.add(new TakingRecordItemList("test", TakingRecordItemList.BODY_TYPE));

    }
}