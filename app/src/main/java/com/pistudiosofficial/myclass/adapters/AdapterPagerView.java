package com.pistudiosofficial.myclass.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pistudiosofficial.myclass.fragments.ClassTabFragment;
import com.pistudiosofficial.myclass.fragments.ClassmateTabFragment;
import com.pistudiosofficial.myclass.fragments.NotifTabFragment;

public class AdapterPagerView extends FragmentPagerAdapter {


    public AdapterPagerView(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new NotifTabFragment();
                break;
            case 1:
                fragment = new ClassmateTabFragment();
                break;
            case 2:
                fragment = new ClassTabFragment();
                break;
            default:
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Notification";
            case 1:
                return "Home";
            case 2:
                return "My Class";
            default:
                return "Home";
        }
    }
}
