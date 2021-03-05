package com.ssh.shuoshi.ui.main;

import dagger.Module;

/**
 * created by hwt on 2020/12/8
 */
@Module
public class MainModule {

    private MainActivity mActivity;

    public MainModule(MainActivity mActivity) {
        this.mActivity = mActivity;
    }
}
