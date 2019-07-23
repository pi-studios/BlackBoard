package com.pistudiosofficial.myclass.adapters;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pistudiosofficial.myclass.fragments.HelloListFragment;
import com.pistudiosofficial.myclass.fragments.HelloRequestFragment;

public class AdapterPagerViewHello extends FragmentPagerAdapter {


    public AdapterPagerViewHello(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new HelloListFragment();
                break;
            case 1:
                fragment = new HelloRequestFragment();
                break;
            default:
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Hello List";
            case 1:
                return "Hello Request";
            default:
                return "Hello List";
        }
    }
}
