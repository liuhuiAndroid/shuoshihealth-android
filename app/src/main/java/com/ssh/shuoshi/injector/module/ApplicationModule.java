package com.ssh.shuoshi.injector.module;

import android.content.Context;

import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.components.okhttp.TokenInterceptor;
import com.ssh.shuoshi.components.retrofit.RequestHelper;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.injector.PerApp;
import com.ssh.shuoshi.library.util.CommonUtils;
import com.ssh.shuoshi.library.util.PhoneUtil;
import com.ssh.shuoshi.library.util.log.Logger;
import com.ssh.shuoshi.util.SPUtil;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by weiyun on 2019/5/16.
 * singleton是application级别的 我们要放在application里面去初始化
 */

@Module
public class ApplicationModule {

    private static final String TAG = "ApplicationModule";

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    /**
     * 提供context
     *
     * @return
     */
    @Provides
    @PerApp
    public Context provideApplicationContext() {
        return context.getApplicationContext();
    }

    /**
     * Provides最终解决第三方类库依赖注入问题
     * Module中的创建类实例方法用Provides进行标注，Component在搜索到目标类中用Inject注解标注的属性后，
     * Component就会去Module中去查找用Provides标注的对应的创建类实例方法，
     * 这样就可以解决第三方类库用dagger2实现依赖注入了
     *
     * @return
     */
    @Provides
    @PerApp
    public Bus provideBusEvent() {
        return new Bus();
    }

    /**
     * 获取OkHttpClient
     *
     * @return
     */
    @Provides
    @PerApp
    @Named("api")
    // 区分返回类型相同的@Provides 方法
    OkHttpClient provideApiOkHttpClient(final Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.i(message);
//                Log.i("hwt", "数据\n" + message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(new TokenInterceptor(context))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder1 = request.newBuilder();
                        builder1.addHeader("key", Constants.app_key);
//                        builder1.addHeader("app_type", "2");
//                        builder1.addHeader("OS_type", "android");
                        builder1.addHeader("app_type", "android");
                        String deviceId = PhoneUtil.getDeviceId(context);
                        builder1.addHeader("device_id", deviceId);
                        builder1.addHeader("app_version", String.valueOf(CommonUtils.getVersionCode(context)));
                        Request build = builder1.build();
                        return chain.proceed(build);
                    }
                });

        OkHttpClient okHttpClient = RetrofitUrlManager.getInstance().with(builder).build();
        return okHttpClient;
    }

    @Provides
    @PerApp
    OkHttpClient provideOkHttpClient(@Named("api") OkHttpClient mOkHttpClient) {
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new TokenInterceptor(context));
        builder.interceptors().clear();
        return builder.build();
    }

    @Provides
    @PerApp
    SPUtil provideSPUtil(Context context) {
        return new SPUtil(context);
    }


    @Provides
    @PerApp
    RequestHelper provideRequestHelper(Context mContext, UserStorage mUserStorage) {
        return new RequestHelper(mContext, mUserStorage);
    }

    @Provides
    @PerApp
    UserStorage provideUserStorage(Context mContext, SPUtil spUtil) {
        return new UserStorage(mContext, spUtil);
    }
}
