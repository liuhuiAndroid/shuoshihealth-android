package com.ssh.shuoshi.ui.videodiagnose.main;

import com.ssh.shuoshi.injector.PerActivity;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.ui.imagediagnose.imageend.ImageDiagnoseEndFragment;
import com.ssh.shuoshi.ui.imagediagnose.imageing.ImageDiagnoseIngFragment;
import com.ssh.shuoshi.ui.videodiagnose.videodai.VideoDiagnoseDaiFragment;
import com.ssh.shuoshi.ui.videodiagnose.videoend.VideoDiagnoseEndFragment;
import com.ssh.shuoshi.ui.videodiagnose.videoing.VideoDiagnoseIngFragment;

import dagger.Component;

/**
 * created by hwt on 2020/12/9
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class,
        VideoDiagnoseModule.class})
public interface VideoDiagnoseComponent {
    void inject(VideoDiagnoseActivity activity);

    void inject(VideoDiagnoseDaiFragment videoDiagnoseDaiFragment);

    void inject(VideoDiagnoseEndFragment videoDiagnoseEndFragment);

    void inject(VideoDiagnoseIngFragment videoDiagnoseIngFragment);
}
