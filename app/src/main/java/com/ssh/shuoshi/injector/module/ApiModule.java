package com.ssh.shuoshi.injector.module;

import android.content.Context;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.components.retrofit.RequestHelper;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.injector.PerApp;
import com.ssh.shuoshi.util.SPUtil;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by weiyun on 2019/5/17.
 */

@Module
public class ApiModule {

    @Provides
    @PerApp
    public CommonApi providesCookieApi(Context context, @Named("api") OkHttpClient okHttpClient,
                                       RequestHelper requestHelper, UserStorage userStorage, SPUtil spUtil) {
        return new CommonApi(context, okHttpClient, requestHelper, userStorage, spUtil);
    }
}
