package com.ssh.shuoshi.ui.doctorauthentication;

import dagger.Module;

/**
 * created by hwt on 2020/12/13
 */
@Module
public class DoctorAuthenticationMoudle {
    private DoctorAuthenticationActivity mActivity;

    public DoctorAuthenticationMoudle(DoctorAuthenticationActivity mActivity) {
        this.mActivity = mActivity;
    }
}
