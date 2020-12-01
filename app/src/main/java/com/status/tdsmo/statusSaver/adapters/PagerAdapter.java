package com.status.tdsmo.statusSaver.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.status.tdsmo.statusSaver.fragments.PhotoStatusesFragment;
import com.status.tdsmo.statusSaver.fragments.VideoStatusesFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private final PhotoStatusesFragment photoStatusesFragment;
    private final VideoStatusesFragment videoStatusesFragment;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        photoStatusesFragment = new PhotoStatusesFragment();
        videoStatusesFragment = new VideoStatusesFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? photoStatusesFragment : videoStatusesFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "PHOTO" : "VIDEO";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
