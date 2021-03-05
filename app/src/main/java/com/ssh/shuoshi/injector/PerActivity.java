package com.ssh.shuoshi.injector;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 通过自定义Scope注解可以更好的管理创建的类实例的生命周期。
 * Scope的真正用处就在于Component的组织。
 * Created by weiyun on 2019/5/16.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
