package com.ssh.shuoshi.ui.graphicinquiry;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;

import dagger.Component;

/**
 * created by hwt on 2020/12/18
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface GraphicInquiryComponent {
    void inject(GraphicInquiryActivity activity);
}
