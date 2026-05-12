package com.ebondhon.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.ebondhon.R;

public class FontUtils {

    private static Typeface hindSiliguri;
    private static Typeface hindSiliguriBold;

    public static Typeface getHindSiliguri(Context context) {
        if (hindSiliguri == null) {
            hindSiliguri = ResourcesCompat.getFont(context, R.font.hind_siliguri_regular);
        }
        return hindSiliguri;
    }

    public static Typeface getHindSiliguriBold(Context context) {
        if (hindSiliguriBold == null) {
            hindSiliguriBold = ResourcesCompat.getFont(context, R.font.hind_siliguri_bold);
        }
        return hindSiliguriBold;
    }

    public static void applyFont(Context context, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyFont(context, group.getChildAt(i));
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTypeface(getHindSiliguri(context));
        }
    }

    public static void applyBoldFont(Context context, TextView textView) {
        textView.setTypeface(getHindSiliguriBold(context));
    }
}
