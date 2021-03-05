package com.ssh.shuoshi.inter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public interface MyObserver extends Observer {
    @Override
    default void onSubscribe(Disposable d) {

    }

    @Override
    default void onNext(Object o) {

    }

    @Override
    default void onError(Throwable e) {

    }

    @Override
    default void onComplete() {

    }
}
