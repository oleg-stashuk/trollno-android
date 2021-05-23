package com.apps.trollino.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.apps.trollino.ui.fragment.DiscussedPostsFragment;
import com.apps.trollino.ui.fragment.FifthCategoryPostFragment;
import com.apps.trollino.ui.fragment.FirstCategoryPostFragment;
import com.apps.trollino.ui.fragment.FourthCategoryPostFragment;
import com.apps.trollino.ui.fragment.FreshPostsFragment;
import com.apps.trollino.ui.fragment.SecondCategoryPostFragment;
import com.apps.trollino.ui.fragment.SixthCategoryPostFragment;
import com.apps.trollino.ui.fragment.ThirdCategoryPostFragment;

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
                return new SecondCategoryPostFragment();
            case 4:
                return new ThirdCategoryPostFragment();
            case 5:
                return new FourthCategoryPostFragment();
            case 6:
                return new FifthCategoryPostFragment();
            case 7:
                return new SixthCategoryPostFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalPage;
    }
}
