package com.mychauffeurapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mychauffeurapp.fragments.SwipeTabFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Bundle bundle = new Bundle();
        String tab = "";
        String layout = "";
        String reg_num,makeofcar,ageofvehicle;
        switch (index) {
            case 0:
                tab = "Vehicle Details";
                layout ="1";
                break;

            case 1:
                tab = "Vehicle Type";
                layout ="";
                //colorResId = Color.WHITE;
                break;
            /*case 2:
                tab = "List of Received Calls";
                colorResId = Color.YELLOW;
                break;*/
        }

        bundle.putString("tab", tab);
        bundle.putString("color", layout);

        SwipeTabFragment swipeTabFragment = new SwipeTabFragment();
        swipeTabFragment.setArguments(bundle);
        return swipeTabFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}