package com.ssh.shuoshi.ui.videodiagnose.main;

import dagger.Module;

/**
 * created by hwt on 2020/12/21
 */
@Module
public class VideoDiagnoseModule {

    private VideoDiagnoseActivity mActivity;

    public VideoDiagnoseModule(VideoDiagnoseActivity mActivity) {
        this.mActivity = mActivity;
    }
}
