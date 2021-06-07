package com.apps.trollino.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.apps.trollino.ui.fragment.DiscussTabFragment;
import com.apps.trollino.ui.fragment.EighthTabFragment;
import com.apps.trollino.ui.fragment.FifthTabFragment;
import com.apps.trollino.ui.fragment.FourthTabFragment;
import com.apps.trollino.ui.fragment.FreshTabFragment;
import com.apps.trollino.ui.fragment.NinthTabFragment;
import com.apps.trollino.ui.fragment.SeventhTabFragment;
import com.apps.trollino.ui.fragment.SixthTabFragment;
import com.apps.trollino.ui.fragment.TenthTabFragment;
import com.apps.trollino.ui.fragment.ThirdTabFragment;

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
                return new FreshTabFragment();
            case 1:
                return new DiscussTabFragment();
            case 2:
                return new ThirdTabFragment();
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
            case 8:
                return new NinthTabFragment();
            case 9:
                return new TenthTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalPage;
    }
}
