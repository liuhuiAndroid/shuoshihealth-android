package com.ssh.shuoshi.ui.prescription.template;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionchinese.CommonlyPrescriptionChineseFragment;
import com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionwestern.CommonlyPrescriptionWesternFragment;
import com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine.CommonlyWesternMedicineFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, PrescriptionTemplateMoudle.class})
public interface PrescriptionTemplateComponent {

    void inject(PrescriptionTemplateActivity activity);

    void inject(CommonlyPrescriptionChineseFragment commonlyPrescriptionChineseFragment);

    void inject(CommonlyPrescriptionWesternFragment commonlyPrescriptionWesternFragment);

    void inject(CommonlyWesternMedicineFragment commonlyWesternMedicineFragment);

}
