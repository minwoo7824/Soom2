package com.kmw.soom2.CommunityFragmentFunc.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunityDetailActivity;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunityWriteActivity;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.LikeActivity;
import com.kmw.soom2.CommunityFragmentFunc.CommunityFragment;
import com.kmw.soom2.CommunityFragmentFunc.Items.CommunityItems;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;
import static com.kmw.soom2.Common.Utils.likeItemArrayList;
import static com.kmw.soom2.Common.Utils.scrapItemArrayList;
import static com.kmw.soom2.CommunityFragmentFunc.CommunityFragment.Twopopup;

public class CommunityAdapter extends BaseAdapter {

    private String TAG = "CommunityAdapter";
    Context context;
    ArrayList<CommunityItems> itemsArrayList = new ArrayList<>();
    CommunityViewPagerAdapter adapter;
    Display display;
    WindowManager wm;
    private Fragment fragment;

    public CommunityAdapter(Context context,Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
    }


    @Override
    public int getCount() {
        return itemsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_community_list_item,null);

            viewHolder.imgProfile = (ImageView)convertView.findViewById(R.id.img_community_list_item_profile);
            viewHolder.imgMore = (ImageView)convertView.findViewById(R.id.img_community_list_item_more);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txt_community_list_item_name);
            viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_community_list_item_date);
            viewHolder.txtContents = (TextView)convertView.findViewById(R.id.txt_community_list_item_contents);
            viewHolder.txtLike = (TextView)convertView.findViewById(R.id.txt_community_list_item_like_cnt);
            viewHolder.txtComment = (TextView)convertView.findViewById(R.id.txt_community_list_item_comment_cnt);
            viewHolder.viewPager = (ViewPager)convertView.findViewById(R.id.view_pager_community_list_item_picture);
            viewHolder.imgLike = (ImageView)convertView.findViewById(R.id.img_community_list_item_like_icon);
            viewHolder.txtTag = (TextView)convertView.findViewById(R.id.txt_community_list_item_tag);
            viewHolder.imgComment = (ImageView)convertView.findViewById(R.id.img_community_list_item_comment_icon);
            viewHolder.imgCopy = (ImageView)convertView.findViewById(R.id.img_community_list_item_copy_icon);
            viewHolder.tabLayout = (TabLayout)convertView.findViewById(R.id.tab_layout_community_list_item);
            viewHolder.tabStip = ((LinearLayout)viewHolder.tabLayout.getChildAt(0));
            viewHolder.frameLayout = (FrameLayout)convertView.findViewById(R.id.frame_community_list_item);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions();

        if (itemsArrayList.get(position).getProfile().length() > 0){
            if (itemsArrayList.get(position).getProfile().contains("http:")){
                Glide.with(context).load(itemsArrayList.get(position).getProfile()).apply(requestOptions.circleCrop()).into(viewHolder.imgProfile);
            }else{
                Glide.with(context).load("http:" + itemsArrayList.get(position).getProfile()).apply(requestOptions.circleCrop()).into(viewHolder.imgProfile);
            }
        }else{
            Glide.with(context).load(R.drawable.ic_user_profile).into(viewHolder.imgProfile);
        }

        viewHolder.txtName.setText(itemsArrayList.get(position).getName());
        viewHolder.txtDate.setText(itemsArrayList.get(position).getDate());

        final Context context = viewHolder.txtContents.getContext();
        final String expanedText = " ... 더보기";

        if (viewHolder.txtContents.getTag() != null && viewHolder.txtContents.getTag().equals(itemsArrayList.get(position).getContents())) { //Tag로 전값 의 text를 비교하여똑같으면 실행하지 않음.

        }else{
            viewHolder.txtContents.setTag(itemsArrayList.get(position).getContents()); //Tag에 text 저장
            viewHolder.txtContents.setText(itemsArrayList.get(position).getContents().replace("\n","")); // setText를 미리 하셔야  getLineCount()를 호출가능
            ViewHolder finalViewHolder = viewHolder;

            viewHolder.txtContents.post(new Runnable() { //getLineCount()는 UI 백그라운드에서만 가져올수 있음
                @Override
                public void run() {

                    if (finalViewHolder.txtContents.getLineCount() >= 4) { //Line Count가 설정한 MaxLine의 값보다 크다면 처리시작

                        int lineEndIndex = finalViewHolder.txtContents.getLayout().getLineVisibleEnd(4 - 1); //Max Line 까지의 text length

                        String[] split = new String[finalViewHolder.txtContents.getLineCount()];

                        for (int x = 0; x < finalViewHolder.txtContents.getLineCount(); x++){
                            split[x] = finalViewHolder.txtContents.getText().toString().substring(finalViewHolder.txtContents.getLayout().getLineStart(x), finalViewHolder.txtContents.getLayout().getLineEnd(x));
                        }

//                        String[] split = itemsArrayList.get(position).getContents().split("\n"); //text를 자름
                        int splitLength = 0;

                        String lessText = "";
                        for (String item : split) {
                            splitLength += item.length() + 1;
                            if (splitLength >= lineEndIndex) { //마지막 줄일때!
                                if (item.length() >= expanedText.length()) {
                                    if (item.length() >= 20){
                                        lessText += item.substring(0, item.length() - (expanedText.length())) + expanedText;
                                    }else{
                                        lessText += item + expanedText;
                                    }
                                } else {
                                    lessText += item + expanedText;
                                }
                                break; //종료
                            }else{
                                lessText += item + "\n";
                            }
                        }
                        SpannableString spannableString = new SpannableString(lessText);
                        spannableString.setSpan(new ClickableSpan() {//클릭이벤트
                            @Override
                            public void onClick(View v) {
                                finalViewHolder.txtContents.setText(itemsArrayList.get(position).getContents());
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) { //컬러 처리
                                ds.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                            }
                        }, spannableString.length() - expanedText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        finalViewHolder.txtContents.setText(spannableString);
                        finalViewHolder.txtContents.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }
            });
        }

        String[] hashTagList = itemsArrayList.get(position).getHashTag().split(",");
        String hasgTag = "";

        if (itemsArrayList.get(position).getHashTag().length() == 0){
            viewHolder.txtTag.setVisibility(View.GONE);
        }else{
            viewHolder.txtTag.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < hashTagList.length; i++){
            hasgTag += "#"+hashTagList[i];
        }

        viewHolder.txtTag.setText(hasgTag);
        viewHolder.txtLike.setText("좋아요 "+itemsArrayList.get(position).getLikeCnt());
        viewHolder.txtComment.setText("댓글 "+itemsArrayList.get(position).getCommentCnt());

        String[] imgList = itemsArrayList.get(position).getImgListPath().split(",");

        if (itemsArrayList.get(position).getImgListPath().length() == 0){
            viewHolder.frameLayout.setVisibility(View.GONE);
        }else{
            viewHolder.frameLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(display.getWidth(),display.getWidth());
            viewHolder.frameLayout.setLayoutParams(params);
        }

        adapter = new CommunityViewPagerAdapter(context);

        if (imgList.length <= 1){
            viewHolder.tabLayout.setVisibility(View.GONE);
        }else{
            viewHolder.tabLayout.setVisibility(View.VISIBLE);
            viewHolder.tabLayout.removeAllTabs();
            for (int i = 0; i < imgList.length; i++) {
                viewHolder.tabLayout.addTab(viewHolder.tabLayout.newTab());
            }
        }

        for (int i = 0; i < imgList.length; i++){
            adapter.addItem(imgList[i]);
        }

        viewHolder.viewPager.setAdapter(adapter);

        viewHolder.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(viewHolder.tabLayout));

        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemsArrayList.get(position).getUserNo().equals(""+ Utils.userItem.getUserNo())){
//                    Thirdpopup(context,itemsArrayList.get(position).getNo());

                    final Dialog dialog = new BottomSheetDialog(context, R.style.SheetDialog);
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View contentView = inflater.inflate(R.layout.dialog_comment_third_btn,null);

                    TextView txtRemove = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_remove);
                    TextView txtEdit = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_edit);
                    TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_cancel);

                    dialog.setContentView(contentView);

                    txtRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
//                            TwoBtnPopup(itemsArrayList.get(position).getNo(),context,"삭제하기","게시물을 삭제하시겠습니까?","취소","삭제");

                            final Dialog dateTimeDialog = new Dialog(context);

                            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.two_btn_popup,null);

                            TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
                            TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
                            final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
                            Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

                            txtTitle.setText("삭제하기");
                            txtContents.setText("게시물을 삭제하시겠습니까?");
                            btnLeft.setText("취소");
                            btnRight.setText("삭제");

                            dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                            Window window = dateTimeDialog.getWindow();

                            dateTimeDialog.setContentView(layout);
                            dateTimeDialog.show();

                            btnLeft.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dateTimeDialog.dismiss();
                                }
                            });

                            btnRight.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dateTimeDialog.dismiss();
                                    //게시물 삭제
                                    new CommunityFragment.DeleteCommunityNetWork().execute(itemsArrayList.get(position).getNo());
                                    itemsArrayList.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    });

                    txtEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent i = new Intent(context, CommunityWriteActivity.class);
                            i.putExtra("communityNo",itemsArrayList.get(position).getNo());
                            i.putExtra("contents",itemsArrayList.get(position).getContents());
                            i.putExtra("hashTag", finalViewHolder1.txtTag.getText().toString());
                            i.putExtra("imgsPath",itemsArrayList.get(position).getImgListPath());
                            if (fragment != null){
                                Log.i(TAG,"aaaa");
                                fragment.startActivityForResult(i,1111);
                            }else{
                                context.startActivity(i);
                            }
                        }
                    });

                    txtCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }else{
                    Twopopup(context);
                }
            }
        });

        for (int i = 0; i < likeItemArrayList.size(); i++){
            if (likeItemArrayList.get(i).getCommunityNo().equals(itemsArrayList.get(position).getNo())){
                viewHolder.imgLike.setImageResource(R.drawable.ic_like_on);
                viewHolder.imgLike.setTag(1);
                break;
            }else{
                viewHolder.imgLike.setImageResource(R.drawable.ic_like_off);
                viewHolder.imgLike.setTag(0);
            }
        }

        for (int i = 0; i < scrapItemArrayList.size(); i++){
            if (scrapItemArrayList.get(i).getCommunityNo().equals(itemsArrayList.get(position).getNo())){
                viewHolder.imgCopy.setImageResource(R.drawable.ic_copy_on);
                viewHolder.imgCopy.setTag(1);
                break;
            }else{
                viewHolder.imgCopy.setImageResource(R.drawable.ic_copy_off);
                viewHolder.imgCopy.setTag(0);
            }
        }

        viewHolder.txtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LikeActivity.class);
                i.putExtra("communityNo",itemsArrayList.get(position).getNo());
                context.startActivity(i);
            }
        });

        viewHolder.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CommunityDetailActivity.class);
                i.putExtra("communityNo",itemsArrayList.get(position).getNo());
                context.startActivity(i);
            }
        });

        viewHolder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder1.imgLike.getTag() != null) {
                    if (finalViewHolder1.imgLike.getTag().equals(1)) {
                        new CommunityFragment.DeleteCommunityLikeNetWork().execute(itemsArrayList.get(position).getNo(), "0");
                        finalViewHolder1.imgLike.setImageResource(R.drawable.ic_like_off);
                        finalViewHolder1.imgLike.setTag(0);
                        itemsArrayList.get(position).setLikeCnt(itemsArrayList.get(position).getLikeCnt() - 1);
                        finalViewHolder1.txtLike.setText("좋아요 " + itemsArrayList.get(position).getLikeCnt());
                    } else {
                        new CommunityFragment.InsertCommunityLikeNetWork().execute(itemsArrayList.get(position).getNo(), "1");
                        finalViewHolder1.imgLike.setImageResource(R.drawable.ic_like_on);
                        finalViewHolder1.imgLike.setTag(1);
                        itemsArrayList.get(position).setLikeCnt(itemsArrayList.get(position).getLikeCnt() + 1);
                        finalViewHolder1.txtLike.setText("좋아요 " + itemsArrayList.get(position).getLikeCnt());
                    }
                }else {
                    new CommunityFragment.InsertCommunityLikeNetWork().execute(itemsArrayList.get(position).getNo(), "1");
                    finalViewHolder1.imgLike.setImageResource(R.drawable.ic_like_on);
                    finalViewHolder1.imgLike.setTag(1);
                    itemsArrayList.get(position).setLikeCnt(itemsArrayList.get(position).getLikeCnt() + 1);
                    finalViewHolder1.txtLike.setText("좋아요 " + itemsArrayList.get(position).getLikeCnt());
                }
            }
        });

        viewHolder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder1.imgCopy.getTag() != null) {
                    if (finalViewHolder1.imgCopy.getTag().equals(1)) {
                        new CommunityFragment.DeleteCommunityLikeNetWork().execute(itemsArrayList.get(position).getNo(), "2");
                        finalViewHolder1.imgCopy.setImageResource(R.drawable.ic_copy_off);
                        finalViewHolder1.imgCopy.setTag(0);
                    } else {
                        new CommunityFragment.InsertCommunityLikeNetWork().execute(itemsArrayList.get(position).getNo(), "2");
                        finalViewHolder1.imgCopy.setImageResource(R.drawable.ic_copy_on);
                        finalViewHolder1.imgCopy.setTag(1);
                    }
                }else {
                    new CommunityFragment.InsertCommunityLikeNetWork().execute(itemsArrayList.get(position).getNo(), "2");
                    finalViewHolder1.imgCopy.setImageResource(R.drawable.ic_copy_on);
                    finalViewHolder1.imgCopy.setTag(1);
                }
            }
        });

       viewHolder.imgComment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(context, CommunityDetailActivity.class);
               i.putExtra("communityNo",itemsArrayList.get(position).getNo());
               context.startActivity(i);
           }
       });

        return convertView;
    }

    private class ViewHolder{
        public ImageView imgProfile,imgMore;
        public ViewPager viewPager;
        public TextView txtName,txtDate,txtContents,txtLike,txtComment,txtTag;
        public ImageView imgLike,imgComment,imgCopy;
        public TabLayout tabLayout;
        public LinearLayout tabStip;
        public FrameLayout frameLayout;
    }

    public void addItem(CommunityItems communityItems){
        itemsArrayList.add(communityItems);
    }

}
