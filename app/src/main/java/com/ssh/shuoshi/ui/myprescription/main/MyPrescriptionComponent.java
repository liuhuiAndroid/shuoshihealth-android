package com.ssh.shuoshi.ui.myprescription.main;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.myprescription.fail.MyPrescriptionFailFragment;
import com.ssh.shuoshi.ui.myprescription.ing.MyPrescriptionIngFragment;
import com.ssh.shuoshi.ui.myprescription.success.MyPrescriptionSuccessFragment;
import com.ssh.shuoshi.ui.myprescription.wait.MyPrescriptionWaitFragment;

import dagger.Component;

/**
 * created by hwt on 2021/1/2
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, MyPrescriptionModule.class})
public interface MyPrescriptionComponent {
    void inject(MyPrescriptionActivity activity);

    void inject(MyPrescriptionFailFragment myPrescriptionFailFragment);

    void inject(MyPrescriptionIngFragment myPrescriptionIngFragment);

    void inject(MyPrescriptionSuccessFragment myPrescriptionSuccessFragment);

    void inject(MyPrescriptionWaitFragment myPrescriptionWaitFragment);
}
