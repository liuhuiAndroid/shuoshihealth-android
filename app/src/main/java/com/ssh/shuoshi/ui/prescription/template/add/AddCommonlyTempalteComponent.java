package com.ssh.shuoshi.ui.prescription.template.add;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.edit.EditChineseMedicinePrescriptionActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface AddCommonlyTempalteComponent {
    void inject(AddCommonlyTempalteActivity activity);
}
