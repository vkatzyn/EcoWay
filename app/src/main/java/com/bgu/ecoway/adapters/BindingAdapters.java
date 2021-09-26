package com.bgu.ecoway.adapters;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bgu.ecoway.Utils;

public class BindingAdapters {


    @BindingAdapter({"adapter"})
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setAdapter(adapter);
    }


    @BindingAdapter(value = {"pagerHorizontalListItemPadding", "pagerVerticalListItemPadding"} , requireAll = false)
    public static void pagerHorizontalListItemPadding(ViewPager2 viewPager, float hdp, float vdp) {
        int hpadding = Utils.dpToPx(hdp);
        int vpadding = Utils.dpToPx(vdp);
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        recyclerView.setPadding(hpadding, vpadding, hpadding, vpadding);
        recyclerView.setClipToPadding(false);
        recyclerView.setClipChildren(false);
        viewPager.setPageTransformer(new MarginPageTransformer(hpadding/2));
    }


}
