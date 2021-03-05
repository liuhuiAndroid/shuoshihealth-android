package com.ssh.shuoshi.ui.doctorauthentication;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.doctorauthentication.auth.DoctorAuthenticationAuthFragment;
import com.ssh.shuoshi.ui.doctorauthentication.basic.DoctorAuthenticationBasicFragment;

import dagger.Component;

/**
 * created by hwt on 2020/12/13
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, DoctorAuthenticationMoudle.class})
public interface DoctorAuthenticationComponent {
    void inject(DoctorAuthenticationActivity activity);

    void inject(DoctorAuthenticationBasicFragment doctorAuthenticationBasicFragment);

    void inject(DoctorAuthenticationAuthFragment doctorAuthenticationAuthFragment);
}
