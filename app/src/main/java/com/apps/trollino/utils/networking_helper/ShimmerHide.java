package com.apps.trollino.utils.networking_helper;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerHide {

    public static void shimmerHide(LinearLayout layout, ShimmerFrameLayout shimmerLayout) {
        layout.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            shimmerLayout.setVisibility(View.GONE);
        }, 1500);
    }

    public static void shimmerHide(RecyclerView recycler, ShimmerFrameLayout shimmerLayout) {
        recycler.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            shimmerLayout.setVisibility(View.GONE);
        }, 1500);
    }
}
