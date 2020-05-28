package com.kmw.soom2.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.kmw.soom2.StaticFunc.Activitys.StaticMedicineActivity;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StaticMedicineCalendarView extends LinearLayout {

    // ************************************************************************************************************************************************************************
    // * Attributes
    // ************************************************************************************************************************************************************************

    final String TAG = "StaticMedicineCalendarView";

    // View
    private Context context;
    public static TextView dateTitle;
    private LinearLayout leftButton;
    private LinearLayout rightButton;
    private View rootView;
    private ViewGroup robotoCalendarMonthLayout;

    // Class
    private CalendarView.RobotoCalendarListener robotoCalendarListener;
    public Calendar currentCalendar;
    private Calendar lastSelectedDayCalendar;

    private static final String DAY_OF_THE_WEEK_TEXT = "dayOfTheWeekText";
    private static final String DAY_OF_THE_WEEK_LAYOUT = "dayOfTheWeekLayout";

    private static final String DAY_OF_THE_MONTH_LAYOUT = "dayOfTheMonthLayout";
    private static final String DAY_OF_THE_MONTH_TEXT = "dayOfTheMonthText";
    private static final String DAY_OF_THE_MONTH_BACKGROUND = "dayOfTheMonthBackground";
    private static final String DAY_OF_THE_MONTH_CIRCLE_IMAGE_1 = "dayOfTheMonthCircleImage1";
    private static final String DAY_OF_THE_MONTH_CIRCLE_IMAGE_2 = "dayOfTheMonthCircleImage2";

    private boolean shortWeekDays = false;

    SimpleDateFormat dataFormatYYYYMM = new SimpleDateFormat("yyyy년 MM월");

    public ArrayList<HistoryItems> items = new ArrayList<>();

    // ************************************************************************************************************************************************************************
    // * Initialization methods
    // ************************************************************************************************************************************************************************

    public StaticMedicineCalendarView(Context context) {
        super(context);
        this.context = context;
        onCreateView();
    }

    public StaticMedicineCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        onCreateView();
    }

    private View onCreateView() {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflate.inflate(R.layout.calendar_picker_layout, this, true);
        findViewsById(rootView);
        setUpEventListeners();
        setUpCalligraphy();
        return rootView;
    }

    private void findViewsById(View view) {

        robotoCalendarMonthLayout = (ViewGroup) view.findViewById(R.id.robotoCalendarDateTitleContainer);
        leftButton = (LinearLayout) view.findViewById(R.id.leftButton);
        rightButton = (LinearLayout) view.findViewById(R.id.rightButton);
        dateTitle = (TextView) view.findViewById(R.id.monthText);

        for (int i = 0; i < 42; i++) {

            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int weekIndex = (i % 7) + 1;
            ViewGroup dayOfTheWeekLayout = (ViewGroup) view.findViewWithTag(DAY_OF_THE_WEEK_LAYOUT + weekIndex);

            // Create day of the month
            View dayOfTheMonthLayout = inflate.inflate(R.layout.calendar_day_of_the_month_layout, null);
            View dayOfTheMonthText = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_TEXT);
            View dayOfTheMonthBackground = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_BACKGROUND);
            View dayOfTheMonthCircleImage1 = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1);
            View dayOfTheMonthCircleImage2 = dayOfTheMonthLayout.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2);

            // Set tags to identify them
            int viewIndex = i + 1;
            dayOfTheMonthLayout.setTag(DAY_OF_THE_MONTH_LAYOUT + viewIndex);
            dayOfTheMonthText.setTag(DAY_OF_THE_MONTH_TEXT + viewIndex);
            dayOfTheMonthBackground.setTag(DAY_OF_THE_MONTH_BACKGROUND + viewIndex);
            dayOfTheMonthCircleImage1.setTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1 + viewIndex);
            dayOfTheMonthCircleImage2.setTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2 + viewIndex);

            dayOfTheWeekLayout.addView(dayOfTheMonthLayout);
        }
    }

    private void setUpEventListeners() {

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (robotoCalendarListener == null) {
                    throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
                }

                // Decrease month
                currentCalendar.add(Calendar.MONTH, -1);
                lastSelectedDayCalendar = null;
                updateView();
                robotoCalendarListener.onLeftButtonClick(currentCalendar);

                StaticMedicineActivity.txtCalendarTitle.setText(dataFormatYYYYMM.format(currentCalendar.getTime()));

                rightButton.setEnabled(true);
                rightButton.setClickable(true);

                for (int i = 0; i < rightButton.getChildCount(); i++){
                    View v = rightButton.getChildAt(i);
                    if (v instanceof ImageView){
                        ((ImageView) v).setImageResource(R.drawable.ic_black_arrow_right);
                        ((ImageView) v).setColorFilter(getResources().getColor(R.color.colorPrimary));
                        ((ImageView) v).setRotation(0);
                    }else if (v instanceof TextView){
                        ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (robotoCalendarListener == null) {
                    throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
                }

                currentCalendar.add(Calendar.MONTH, 1);
                lastSelectedDayCalendar = null;
                updateView();
                robotoCalendarListener.onRightButtonClick(currentCalendar);

                StaticMedicineActivity.txtCalendarTitle.setText(dataFormatYYYYMM.format(currentCalendar.getTime()));

                // Increase month
                Calendar calendar = Calendar.getInstance();
                if ((currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) && (currentCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))) {
                    rightButton.setEnabled(false);
                    rightButton.setClickable(false);
                    for (int i = 0; i < rightButton.getChildCount(); i++){
                        View v = rightButton.getChildAt(i);
                        if (v instanceof ImageView){
                            ((ImageView) v).setImageResource(R.drawable.ic_black_arrow_right);
                            ((ImageView) v).setColorFilter(getResources().getColor(R.color.black));
                        }else if (v instanceof TextView){
                            ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                        }
                    }
                }else{

                }
            }
        });
    }

    private void setUpCalligraphy() {
        // Initialize calendar for current month
        Calendar currentCalendar = Calendar.getInstance();
        setCalendar(currentCalendar);
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }

    // ************************************************************************************************************************************************************************
    // * Auxiliary UI methods
    // ************************************************************************************************************************************************************************

    private void setUpMonthLayout() {
        String dateText = new DateFormatSymbols(Locale.KOREA).getMonths()[currentCalendar.get(Calendar.MONTH)];
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd일");

        if (dateText.length() == 2){
            dateText = "0"+dateText;
        }

        if (currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            if (currentCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)){
                dateTitle.setText(calendar.get(Calendar.YEAR) + "년" + dateText );
//                dateTitle.setText(calendar.get(Calendar.YEAR) + "년" + dateText + simpleDateFormat.format(new Date(System.currentTimeMillis())));

                rightButton.setEnabled(false);
                rightButton.setClickable(false);
                for (int i = 0; i < rightButton.getChildCount(); i++){
                    View v = rightButton.getChildAt(i);
                    if (v instanceof ImageView){
                        ((ImageView) v).setImageResource(R.drawable.ic_black_arrow_right);
                        ((ImageView) v).setColorFilter(getResources().getColor(R.color.black));
                    }else if (v instanceof TextView){
                        ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                    }
                }

            }else{
                dateTitle.setText(calendar.get(Calendar.YEAR) + "년" + dateText);
            }
        } else {
            dateTitle.setText(String.format("%s년%s", currentCalendar.get(Calendar.YEAR),  dateText));
        }
    }

    private void setUpWeekDaysLayout() {
        TextView dayOfWeek;
        String dayOfTheWeekString;
        String[] weekDaysArray = new DateFormatSymbols(Locale.KOREA).getWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++) {
            dayOfWeek = (TextView) rootView.findViewWithTag(DAY_OF_THE_WEEK_TEXT + getWeekIndex(i, currentCalendar));
            dayOfTheWeekString = weekDaysArray[i];
            if (shortWeekDays) {
                dayOfTheWeekString = checkSpecificLocales(dayOfTheWeekString, i);
            } else {
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase() + dayOfTheWeekString.substring(1, 1);
            }

            dayOfWeek.setText(dayOfTheWeekString);
            dayOfWeek.setTextColor(Color.argb(96,0,0,0));
        }
    }

    // 리스트 클릭시 해당하는 날짜에 표시 해주기 위한 세팅
    public void setCalendarItem(ArrayList<HistoryItems> item) {
        items = new ArrayList<>();
        this.items = item;
        updateView();
    }

    private void setUpDaysOfMonthLayout() {

        TextView dayOfTheMonthText;
        View circleImage1;
        View circleImage2;
        ViewGroup dayOfTheMonthContainer;
        ViewGroup dayOfTheMonthBackground;

        for (int i = 1; i < 43; i++) {

            dayOfTheMonthContainer = (ViewGroup) rootView.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + i);
            dayOfTheMonthBackground = (ViewGroup) rootView.findViewWithTag(DAY_OF_THE_MONTH_BACKGROUND + i);
            dayOfTheMonthText = (TextView) rootView.findViewWithTag(DAY_OF_THE_MONTH_TEXT + i);
            circleImage1 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1 + i);
            circleImage2 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2 + i);

            dayOfTheMonthText.setVisibility(INVISIBLE);
            circleImage1.setVisibility(GONE);
            circleImage2.setVisibility(GONE);

            // Apply styles
            dayOfTheMonthText.setBackgroundResource(android.R.color.transparent);
            dayOfTheMonthText.setTypeface(null, Typeface.NORMAL);
            dayOfTheMonthText.setTextColor(ContextCompat.getColor(context, R.color.calendar_day_of_the_month_font));
            dayOfTheMonthContainer.setBackgroundResource(android.R.color.transparent);
            dayOfTheMonthContainer.setOnClickListener(null);
            dayOfTheMonthBackground.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void setUpDaysInCalendar() {
        Log.i(TAG, "setUpDaysInCalendar");
        Calendar auxCalendar = Calendar.getInstance(Locale.getDefault());
        auxCalendar.setTime(currentCalendar.getTime());
        auxCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);
        TextView dayOfTheMonthText;
        ViewGroup dayOfTheMonthContainer;
        ViewGroup dayOfTheMonthLayout;

        ImageView circleImage1;
        ImageView circleImage2;

        // Calculate dayOfTheMonthIndex
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(System.currentTimeMillis()));

        int dayOfTheMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

        for (int i = 1; i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++, dayOfTheMonthIndex++) {
            dayOfTheMonthContainer = (ViewGroup) rootView.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + dayOfTheMonthIndex);
            dayOfTheMonthText = (TextView) rootView.findViewWithTag(DAY_OF_THE_MONTH_TEXT + dayOfTheMonthIndex);
            if (dayOfTheMonthText == null) {
                break;
            }
            circleImage1 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1 + dayOfTheMonthIndex);
            circleImage2 = rootView.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2 + dayOfTheMonthIndex);

            dayOfTheMonthContainer.setOnClickListener(onDayOfMonthClickListener);
            dayOfTheMonthContainer.setOnLongClickListener(onDayOfMonthLongClickListener);
            dayOfTheMonthText.setVisibility(VISIBLE);
            dayOfTheMonthText.setText(String.valueOf(i));

            if ((currentCalendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR)) && (currentCalendar.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH))){
                if (i > calendar1.get(Calendar.DAY_OF_MONTH)){
                    dayOfTheMonthText.setTextColor(Color.argb(125,0,0,0));
                }else{
                    dayOfTheMonthText.setTextColor(Color.argb(255,0,0,0));
                }
            }else{
                dayOfTheMonthText.setTextColor(Color.argb(255,0,0,0));
            }
            if (items.size() > 0) {

                final String month = (currentCalendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (currentCalendar.get(Calendar.MONTH) + 1) : "" + (currentCalendar.get(Calendar.MONTH) + 1);
                final String year = "" + currentCalendar.get(Calendar.YEAR);
                final String day = (String.valueOf(i).length() < 2) ? "0" + String.valueOf(i) : "" + String.valueOf(i);


                Boolean normFlag = (items.stream().filter(new Predicate<HistoryItems>() {
                    @Override
                    public boolean test(HistoryItems historyItems) {
                        return (historyItems.getRegisterDt().substring(0, 10).equals(year + "-" + month + "-" + day)) ? true : false;
                    }
                }).filter(new Predicate<HistoryItems>() {
                    @Override
                    public boolean test(HistoryItems historyItems) {
                        return (historyItems.getEmergencyFlag() == 1) ? true : false;
                    }
                }).count() > 0);

                Boolean emergencyFlag = (items.stream().filter(new Predicate<HistoryItems>() {
                    @Override
                    public boolean test(HistoryItems historyItems) {
                        return (historyItems.getRegisterDt().substring(0, 10).equals(year + "-" + month + "-" + day)) ? true : false;
                    }
                }).filter(new Predicate<HistoryItems>() {
                    @Override
                    public boolean test(HistoryItems historyItems) {
                        return (historyItems.getEmergencyFlag() == 2) ? true : false;
                    }
                }).count() > 0);

                if (normFlag && emergencyFlag) {
                    circleImage1.setVisibility(VISIBLE);
                    circleImage2.setVisibility(VISIBLE);

                    circleImage1.setBackgroundResource(R.color.colorPrimary);
                    circleImage2.setBackgroundResource(R.color.ff6767);
                }else if (normFlag && !emergencyFlag) {
                    circleImage1.setVisibility(VISIBLE);
                    circleImage2.setVisibility(VISIBLE);

                    circleImage1.setBackgroundResource(R.color.colorPrimary);
                    circleImage2.setBackgroundResource(R.color.colorPrimary);
                }else if (!normFlag && emergencyFlag) {
                    circleImage1.setVisibility(VISIBLE);
                    circleImage2.setVisibility(VISIBLE);

                    circleImage1.setBackgroundResource(R.color.ff6767);
                    circleImage2.setBackgroundResource(R.color.ff6767);
                }else {
                    circleImage1.setVisibility(INVISIBLE);
                    circleImage2.setVisibility(INVISIBLE);
                }





//                for (int o = 0; o < items.size(); o++) {
//                    if (items.get(o).getRegisterDt().substring(0, 10).equals(year + "-" + month + "-" + day)) {
//                        Boolean normFlag = items.get(o).getEmergencyFlag() == 1 ? true : false;
//                        Boolean emergencyFlag = items.get(o).getEmergencyFlag() == 2 ? true : false;
//                        Log.i(TAG, "normFlag " + normFlag + ", emergencyFlag " + emergencyFlag + ", item size : " + items.size());
//
//                        if (normFlag && emergencyFlag) {
//                            circleImage1.setVisibility(VISIBLE);
//                            circleImage2.setVisibility(VISIBLE);
//
//                            circleImage1.setBackgroundResource(R.color.colorPrimary);
//                            circleImage2.setBackgroundResource(R.color.ff6767);
//                        }else if (normFlag && !emergencyFlag) {
//                            circleImage1.setVisibility(VISIBLE);
//                            circleImage2.setVisibility(VISIBLE);
//
//                            circleImage1.setBackgroundResource(R.color.colorPrimary);
//                            circleImage2.setBackgroundResource(R.color.colorPrimary);
//                        }else if (!normFlag && emergencyFlag) {
//                            circleImage1.setVisibility(VISIBLE);
//                            circleImage2.setVisibility(VISIBLE);
//
//                            circleImage1.setBackgroundResource(R.color.ff6767);
//                            circleImage2.setBackgroundResource(R.color.ff6767);
//                        }else {
//                            circleImage1.setVisibility(INVISIBLE);
//                            circleImage2.setVisibility(INVISIBLE);
//                        }
//                    }
//                }
            }
        }


        for (int i = 36; i < 43; i++) {
            dayOfTheMonthText = (TextView) rootView.findViewWithTag(DAY_OF_THE_MONTH_TEXT + i);
            dayOfTheMonthLayout = (ViewGroup) rootView.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + i);
            if (dayOfTheMonthText.getVisibility() == INVISIBLE) {
                dayOfTheMonthLayout.setVisibility(GONE);
            } else {
                dayOfTheMonthLayout.setVisibility(VISIBLE);
            }
            if (auxCalendar.after(calendar1)){
                dayOfTheMonthText.setTextColor(Color.argb(125,0,0,0));
            }else{
                dayOfTheMonthText.setTextColor(Color.argb(255,0,0,0));
            }
        }
    }

    void getFlagInDatas(String stDay, ArrayList<HistoryItems> items) {
        if (items.size() > 0) {
            final String month = (currentCalendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (currentCalendar.get(Calendar.MONTH) + 1) : "" + (currentCalendar.get(Calendar.MONTH) + 1);
            final String year = "" + currentCalendar.get(Calendar.YEAR);
            final String day = (stDay.length() < 2) ? "0" + stDay : "" + stDay;


            List<Object> filterDatas = items.stream().filter(new Predicate<HistoryItems>() {
                @Override
                public boolean test(HistoryItems f) {
                    Log.i(TAG, f.getRegisterDt());
                    if ( f.getRegisterDt().substring(0, 10) == year + "-" + month + "-" + day) {
                        return true;
                    }
                    return false;
                }
            }).collect(Collectors.toList());
//            Log.i(TAG, "year + \"-\" + month + \"-\" + day : " + year + "-" + month + "-" + day + ", " + filterDatas.toString());
        }
    }

    private void markDayAsCurrentDay() {
        // If it's the current month, mark current day
        Calendar nowCalendar = Calendar.getInstance();
        if (nowCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && nowCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(nowCalendar.getTime());

            TextView dayOfText = getDayOfMonthText(currentCalendar);
            dayOfText.setBackgroundResource(R.drawable.custom_primary_50dp);
            dayOfText.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void markDayAsSelectedDay(Calendar calendar) {

        // Clear previous current day mark
        clearSelectedDay(lastSelectedDayCalendar);

        Calendar nowCalendar = Calendar.getInstance();
        if (nowCalendar != calendar){
            TextView dayOfTheMonth = getDayOfMonthText(nowCalendar);
            dayOfTheMonth.setTextColor(ContextCompat.getColor(context, R.color.calendar_day_of_the_week_font));
            dayOfTheMonth.setBackgroundResource(android.R.color.transparent);
        }

        // Store current values as last values
        lastSelectedDayCalendar = calendar;

        TextView dayOfTheMonth = getDayOfMonthText(calendar);
        dayOfTheMonth.setTextColor(getResources().getColor(R.color.white));
        dayOfTheMonth.setBackgroundResource(R.drawable.custom_primary_50dp);
    }

    private void clearSelectedDay(Calendar calendar) {
        if (calendar != null) {

            TextView dayOfTheMonth = getDayOfMonthText(calendar);
            dayOfTheMonth.setTextColor(ContextCompat.getColor(context, R.color.calendar_day_of_the_week_font));
            dayOfTheMonth.setBackgroundResource(android.R.color.transparent);

            ImageView circleImage1 = getCircleImage1(calendar);
//            ImageView circleImage2 = getCircleImage2(calendar);

        }
    }

    private String checkSpecificLocales(String dayOfTheWeekString, int i) {
        // Set Wednesday as "X" in Spanish Locale.getDefault()
        if (i == 4 && Locale.getDefault().getCountry().equals("ES")) {
            dayOfTheWeekString = "X";
        } else {
            dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase();
        }
        return dayOfTheWeekString;
    }

    // ************************************************************************************************************************************************************************
    // * Public calendar methods
    // ************************************************************************************************************************************************************************

    /**
     * Set an specific calendar to the view
     *
     * @param calendar
     */
    public void setCalendar(Calendar calendar) {
        this.currentCalendar = calendar;
        Log.i(TAG, "StaticMedicineActivity currentCalendar date : " + currentCalendar.get(Calendar.MONTH));
        updateView();
    }

    /**
     * Update the calendar view
     */
    public void updateView() {
        setUpMonthLayout();
        setUpWeekDaysLayout();
        setUpDaysOfMonthLayout();
        setUpDaysInCalendar();
//        markDayAsCurrentDay();    // 클릭 이벤트가 필요 없음 달력에서는
    }

    public void setShortWeekDays(boolean shortWeekDays) {
        this.shortWeekDays = shortWeekDays;
    }

    /**
     * Clear the view of marks and selections
     */
    public void clearCalendar() {
        updateView();
    }

    public void markCircleImage1(Calendar calendar) {
        ImageView circleImage1 = getCircleImage1(calendar);
        circleImage1.setVisibility(VISIBLE);
        DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(context, R.color.calendar_circle_ovulation_low));
    }

    public void markCircleImageMedication(Calendar calendar, int status) {
        ImageView circleImage1 = getCircleImage1(calendar);
        circleImage1.setImageResource(R.drawable.custom_primary_50dp);
        if (status == 1){
            circleImage1.setColorFilter(getResources().getColor(R.color.staticsGreen));
        }else if (status == 2){
            circleImage1.setColorFilter(getResources().getColor(R.color.staticsYellow));
        }else if (status == 3){
            circleImage1.setColorFilter(getResources().getColor(R.color.staticsRed));
        }

        circleImage1.setVisibility(VISIBLE);
        // DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(context, R.color.calendar_circle_ovulation_low));
    }

    public void markCircleImageSymptom(Calendar calendar) {
        ImageView circleImage2 = getCircleImage2(calendar);
        circleImage2.setImageResource(R.drawable.ovulation_circle_normal);
        circleImage2.setVisibility(VISIBLE);
        // DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(context, R.color.calendar_circle_ovulation_low));
    }

    public void markCircleImageNormal(Calendar calendar) {
        ImageView circleImage1 = getCircleImage1(calendar);
        circleImage1.setVisibility(VISIBLE);
        circleImage1.setImageResource(R.drawable.ovulation_circle_normal);

        // DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(context, R.color.calendar_circle_ovulation_normal));
    }
    public void markCircleImageHigh(Calendar calendar) {
        ImageView circleImage1 = getCircleImage1(calendar);
        circleImage1.setVisibility(VISIBLE);
        circleImage1.setImageResource(R.drawable.ovulation_circle_high);

        // DrawableCompat.setTint(circleImage1.getDrawable(), ContextCompat.getColor(context, R.color.calendar_circle_ovulation_high));
    }

    public void markCircleImage2(Calendar calendar) {
        ImageView circleImage2 = getCircleImage2(calendar);
        circleImage2.setVisibility(VISIBLE);
        DrawableCompat.setTint(circleImage2.getDrawable(), ContextCompat.getColor(context, R.color.calendar_circle_ovulation_normal));
    }

    public void showDateTitle(boolean show) {
        if (show) {
            robotoCalendarMonthLayout.setVisibility(VISIBLE);
        } else {
            robotoCalendarMonthLayout.setVisibility(GONE);
        }
    }

    // ************************************************************************************************************************************************************************
    // * Public interface
    // ************************************************************************************************************************************************************************

    public interface RobotoCalendarListener {

        void onDayClick(Calendar daySelectedCalendar);

        void onDayLongClick(Calendar daySelectedCalendar);

        void onRightButtonClick();

        void onLeftButtonClick();
    }

    public void setRobotoCalendarListener(CalendarView.RobotoCalendarListener robotoCalendarListener) {
        this.robotoCalendarListener = robotoCalendarListener;
    }

    // ************************************************************************************************************************************************************************
    // * Event handler methods
    // ************************************************************************************************************************************************************************

    private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            // Extract day selected
            ViewGroup dayOfTheMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfTheMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_THE_MONTH_LAYOUT.length(), tagId.length());
            TextView dayOfTheMonthText = (TextView) view.findViewWithTag(DAY_OF_THE_MONTH_TEXT + tagId);

            // Extract the day from the text
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dayOfTheMonthText.getText().toString()));

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date(System.currentTimeMillis()));
            if (calendar.after(calendar1)){

            }else{
                markDayAsSelectedDay(calendar);

                String month = String.valueOf((currentCalendar.get(Calendar.MONTH) + 1));
                String day = dayOfTheMonthText.getText().toString();

                if (month.length() == 1){
                    month = "0"+month;
                }
                if (day.length() == 1){
                    day = "0"+day;
                }
                dateTitle.setText(currentCalendar.get(Calendar.YEAR) + "년" + month + "월");
//                dateTitle.setText(currentCalendar.get(Calendar.YEAR) + "년" + month + "월" + day +"일");
            }

            // Fire event
            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
            } else {
                robotoCalendarListener.onDayClick(calendar);
            }
        }
    };

    private OnLongClickListener onDayOfMonthLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

            // Extract day selected
            ViewGroup dayOfTheMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfTheMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_THE_MONTH_LAYOUT.length(), tagId.length());
            TextView dayOfTheMonthText = (TextView) view.findViewWithTag(DAY_OF_THE_MONTH_TEXT + tagId);

            // Extract the day from the text
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dayOfTheMonthText.getText().toString()));

            markDayAsSelectedDay(calendar);

            // Fire event
            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assign a valid RobotoCalendarListener first!");
            } else {
                robotoCalendarListener.onDayLongClick(calendar);
            }
            return true;
        }
    };

    // ************************************************************************************************************************************************************************
    // * Getter methods
    // ************************************************************************************************************************************************************************

    private ViewGroup getDayOfMonthBackground(Calendar currentCalendar) {
        return (ViewGroup) getView(DAY_OF_THE_MONTH_BACKGROUND, currentCalendar);
    }

    private TextView getDayOfMonthText(Calendar currentCalendar) {
        return (TextView) getView(DAY_OF_THE_MONTH_TEXT, currentCalendar);
    }

    private ImageView getCircleImage1(Calendar currentCalendar) {
        return (ImageView) getView(DAY_OF_THE_MONTH_CIRCLE_IMAGE_1, currentCalendar);
    }

    private ImageView getCircleImage2(Calendar currentCalendar) {
        return (ImageView) getView(DAY_OF_THE_MONTH_CIRCLE_IMAGE_2, currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return currentDay + monthOffset;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {

            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();

        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {

            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    private View getView(String key, Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        return rootView.findViewWithTag(key + index);
    }

    public void leftClick(){

        leftButton.performClick();
    }

    public void rightClick(){
        rightButton.performClick();
    }
}

