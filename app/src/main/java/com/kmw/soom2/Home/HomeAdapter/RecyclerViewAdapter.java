package com.kmw.soom2.Home.HomeAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Home.HomeActivity.AdultActivitys.AdultCheckResultActivity;
import com.kmw.soom2.Home.HomeActivity.DustRecordActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MedicineRecordEditActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.SymptomRecord;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.kmw.soom2.Common.Utils.formatYYYYMMDDHHMMSS;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.HEADER_TYPE;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.ITEM_TYPE;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "RecyclerViewAdapter";
    private ArrayList<RecyclerViewItemList> arrayList;

    Context context;

    String[] causes = new String[]{"감기","미세먼지","찬바람","운동","집먼지진드기","애완동물","음식알러지","꽃가루","스트레스","담배연기","요리연기","모르겠어요"};

    private Fragment fragment;
    Calendar calendar = Calendar.getInstance();
    Typeface typeface,typeface1;
    public RecyclerViewAdapter(Context context, ArrayList<RecyclerViewItemList> list, Fragment fragment) {
        this.context = context;
        this.arrayList = list;
        this.fragment = fragment;
        typeface = ResourcesCompat.getFont(context, R.font.notosanscjkkr_medium_regular);
        typeface1 = ResourcesCompat.getFont(context, R.font.notosanscjkkr_medium);
    }
    @Override
    public int getItemViewType(int position) {
        RecyclerViewItemList item = arrayList.get(position);
        if (item != null) {
            return item.getViewType();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_header, parent, false);
                return new RecyclerViewAdapter.ViewHolderHeader(view);
            case ITEM_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_view_item_list, parent, false);
                return new RecyclerViewAdapter.ViewHolderItem(view);
        }
        return null;
    }

    public String createSharedText(TreeMap<String, ArrayList<HistoryItems>> items) {
        String sharedText = "";
        TreeMap<String, ArrayList<HistoryItems>> tree = new TreeMap<>(items);

        for (Map.Entry<String, ArrayList<HistoryItems>> entry1 : tree.descendingMap().entrySet()) {
            String k = entry1.getKey();
            ArrayList<HistoryItems> value1 = entry1.getValue();

            System.out.println(value1);

            if (value1.get(0).getCategory() == 11 || value1.get(0).getCategory() == 12 || value1.get(0).getCategory() == 13 || value1.get(0).getCategory() == 14) {
                String category = String.valueOf(value1.get(0).getCategory());
                if (category.equals("11")) {
                    sharedText += "기침\n";
                } else if (category.equals("12")) {
                    sharedText += "호흡곤란\n";
                } else if (category.equals("13")) {
                    sharedText += "천명음\n";
                } else {
                    sharedText += "가슴답답함\n";
                }

                if (value1.get(0).getCause().length() != 0) {
                    String[] causeList = value1.get(0).getCause().split(",");
                    String cause = "";
                    for (int j = 0; j < causeList.length; j++) {
                        cause += "" + causes[Integer.parseInt(causeList[j])];
                    }
                    sharedText += "" + cause + "\n\n";
                }
            } else if (value1.get(0).getCategory() == 21) {
                sharedText += "천식조절 검사\n";
                if (value1.get(0).getActScore() != 0) {
                    if (value1.get(0).getActState() == 1) {
                        sharedText += "양호 - " + value1.get(0).getActScore() + " / 조절(양호)\n천식 증상이 잘 조절되고 있습니다.\n\n";
                    } else if (value1.get(0).getActScore() == 2) {
                        sharedText += "주의 - " + value1.get(0).getActScore() + " / 부분조절(주의)\n증상이 부분적으로만 조절되고 있습니다.\n\n";
                    } else {
                        sharedText += "위험 - " + value1.get(0).getActScore() + " / 조절안됨(위험)\n천식 증상이 조절되지 않고 있습니다.\n\n";
                    }
                }
            } else if (value1.get(0).getCategory() == 22) {
                sharedText += "폐기능\n";

                sharedText += "" + value1.get(0).getPefScore() + "\n";
                if (value1.get(0).getInspiratorFlag() == 1) {
                    sharedText += "흡입기 사용 이후 측정\n\n";
                } else {
                    sharedText += "흡입기 미사용\n\n";
                }
            } else if (value1.get(0).getCategory() == 23) {
                sharedText += "미세먼지\n";

                if (value1.get(0).getDust() != 0 && value1.get(0).getDustState() != 0 && value1.get(0).getUltraDust() != 0 && value1.get(0).getUltraDustState() != 0) {
                    String dustState = "";
                    if (value1.get(0).getDustState() == 1) {
                        dustState = "좋음";
                    } else if (value1.get(0).getDustState() == 2) {
                        dustState = "보통";
                    } else if (value1.get(0).getDustState() == 3) {
                        dustState = "나쁨";
                    } else if (value1.get(0).getDustState() == 4) {
                        dustState = "매우나쁨";
                    }
                    String ultraDustState = "";
                    if (value1.get(0).getUltraDustState() == 1) {
                        ultraDustState = "좋음";
                    } else if (value1.get(0).getUltraDustState() == 2) {
                        ultraDustState = "보통";
                    } else if (value1.get(0).getUltraDustState() == 3) {
                        ultraDustState = "나쁨";
                    } else if (value1.get(0).getUltraDustState() == 4) {
                        ultraDustState = "매우나쁨";
                    }
                    sharedText += "위치 - " + value1.get(0).getLocation() + "\n미세먼지 - " + value1.get(0).getDust() + " / " + dustState + "\n초미세먼지 - "
                            + value1.get(0).getUltraDust() + " / " + ultraDustState + "\n\n";
                } else {
                    sharedText += "위치 - " + value1.get(0).getLocation() + "\n\n";
                }
            } else if (value1.get(0).getCategory() == 30) {

            } else {
                sharedText += "\n복약\n";
//                    sharedText += value.get(i).getKo();
                sharedText += value1.stream().map(s->s.getKo()).collect(Collectors.joining("\n"));
                sharedText += "\n";
            }
        }
        return sharedText;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RecyclerViewItemList item = null;
        if (position < arrayList.size()){
            item = arrayList.get(position);

            switch (item.getViewType()) {
                case HEADER_TYPE:

//                if (holder instanceof ViewHolderHeader){

//                    try {
//                        calendar.setTime(formatYYYYMMDDHHMMSS.parse(item.getTitle()));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

//                    ((RecyclerViewAdapter.ViewHolderHeader) holder).title.setText(item.getTitle().substring(0,10)+"("+dayChoice(calendar)+")");
                    ((RecyclerViewAdapter.ViewHolderHeader) holder).title.setText(item.getTitle().substring(0,10));

                    RecyclerViewItemList finalItem1 = item;
                    ((ViewHolderHeader) holder).imageView.setOnClickListener(new View.OnClickListener() {
//                    ((RecyclerViewAdapter.ViewHolderHeader) holder).title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "TAG : " + TAG + " header clk : " + finalItem1.getHistoryItemList().size());
                            Intent i = new Intent(context, CommunityWriteActivity.class);
                            i.putExtra("sharedData", createSharedText(arrayList.get(position).getHistoryItemList()));

                            context.startActivity(i);
                        }
                    });
//                }
                    break;
                case ITEM_TYPE:

                    if (holder instanceof ViewHolderItem){

                        try {
                            ((RecyclerViewAdapter.ViewHolderItem) holder).txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(item.getEtcItem().getRegisterDt().substring(11,18))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (item.getMedicineKo().get(0).equals("11")){

                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("기침");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(" "+item.getEtcItem().getMemo());

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                        }else if (item.getMedicineKo().get(0).equals("12")){

                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("호흡곤란");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(" "+item.getEtcItem().getMemo());

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("13")){

                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("천명음");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(" "+item.getEtcItem().getMemo());

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("14")){

                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("가슴답답함");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(" "+item.getEtcItem().getMemo());

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모");

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("21")){

                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("천식조절검사");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.color3382b7));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.color3382b7));

                            if (item.getEtcItem().getActScore() != 0){
                                TextView textView = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textView.setLayoutParams(params);
                                textView.setTextColor(context.getResources().getColor(R.color.black));

                                Log.i(TAG,"score : " + item.getEtcItem().getActScore());

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);

                                if (item.getEtcItem().getActScore() == 25){
                                    textView.setText("양호 - " + item.getEtcItem().getActScore() + " / 조절(양호)\n천식 증상이 잘 조절되고 있습니다.");
                                }else if (item.getEtcItem().getActScore() >= 20){
                                    textView.setText("주의 - " + item.getEtcItem().getActScore() + " / 부분조절(주의)\n증상이 부분적으로만 조절되고 있습니다.");
                                }else{
                                    textView.setText("위험 - " + item.getEtcItem().getActScore() + " / 조절안됨(위험)\n천식 증상이 조절되지 않고 있습니다.");
                                }

                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                            }

                        }else if (item.getMedicineKo().get(0).equals("22")){

                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("폐기능");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.color8489e2));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.color8489e2));

                            if (item.getEtcItem().getPefScore() != 0){
                                TextView textView = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textView.setLayoutParams(params);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);

                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText(""+item.getEtcItem().getPefScore());
                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                            }

                            if (item.getEtcItem().getInspiratorFlag() != 0){
                                TextView textView = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textView.setLayoutParams(params);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);

                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                if (item.getEtcItem().getInspiratorFlag() == 1){
                                    textView.setText("흡입기 사용 이후 측정");
                                }else {
                                    textView.setText("흡입기 미사용");
                                }
                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                            }

                        }else if (item.getMedicineKo().get(0).equals("23")){
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("미세먼지");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.color3382b7));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.color3382b7));


                            TextView textView = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            textView.setLayoutParams(params);

                            textView.setTypeface(typeface1);
                            textView.setIncludeFontPadding(false);

                            textView.setTextColor(context.getResources().getColor(R.color.black));
                            String dustStatus = "";
                            String ultraDustStatus = "";

                            if (item.getEtcItem().getDustStatus() == 1){
                                dustStatus = "좋음";
                            }else if (item.getEtcItem().getDustStatus() == 2){
                                dustStatus = "보통";
                            }else if (item.getEtcItem().getDustStatus() == 3){
                                dustStatus = "나쁨";
                            }else if (item.getEtcItem().getDustStatus() == 4){
                                dustStatus = "매우나쁨";
                            }

                            if (item.getEtcItem().getUltraDustStatus() == 1){
                                ultraDustStatus = "좋음";
                            }else if (item.getEtcItem().getUltraDustStatus() == 2){
                                ultraDustStatus = "보통";
                            }else if (item.getEtcItem().getUltraDustStatus() == 3){
                                ultraDustStatus = "나쁨";
                            }else if (item.getEtcItem().getUltraDustStatus() == 4){
                                ultraDustStatus = "매우나쁨";
                            }
                            textView.setText("위치 - "+item.getEtcItem().getLocation() + "\n" + "미세먼지 - " + item.getEtcItem().getDust() + " / " + dustStatus + "\n" + "초미세먼지 - " + item.getEtcItem().getUltraDust() + " / " + ultraDustStatus);
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);

                        }else if (item.getMedicineKo().get(0).equals("30")){
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            if (item.getEtcItem().getImageFile().length() != 0){
                                ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.VISIBLE);
                            }else{
                                ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            }
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("메모");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.ff6767));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.ff6767));

                            TextView textView = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            textView.setLayoutParams(params);

                            textView.setTypeface(typeface1);
                            textView.setIncludeFontPadding(false);

                            textView.setTextColor(context.getResources().getColor(R.color.black));
                            textView.setText(""+item.getEtcItem().getMemo());
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);

                        }else{
                            if (item.getMedicineKo().size() != 0){
                                ((RecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                                ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                                for (int i = 0; i < item.getMedicineKo().size(); i++){
                                    TextView textView = new TextView(context);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textView.setLayoutParams(params);

                                    textView.setTypeface(typeface1);
                                    textView.setIncludeFontPadding(false);

                                    textView.setTextColor(context.getResources().getColor(R.color.black));
                                    textView.setText(item.getMedicineKo().get(i));
                                    ((RecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                                }
                            }

                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setText("약");
                            ((RecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            ((RecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        }

                        final RecyclerViewItemList finalItem = item;

                        ((RecyclerViewAdapter.ViewHolderItem) holder).linParent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (finalItem.getMedicineKo().get(0).equals("11") || finalItem.getMedicineKo().get(0).equals("12") ||
                                        finalItem.getMedicineKo().get(0).equals("13") || finalItem.getMedicineKo().get(0).equals("14")){
                                    Intent i = new Intent(context, SymptomRecord.class);
                                    i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                    i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                    i.putExtra("category", finalItem.getMedicineKo().get(0));
                                    i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                    i.putExtra("cause", finalItem.getEtcItem().getCause());
                                    i.putExtra("memo", finalItem.getEtcItem().getMemo());
                                    fragment.startActivityForResult(i,1111);
                                }else if (finalItem.getMedicineKo().get(0).equals("21")){
                                    Intent i = new Intent(context, AdultCheckResultActivity.class);
                                    i.putExtra("intoHomeFeed",true);
                                    i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                    i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                    i.putExtra("category", finalItem.getMedicineKo().get(0));
                                    i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                    i.putExtra("resultType", finalItem.getEtcItem().getActType());
                                    i.putExtra("kidsScore", finalItem.getEtcItem().getActSelected());
                                    i.putExtra("score",finalItem.getEtcItem().getActScore());
                                    i.putExtra("homeFragment",true);
                                    fragment.startActivityForResult(i,1111);
                                }else if (finalItem.getMedicineKo().get(0).equals("22")){
                                    Intent i = new Intent(context, BreathRecordActivity.class);
                                    i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                    i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                    i.putExtra("category", finalItem.getMedicineKo().get(0));
                                    i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                    i.putExtra("pefScore", finalItem.getEtcItem().getPefScore());
                                    i.putExtra("inspiratorFlag", finalItem.getEtcItem().getInspiratorFlag());
                                    fragment.startActivityForResult(i,1111);

                                }else if (finalItem.getMedicineKo().get(0).equals("23")){
                                    Intent i = new Intent(context, DustRecordActivity.class);
                                    i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                    i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                    i.putExtra("category", finalItem.getMedicineKo().get(0));
                                    i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                    i.putExtra("location",finalItem.getEtcItem().getLocation());
                                    i.putExtra("dust",finalItem.getEtcItem().getDust());
                                    i.putExtra("ultraDust",finalItem.getEtcItem().getUltraDust());
                                    i.putExtra("dustStatus",finalItem.getEtcItem().getDustStatus());
                                    i.putExtra("ultraDustStatus",finalItem.getEtcItem().getUltraDustStatus());
                                    i.putExtra("lat",finalItem.getEtcItem().getLat());
                                    i.putExtra("lng",finalItem.getEtcItem().getLng());
                                    fragment.startActivityForResult(i,1111);
                                }else if (finalItem.getMedicineKo().get(0).equals("30")){
                                    Intent i = new Intent(context, MemoActivity.class);
                                    i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                    i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                    i.putExtra("category", finalItem.getMedicineKo().get(0));
                                    i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                    i.putExtra("memo", finalItem.getEtcItem().getMemo());
                                    i.putExtra("imgsPath",finalItem.getEtcItem().getImageFile());
                                    fragment.startActivityForResult(i,1111);
                                }else{
                                    Intent i = new Intent(context, MedicineRecordEditActivity.class);
                                    i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                    i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                    i.putExtra("medicineTypeNo", finalItem.getEtcItem().getMedicineTypeNo());
                                    i.putExtra("volume", finalItem.getEtcItem().getVolume());
                                    i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                    i.putExtra("unit", finalItem.getEtcItem().getUnit());
                                    i.putExtra("emergency", finalItem.getEtcItem().getEmergencyFlag());
                                    i.putExtra("ko",finalItem.getMedicineKo().get(0));
                                    fragment.startActivityForResult(i,1111);
                                }
                            }
                        });
                    }
                    break;
            }
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.list_item_section_text);
            imageView = itemView.findViewById(R.id.direction_button);

            itemView.setTag(true);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        public TextView title,txtTime;
        public LinearLayout linParent,linListParent;
        public LinearLayout linColor;
        public ImageView imgMemo;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imgMemo = (ImageView)itemView.findViewById(R.id.img_memo_path);
            title = (TextView)itemView.findViewById(R.id.title_text_view);
            txtTime = (TextView)itemView.findViewById(R.id.time_text_view);
            linListParent = (LinearLayout)itemView.findViewById(R.id.lin_recycler_View_list_item_parent);
            linParent = (LinearLayout)itemView.findViewById(R.id.lin_recycler_view_parent);
            linColor = (LinearLayout)itemView.findViewById(R.id.recycler_view_color);

            itemView.setTag(false);
        }
    }

    private String dayChoice(Calendar calendar){

        String day = "";

        if (calendar.get(Calendar.DAY_OF_WEEK) == 1){
            day = "일";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 2){
            day = "월";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 3){
            day = "화";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 4){
            day = "수";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 5){
            day = "목";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 6){
            day = "금";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 7){
            day = "토";
        }

        return day;
    }


}
