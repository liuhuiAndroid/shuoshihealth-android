package com.ssh.shuoshi.ui.imagediagnose.main;

import dagger.Module;

/**
 * created by hwt on 2020/12/8
 */
@Module
public class ImageDiagnoseModule {

    private ImageDiagnoseActivity mActivity;

    public ImageDiagnoseModule(ImageDiagnoseActivity mActivity) {
        this.mActivity = mActivity;
    }
}
