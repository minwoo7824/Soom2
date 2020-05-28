package com.kmw.soom2.DrugControl.Item;

import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.DrugControl.Interface.StickyView;

import java.util.ArrayList;
import java.util.List;

public class StickyItemDecoration extends RecyclerView.ItemDecoration {



        private View mStickyItemView;
        private int mStickyItemViewMarginTop;
        private int mStickyItemViewHeight;
        private StickyView mStickyView;
        private boolean mCurrentUIFindStickView;
        private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
        private RecyclerView.ViewHolder mViewHolder;
        private List<Integer> mStickyPositionList = new ArrayList<>();
        private LinearLayoutManager mLayoutManager;
        private int mBindDataPosition = -1;

        public StickyItemDecoration() {
            mStickyView = new ExampleStickyView();
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            if (parent.getAdapter().getItemCount() <= 0) return;

            mLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
            mCurrentUIFindStickView = false;
            clearStickyPositionList();

            for (int m = 0, size = parent.getChildCount(); m < size; m++) {
                View view = parent.getChildAt(m);

                if (mStickyView.isStickyView(view)) {

                    mCurrentUIFindStickView = true;
                    getStickyViewHolder(parent);
                    cacheStickyViewPosition(m);

                    if (view.getTop() <= 0) {
                        bindDataForStickyView(mLayoutManager.findFirstVisibleItemPosition(), parent.getMeasuredWidth());

                    }

                    if (view.getTop() > 0 && view.getTop() <= mStickyItemViewHeight) {

                        mStickyItemViewMarginTop = mStickyItemViewHeight - view.getTop();
                    } else {

                        mStickyItemViewMarginTop = 0;
                        View nextStickyView = getNextStickyView(parent);
                        if (nextStickyView != null && nextStickyView.getTop() <= mStickyItemViewHeight) {

                            mStickyItemViewMarginTop = mStickyItemViewHeight - nextStickyView.getTop();
                        }
                    }
                    drawStickyItemView(c);
                    break;
                }
            }

            if (!mCurrentUIFindStickView) {

                mStickyItemViewMarginTop = 0;
                if (mLayoutManager.findFirstVisibleItemPosition() + parent.getChildCount() == parent.getAdapter().getItemCount() && mStickyPositionList.size() > 0) {

                    bindDataForStickyView(mStickyPositionList.get(mStickyPositionList.size() - 1), parent.getMeasuredWidth());

                }
                drawStickyItemView(c);
            }
        }

        private void clearStickyPositionList() {
            if (mLayoutManager.findFirstVisibleItemPosition() == 0) {

                mStickyPositionList.clear();
            }
        }

        private View getNextStickyView(RecyclerView parent) {
            int num = 0;
            View nextStickyView = null;

            for (int m = 0, size = parent.getChildCount(); m < size; m++) {
                View view = parent.getChildAt(m);

                if (mStickyView.isStickyView(view)) {
                    nextStickyView = view;
                    num++;

                }
                if (num == 2) break;

            }

            return num >= 2 ? nextStickyView : null;

        }

        private void bindDataForStickyView(int position, int width) {

            if (mBindDataPosition == position || mViewHolder == null) return;

            mBindDataPosition = position;
            mAdapter.onBindViewHolder(mViewHolder, mBindDataPosition);
            measureLayoutStickyItemView(width);
            mStickyItemViewHeight = mViewHolder.itemView.getBottom() - mViewHolder.itemView.getTop();

        }

        private void cacheStickyViewPosition(int m) {

            int position = getStickyViewPositionOfRecyclerView(m);
            if (!mStickyPositionList.contains(position)) {

                mStickyPositionList.add(position);
            }
        }

        private int getStickyViewPositionOfRecyclerView(int m) {

            return mLayoutManager.findFirstVisibleItemPosition() + m;
        }

        private void getStickyViewHolder(RecyclerView recyclerView) {

            if (mAdapter != null) return;

            mAdapter = recyclerView.getAdapter();
            mViewHolder = mAdapter.onCreateViewHolder(recyclerView, mStickyView.getStickViewType());
            mStickyItemView = mViewHolder.itemView;
        }

        private void measureLayoutStickyItemView(int parentWidth) {

            if (mStickyItemView == null || !mStickyItemView.isLayoutRequested()) return;

            int widthSpec = View.MeasureSpec.makeMeasureSpec(parentWidth, View.MeasureSpec.EXACTLY);
            int heightSpec;

            ViewGroup.LayoutParams layoutParams = mStickyItemView.getLayoutParams();
            if (layoutParams != null && layoutParams.height > 0) {

                heightSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY);
            } else {

                heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }

            mStickyItemView.measure(widthSpec, heightSpec);
            mStickyItemView.layout(0, 0, mStickyItemView.getMeasuredWidth(), mStickyItemView.getMeasuredHeight());
        }

        private void drawStickyItemView(Canvas canvas) {
            if (mStickyItemView == null) return;

            int saveCount = canvas.save();
            canvas.translate(0, -mStickyItemViewMarginTop);
            mStickyItemView.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }
