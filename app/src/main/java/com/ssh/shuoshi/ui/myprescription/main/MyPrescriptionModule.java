package com.ssh.shuoshi.ui.myprescription.main;

import dagger.Module;

/**
 * created by hwt on 2021/1/2
 */
@Module
public class MyPrescriptionModule {

    private MyPrescriptionActivity mActivity;

    public MyPrescriptionModule(MyPrescriptionActivity mActivity) {
        this.mActivity = mActivity;
    }
}
