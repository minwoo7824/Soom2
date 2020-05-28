package com.kmw.soom2.Common.DateTimePicker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;


public class WheelSymptomPicker extends WheelPicker<String> {

    private SimpleDateFormat simpleDateFormat;
    protected int minYear;
    protected int maxYear;

    String[] values = {""};

    private OnSymptomSelectListener onSymptomSelectListener;

    public WheelSymptomPicker(Context context) {
        super(context);
    }

    public WheelSymptomPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelSymptomPicker(Context context, AttributeSet attrs, String[] values) {
        super(context, attrs);
        this.values = values;
    }
    @Override
    protected void init() {
        simpleDateFormat = new SimpleDateFormat("yyyy", getCurrentLocale());

        this.values = new String[]{"전체"};
    }
    public void setValues(String[] values){
        this.values = values;
        this.notifyDatasetChanged();
    }
    @Override
    protected String initDefault() {
        return getDefaultText();
    }

    @NonNull
    private String getDefaultText() {
        return "전체";
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (onSymptomSelectListener != null) {
            onSymptomSelectListener.onSymptomSelected(this, position, values[position]);
        }
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
        notifyDatasetChanged();
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
        notifyDatasetChanged();
    }

    @Override
    protected List<String> generateAdapterValues() {
//        final List<String> years = new ArrayList<>();
//
//        final Calendar instance = Calendar.getInstance();
//        instance.set(Calendar.YEAR, minYear-1);
//
//        for (int i = minYear; i <= maxYear; i++) {
//            instance.add(Calendar.YEAR, 1);
//            years.add(getFormattedValue(instance.getTime()));
//        }

//        return years;
        Log.i("WheelSymptomPicker", "picker values : " + values.toString());

        return Arrays.asList(values);
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnYearSelectedListener(OnSymptomSelectListener onSymptomSelectListener) {
        this.onSymptomSelectListener = onSymptomSelectListener;
    }

    public int getCurrentSymptom() {
        return convertItemToYear(super.getCurrentItemPosition());
    }

    private int convertItemToYear(int itemPosition) {
        return minYear + itemPosition;
    }

    public interface OnSymptomSelectListener {
        void onSymptomSelected(WheelSymptomPicker picker, int position, String value);
    }
}
