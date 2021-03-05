package com.ssh.shuoshi.ui.verified.phone;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.verified.face.VerifiedFaceActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface VerifiedPhoneComponent {

    void inject(VerifiedPhoneActivity activity);

}
