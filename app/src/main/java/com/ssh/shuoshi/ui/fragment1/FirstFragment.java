package com.ssh.shuoshi.ui.fragment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseMainFragment;

/**
 * created by hwt on 2020/12/8
 */
public class FirstFragment extends BaseMainFragment {

    public static FirstFragment newInstance() {
        Bundle args = new Bundle();

        FirstFragment firstFragment = new FirstFragment();
        firstFragment.setArguments(args);
        return firstFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (findChildFragment(Fragment1.class) == null) {
            loadRootFragment(R.id.fl_first_container, Fragment1.newInstance());
        }
    }
}
