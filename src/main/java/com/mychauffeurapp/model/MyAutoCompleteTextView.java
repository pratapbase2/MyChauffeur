package com.mychauffeurapp.model;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class MyAutoCompleteTextView extends AutoCompleteTextView {


        public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public MyAutoCompleteTextView(Context context) {
            super(context);
            init();
        }

        public void init() {

            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNova-Regular.otf");
            setTypeface(tf);
        }
    }

