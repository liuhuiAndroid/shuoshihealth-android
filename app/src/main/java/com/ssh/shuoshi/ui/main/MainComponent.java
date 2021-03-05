package com.ssh.shuoshi.ui.main;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.fragment1.Fragment1;
import com.ssh.shuoshi.ui.fragment2.Fragment2;

import dagger.Component;

/**
 * created by hwt on 2020/12/8
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, MainModule.class})
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(Fragment1 fragment1);

    void inject(Fragment2 fragment2);


}
