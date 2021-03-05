package com.ssh.shuoshi.injector.component;

import android.app.Activity;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by weiyun on 2019/5/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();
}
