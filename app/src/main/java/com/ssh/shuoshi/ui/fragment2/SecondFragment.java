package com.ssh.shuoshi.ui.fragment2;

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
public class SecondFragment extends BaseMainFragment {

    public static SecondFragment newInstance() {

        Bundle args = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        if (findChildFragment(Fragment2.class) == null) {
            loadRootFragment(R.id.fl_second_container, Fragment2.newInstance());
        }
        return view;
    }

}
