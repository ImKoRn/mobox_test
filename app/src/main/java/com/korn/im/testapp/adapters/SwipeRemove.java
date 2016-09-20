package com.korn.im.testapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by korn on 15.09.16.
 */
public class SwipeRemove extends ItemTouchHelper.Callback {
    public OnItemSwipedListener onItemSwipedListener;

    public SwipeRemove(OnItemSwipedListener listener) {
        onItemSwipedListener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (onItemSwipedListener != null)
            onItemSwipedListener.onSwiped(viewHolder.getAdapterPosition());
    }

    public interface OnItemSwipedListener {
        void onSwiped(int position);
    }
}
