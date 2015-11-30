package com.mychauffeurapp.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextviewBlack extends TextView {

    public MyTextviewBlack(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextviewBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextviewBlack(Context context) {
        super(context);
        init();
    }

    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNova-Regular.otf");
        setTypeface(tf);
        //setTextColor(Color.parseColor("#FFFFFF"));
        //setLineSpacing(10f, 1f);
    }
}

