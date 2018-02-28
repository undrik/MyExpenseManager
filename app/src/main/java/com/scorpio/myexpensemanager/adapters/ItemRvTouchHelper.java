package com.scorpio.myexpensemanager.adapters;

import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Recycler View swipe helper
 * Created by User on 22-02-2018.
 */

public class ItemRvTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RvItemSwipeListener listener;

    public interface RvItemSwipeListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    public ItemRvTouchHelper(int dragDirs, int swipeDirs, RvItemSwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //super.onSelectedChanged(viewHolder, actionState);
        if (null != viewHolder) {
            getDefaultUIUtil().onSelected(getCommonCardView(viewHolder));
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
            viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState,
        // isCurrentlyActive);

//        final View companyCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).commonCv;
        getDefaultUIUtil().onDrawOver(c, recyclerView, getCommonCardView(viewHolder), dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        super.clearView(recyclerView, viewHolder);
//        final View companyCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).commonCv;
        getDefaultUIUtil().clearView(getCommonCardView(viewHolder));
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
            viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (dX > 0) {
            //swipe from Left ->Right
            getDeleteBackgroud(viewHolder).setVisibility(View.INVISIBLE);
            getEditBackgroud(viewHolder).setVisibility(View.VISIBLE);
        } else {
            // swipe from Right->Left
            getEditBackgroud(viewHolder).setVisibility(View.INVISIBLE);
            getDeleteBackgroud(viewHolder).setVisibility(View.VISIBLE);
        }
//        final View companyCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).commonCv;

        getDefaultUIUtil().onDraw(c, recyclerView, getCommonCardView(viewHolder), dX, dY, actionState,
                isCurrentlyActive);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private View getCommonCardView(RecyclerView.ViewHolder viewHolder) {
        View commonCv = null;
        if (viewHolder instanceof CompanyRvAdapter.CompanyViewHolder) {
            commonCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).commonCv;
        } else if (viewHolder instanceof LedgerRvAdapter.LedgerViewHolder) {
            commonCv = ((LedgerRvAdapter.LedgerViewHolder) viewHolder).commonCv;
        }
        return commonCv;
    }

    private ConstraintLayout getDeleteBackgroud(RecyclerView.ViewHolder viewHolder) {
        ConstraintLayout deleteBackgroud = null;
        if (viewHolder instanceof CompanyRvAdapter.CompanyViewHolder) {
            deleteBackgroud = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).deleteBackground;
        } else if (viewHolder instanceof LedgerRvAdapter.LedgerViewHolder) {
            deleteBackgroud = ((LedgerRvAdapter.LedgerViewHolder) viewHolder).deleteBackground;
        }
        return deleteBackgroud;
    }

    private ConstraintLayout getEditBackgroud(RecyclerView.ViewHolder viewHolder) {
        ConstraintLayout editBackground = null;
        if (viewHolder instanceof CompanyRvAdapter.CompanyViewHolder) {
            editBackground = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).editBackgroud;
        } else if (viewHolder instanceof LedgerRvAdapter.LedgerViewHolder) {
            editBackground = ((LedgerRvAdapter.LedgerViewHolder) viewHolder).editBackgroud;
        }
        return editBackground;
    }

}
