package com.ssh.shuoshi.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ssh.shuoshi.ui.BaseFragment;

import java.util.List;

/**
 * created by hwt on 2020/12/9
 */
public class ImagePageAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments;

    public ImagePageAdapter(FragmentManager fm, List<BaseFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

}
