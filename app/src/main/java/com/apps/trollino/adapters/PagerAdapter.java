package com.apps.trollino.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.apps.trollino.ui.fragment.DiscussedPostsFragment;
import com.apps.trollino.ui.fragment.SeventhTabFragment;
import com.apps.trollino.ui.fragment.FirstCategoryPostFragment;
import com.apps.trollino.ui.fragment.SixthTabFragment;
import com.apps.trollino.ui.fragment.FreshPostsFragment;
import com.apps.trollino.ui.fragment.FourthTabFragment;
import com.apps.trollino.ui.fragment.EighthTabFragment;
import com.apps.trollino.ui.fragment.FifthTabFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private final int totalPage;
    
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.totalPage = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FreshPostsFragment();
            case 1:
                return new DiscussedPostsFragment();
            case 2:
                return new FirstCategoryPostFragment();
            case 3:
                return new FourthTabFragment();
            case 4:
                return new FifthTabFragment();
            case 5:
                return new SixthTabFragment();
            case 6:
                return new SeventhTabFragment();
            case 7:
                return new EighthTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalPage;
    }
}
