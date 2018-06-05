package com.example.user.chatapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.chatapp.Fragments.AllUsersFragment;
import com.example.user.chatapp.Fragments.BlocksFragment;
import com.example.user.chatapp.Fragments.ContactsFragment;

public class PagerAdapters extends FragmentStatePagerAdapter {

    int noOfTabs;

    public PagerAdapters(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ContactsFragment tab1=new ContactsFragment();
                return tab1;
            case 1:
                AllUsersFragment tab2=new AllUsersFragment();
                return tab2;
            case 2:
                BlocksFragment tab3=new BlocksFragment();
                return tab3;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
