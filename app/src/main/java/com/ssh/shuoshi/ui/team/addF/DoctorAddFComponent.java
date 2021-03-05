package com.ssh.shuoshi.ui.team.addF;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;

import dagger.Component;

/**
 * created by hwt on 2021/1/3
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface DoctorAddFComponent {

    void inject(DoctorAddFActivity activity);
}
