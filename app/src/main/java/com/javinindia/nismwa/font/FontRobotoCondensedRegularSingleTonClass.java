package com.javinindia.nismwa.font;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Ashish on 24-04-2017.
 */

public class FontRobotoCondensedRegularSingleTonClass {
    private static FontRobotoCondensedRegularSingleTonClass instance;
    private static Typeface typeface;

    public static FontRobotoCondensedRegularSingleTonClass getInstance(Context context) {
        synchronized (FontRobotoCondensedRegularSingleTonClass.class) {
            if (instance == null) {
                instance = new FontRobotoCondensedRegularSingleTonClass();
                if (context != null) {
                    typeface = Typeface.createFromAsset(context.getResources().getAssets(), "robotocondensed-regular.ttf");
                }

            }
            return instance;
        }
    }

    public Typeface getTypeFace() {
        return typeface;
    }
}
