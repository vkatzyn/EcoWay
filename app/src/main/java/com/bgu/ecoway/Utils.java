package com.bgu.ecoway;

import android.util.TypedValue;

public class Utils {
    public static int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, MainActivity.instance.get().getResources().getDisplayMetrics());
    }
}
