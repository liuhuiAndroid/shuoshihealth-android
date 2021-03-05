package com.ssh.shuoshi.ui.verified.h5;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;

import dagger.Component;

/**
 * created by hwt on 2020/12/30
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface H5SecondComponent {

    void inject(H5SecondActivity activity);
}
