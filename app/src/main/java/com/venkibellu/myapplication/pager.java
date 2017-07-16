package com.venkibellu.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


    //Extending FragmentStatePagerAdapter
    public class pager extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount= tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    tab_alumini tab_alumini = new tab_alumini();
                    return tab_alumini;
                case 1:
                    tab_visionuvce tab_visionuvce = new tab_visionuvce();
                    return tab_visionuvce;
                case 2:
                    tab_uvcefoundation tab_uvcefoundation = new tab_uvcefoundation();
                    return tab_uvcefoundation;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }

