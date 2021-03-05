package com.ssh.shuoshi.ui.prescription.chinesemedicine.addherbs;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.advice.ChineseMedicineMedicalAdviceActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ChineseMedicineMedicalAddHerbsComponent {

    void inject(ChineseMedicineMedicalAddHerbsActivity activity);

}
