package com.ssh.shuoshi.ui;


import androidx.annotation.NonNull;

/**
 * Created by weiyun on 2019/5/17.
 */

public interface BasePresenter<T extends BaseView> {

    //绑定view,这个方法将会在activity中调用
    void attachView(@NonNull T view);

    //解绑
    void detachView();
}
