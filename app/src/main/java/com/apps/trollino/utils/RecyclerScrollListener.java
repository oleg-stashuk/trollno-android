package com.apps.trollino.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerScrollListener extends RecyclerView.OnScrollListener {
    public RecyclerScrollListener() {}

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
            onScrolledToEnd();
        }
        if (!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
            onScrolledToTop();
        }
    }

    public abstract void onScrolledToEnd();
    public abstract void onScrolledToTop();
}
