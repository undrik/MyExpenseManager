package com.scorpio.myexpensemanager.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by User on 22-02-2018.
 */

public class CompanyRvTouchHelper extends ItemTouchHelper.SimpleCallback {

    private CompanyRvItemSwipeListener listener;

    public interface CompanyRvItemSwipeListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

    public CompanyRvTouchHelper(int dragDirs, int swipeDirs, CompanyRvItemSwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //super.onSelectedChanged(viewHolder, actionState);
        if (null != viewHolder) {
            final View companyCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).companyCv;
            getDefaultUIUtil().onSelected(companyCv);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
            viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState,
        // isCurrentlyActive);

        final View companyCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).companyCv;
        getDefaultUIUtil().onDrawOver(c, recyclerView, companyCv, dX, dY, actionState,
                isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        super.clearView(recyclerView, viewHolder);
        final View companyCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).companyCv;
        getDefaultUIUtil().clearView(companyCv);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
            viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        final View companyCv = ((CompanyRvAdapter.CompanyViewHolder) viewHolder).companyCv;
        getDefaultUIUtil().onDraw(c, recyclerView, companyCv, dX, dY, actionState,
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
}
