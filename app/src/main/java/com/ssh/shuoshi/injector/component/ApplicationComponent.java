package com.ssh.shuoshi.injector.component;

import android.content.Context;

import com.ssh.shuoshi.MyApplication;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.injector.PerApp;
import com.ssh.shuoshi.injector.module.ApiModule;
import com.ssh.shuoshi.injector.module.ApplicationModule;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.util.SPUtil;
import com.squareup.otto.Bus;


import dagger.Component;

/**
 * 全局的ApplicationComponent,负责管理整个app的全局类实例，并且它们的生命周期与app的生命周期一样。
 * 若ApplicationComponent和Module的Scope是不一样的，则在编译时报错。
 * <p>
 * 一个Component可以包含多个Module，这样Component获取依赖时候会自动从多个Module中查找获取，Module间不能有重复方法
 * Created by weiyun on 2019/5/17.
 */

@PerApp             //3.指明Component在哪些Module中查找依赖
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent { //4.接口,自动实现

    //被依赖时候,必须显示地把这些A找不到的依赖提供给A,需要添加如下方法
    //Component的方法可以没有输入参数,但必须有返回值
    Context getContext();

    Bus getBus();

    CommonApi getCommonApi();

    //5.注入方法,在Container中调用
    void inject(MyApplication myApplication);

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    UserStorage getUserStorage();

    SPUtil provideSPUtil();
}
