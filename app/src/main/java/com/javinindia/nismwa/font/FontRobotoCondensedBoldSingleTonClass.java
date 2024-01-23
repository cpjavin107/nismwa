package com.javinindia.nismwa.font;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Ashish on 20-09-2016.
 */
public class FontRobotoCondensedBoldSingleTonClass {
    private static FontRobotoCondensedBoldSingleTonClass instance;
    private static Typeface typeface;

    public static FontRobotoCondensedBoldSingleTonClass getInstance(Context context) {
        synchronized (FontRobotoCondensedBoldSingleTonClass.class) {
            if (instance == null) {
                instance = new FontRobotoCondensedBoldSingleTonClass();
                if(context != null){
                    typeface = Typeface.createFromAsset(context.getResources().getAssets(), "robotocondensed-bold.ttf");
                }

            }
            return instance;
        }
    }

    public Typeface getTypeFace() {
        return typeface;
    }
}
