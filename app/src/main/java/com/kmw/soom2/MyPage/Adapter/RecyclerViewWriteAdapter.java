package com.kmw.soom2.MyPage.Adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Home.HomeItem.ZoomAnimation;
import com.kmw.soom2.MyPage.Item.RecyclerViewWriteItemList;
import com.kmw.soom2.MyPage.Item.ViewPagerItemList;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;


public class RecyclerViewWriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnTouchListener {
    ArrayList<RecyclerViewWriteItemList> arrayList;
    Context context;
    ArrayList<ViewPagerItemList> viewPagerItemLists;
    HashMap<Integer, Integer> mViewPagerState = new HashMap<>();
    BottomSheetDialog iPhoneModelOneDialog, iPhoneModelTwoDialog;
    View iPhoneModelOneDialogView;
    View iPhoneModelTwoDialogView;
    ImageView likeImageViwe,commentImageView,bookMarkImageView;
    boolean likeBoolean = false, bookMarkBoolean = false;
    public RecyclerViewWriteAdapter(Context context, ArrayList<RecyclerViewWriteItemList> arrayList, ArrayList<ViewPagerItemList> viewPagerItemLists) {
        this.arrayList = arrayList;
        this.context = context;
        this.viewPagerItemLists = viewPagerItemLists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.write_item, parent, false);
                return new ViewHolderUserIcon(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewWriteItemList item = arrayList.get(position);
        if (item != null) {
            return item.getViewType();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewWriteItemList item = arrayList.get(position);


        ViewPagerImageAdapter adapter = new ViewPagerImageAdapter(viewPagerItemLists, context);
        switch (item.getViewType()) {
            case 0:
                ((ViewHolderUserIcon) holder).userIconImageView.setImageResource(item.getUserIcon());
                if (viewPagerItemLists.size() != 0) {
                    ((ViewHolderUserIcon) holder).viewPager.setAdapter(adapter);
                    ((ViewHolderUserIcon) holder).viewPager.setId(position + 1);
                    ((ViewHolderUserIcon) holder).viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(((ViewHolderUserIcon) holder).tabLayout));
                    if (viewPagerItemLists.size() >= 2) {
                        for (int i = 0; i < viewPagerItemLists.size(); i++) {
                            ((ViewHolderUserIcon) holder).tabLayout.addTab(((ViewHolderUserIcon) holder).tabLayout.newTab());
                        }
                        ((ViewHolderUserIcon) holder).tabStip = ((LinearLayout) ((ViewHolderUserIcon) holder).tabLayout.getChildAt(0));
                        for (int i = 0; i < viewPagerItemLists.size(); i++) {
                            ((ViewHolderUserIcon) holder).tabStip.getChildAt(i).setOnTouchListener(this);
                        }
                    }
                    if (mViewPagerState.containsKey(position)) {
                        ((ViewHolderUserIcon) holder).viewPager.setCurrentItem(mViewPagerState.get(position));
                    }
                } else if (viewPagerItemLists.size() == 0) {
                    ((ViewHolderUserIcon) holder).constraintLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    class ViewHolderUserIcon extends RecyclerView.ViewHolder {
        ImageView userIconImageView,menuTabImageView;
        ViewPager viewPager;
        TabLayout tabLayout;
        LinearLayout tabStip;
        ConstraintLayout constraintLayout;
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        public ViewHolderUserIcon(@NonNull View itemView) {
            super(itemView);
            iPhoneModelOneDialog = new BottomSheetDialog(context,R.style.SheetDialog);
            iPhoneModelTwoDialog = new BottomSheetDialog(context,R.style.SheetDialog);
            iPhoneModelOneDialogView = inflate.inflate(R.layout.bottom_sheet_iphone_model_a, null);
            iPhoneModelTwoDialogView = inflate.inflate(R.layout.bottom_sheet_iphone_model_b, null);

            iPhoneModelOneDialog.setContentView(iPhoneModelOneDialogView);
            iPhoneModelTwoDialog.setContentView(iPhoneModelTwoDialogView);

            userIconImageView = (ImageView) itemView.findViewById(R.id.user_icon_activity_posts);
            likeImageViwe = (ImageView) itemView.findViewById(R.id.like_image_view_activity_posts);
            commentImageView = (ImageView) itemView.findViewById(R.id.comment_image_view_activity_posts);
            bookMarkImageView = (ImageView) itemView.findViewById(R.id.book_mark_image_view_activity_posts);

            viewPager = (ViewPager) itemView.findViewById(R.id.view_pager_write_item);
            tabLayout = (TabLayout) itemView.findViewById(R.id.tab_layout_write_item);
            menuTabImageView = (ImageView) itemView.findViewById(R.id.expandable_image_view_activity_posts);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.view_pager_container_activity_posts);
            viewPager.setPageTransformer(true, new ZoomAnimation());

            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int height = dm.heightPixels;

            iPhoneModelOneDialogView.getLayoutParams().height = (int) (height * 0.20f);
            iPhoneModelTwoDialogView.getLayoutParams().height = (int) (height * 0.15f);
            menuTabImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.

                        RecyclerViewWriteItemList item = arrayList.get(pos) ;
                        iPhoneModelOneDialog.show();

                        // TODO : use item.
                    }
                }
            });

            likeImageViwe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!likeBoolean) {
                        likeImageViwe.setImageResource(R.drawable.ic_favorite_green_24dp);
                        likeBoolean = true;
                    }else if(likeBoolean){
                        likeImageViwe.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        likeBoolean =false;
                    }
                }
            });
            bookMarkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!bookMarkBoolean) {
                        bookMarkImageView.setImageResource(R.drawable.book_mark_icon_green);
                        bookMarkBoolean = true;
                    }else if(bookMarkBoolean){
                        bookMarkImageView.setImageResource(R.drawable.book_mark_icon);
                        bookMarkBoolean =false;
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

}
