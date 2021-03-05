package com.ssh.shuoshi.ui.imagediagnose.main;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.imagediagnose.imagedai.ImageDiagnoseDaiFragment;
import com.ssh.shuoshi.ui.imagediagnose.imageend.ImageDiagnoseEndFragment;
import com.ssh.shuoshi.ui.imagediagnose.imageing.ImageDiagnoseIngFragment;

import dagger.Component;

/**
 * created by hwt on 2020/12/9
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, ImageDiagnoseModule.class})
public interface ImageDiagnoseComponent {
    void inject(ImageDiagnoseActivity activity);

    void inject(ImageDiagnoseDaiFragment imageDiagnoseDaiFragment);

    void inject(ImageDiagnoseIngFragment imageDiagnoseIngFragment);

    void inject(ImageDiagnoseEndFragment imageDiagnoseEndFragment);
}
