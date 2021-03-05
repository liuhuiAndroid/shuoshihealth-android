package com.ssh.shuoshi.ui.consultationsummary;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ConsultationSummaryComponent {
    void inject(ConsultationSummaryActivity activity);
}
