package com.ssh.shuoshi.ui.myorder;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.myorder.consultation.ConsultationOrderFragment;
import com.ssh.shuoshi.ui.myorder.prescription.PrescriptionOrderFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface MyOrderComponent {

    void inject(MyOrderActivity activity);

    void inject(ConsultationOrderFragment fragment);

    void inject(PrescriptionOrderFragment fragment);

}
