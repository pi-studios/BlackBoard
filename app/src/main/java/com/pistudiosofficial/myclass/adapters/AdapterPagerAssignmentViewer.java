package com.pistudiosofficial.myclass.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pistudiosofficial.myclass.fragments.AssignmentViewerInfoFragment;
import com.pistudiosofficial.myclass.fragments.AssignmentViewerSubmissionFragment;
import com.pistudiosofficial.myclass.fragments.HelloListFragment;
import com.pistudiosofficial.myclass.fragments.HelloRequestFragment;

public class AdapterPagerAssignmentViewer extends FragmentPagerAdapter {


    public AdapterPagerAssignmentViewer(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new AssignmentViewerInfoFragment();
                break;
            case 1:
                fragment = new AssignmentViewerSubmissionFragment();
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
                return "Info";
            case 1:
                return "Submissions";
            default:
                return "Info";
        }
    }
}
