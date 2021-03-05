package com.ssh.shuoshi.ui.verified.face;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.prescription.westernmedicine.add.NewDrugsActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface VerifiedFaceComponent {

    void inject(VerifiedFaceActivity activity);

}
