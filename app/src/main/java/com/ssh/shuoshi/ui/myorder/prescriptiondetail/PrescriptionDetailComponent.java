package com.ssh.shuoshi.ui.myorder.prescriptiondetail;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.myorder.MyOrderActivity;

import dagger.Component;

/**
 * created by hwt on 2020/12/9
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface PrescriptionDetailComponent {

    void inject(PrescriptionDetailActivity activity);

}
