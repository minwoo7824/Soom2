package com.kmw.soom2.Common.DateTimePicker;

import android.content.Context;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.kmw.soom2.Common.DateTimePicker.DateHelper.getMonth;
import static com.kmw.soom2.Common.DateTimePicker.DateHelper.today;


public class WheelMonthPicker extends WheelPicker<String> {

    private int lastScrollPosition;

    private MonthSelectedListener listener;

    private boolean displayMonthNumbers = false;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {

    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> monthList = new ArrayList<>();

        final SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.getDefault());
        final Calendar cal = Calendar.getInstance(Locale.getDefault());

        for (int i = 1; i < 13; i++) {
//            cal.set(Calendar.MONTH, i);
//            if (displayMonthNumbers) {
//                monthList.add(String.format("%02d", i + 1));
//            } else {
//                monthList.add(month_date.format(cal.getTime()));
//            }
            monthList.add(""+i+"월");
        }

        return monthList;
    }


    @Override
    protected String initDefault() {
        return String.valueOf(getMonth(today()));
    }

    public void setOnMonthSelectedListener(MonthSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (listener != null) {
            listener.onMonthSelected(this, position, item);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, String item) {
        if (lastScrollPosition != position) {
            onItemSelected(position, item);
            lastScrollPosition = position;
        }
    }

    public boolean displayMonthNumbers() {
        return displayMonthNumbers;
    }

    public void setDisplayMonthNumbers(boolean displayMonthNumbers) {
        this.displayMonthNumbers = displayMonthNumbers;
    }

    public int getCurrentMonth() {
        return getCurrentItemPosition();
    }

    public interface MonthSelectedListener {
        void onMonthSelected(WheelMonthPicker picker, int monthIndex, String monthName);
    }
}
