package com.bytelicious.phramazing.home;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author ylyubenov
 */

public class PhramazeAdapterTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ProfileRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    public PhramazeAdapterTouchHelper(ProfileRecyclerAdapter adapter, RecyclerView recyclerView) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemRemove(viewHolder);
    }
}
