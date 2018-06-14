package com.example.user.chatapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.chatapp.Fragments.AllUsersFragment;
import com.example.user.chatapp.Fragments.BlocksFragment;
import com.example.user.chatapp.Fragments.ContactsFragment;

public class PagerAdapters extends FragmentStatePagerAdapter {

    private int noOfTabs;

    public PagerAdapters(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ContactsFragment();
            case 1:
                return new AllUsersFragment();
            case 2:
                return new BlocksFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
