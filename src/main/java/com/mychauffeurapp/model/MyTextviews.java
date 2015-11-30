package com.mychauffeurapp.model;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class MyTextviews  extends TextView {

    public MyTextviews(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextviews(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextviews(Context context) {
        super(context);
        init();
    }

    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNova-Regular.otf");
        setTypeface(tf);
        // setTextColor(Color.WHITE); {
    }
}
