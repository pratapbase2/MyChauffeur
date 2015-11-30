package com.mychauffeurapp.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


public class ButtonRegularText extends Button{
    public ButtonRegularText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonRegularText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonRegularText(Context context) {
        super(context);
        init();
    }

    public void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNova-Regular.otf");
        setTypeface(tf);
        setTextColor(Color.WHITE);
        //setLineSpacing(10f, 1f);
    }
}
