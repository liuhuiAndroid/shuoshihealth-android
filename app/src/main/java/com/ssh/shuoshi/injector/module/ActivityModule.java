package com.ssh.shuoshi.injector.module;

import android.app.Activity;


import com.ssh.shuoshi.injector.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * 创建类实例级别Module维度要高于Inject维度。
 * Created by weiyun on 2019/5/17.
 */

@Module // 1.注明本类属于Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    // Provides最终解决第三方类库依赖注入问题
    @Provides       //2. 注明该方法是用来提供依赖对象的特殊方法
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }
}
