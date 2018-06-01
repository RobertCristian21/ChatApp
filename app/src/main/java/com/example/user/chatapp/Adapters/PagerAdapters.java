package com.example.user.chatapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.chatapp.Fragments.AllUsersFragment;
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
                AllUsersFragment tab1=new AllUsersFragment();
                return tab1;
            case 1:
                ContactsFragment tab2=new ContactsFragment();
                return tab2;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
