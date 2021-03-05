package com.ssh.shuoshi.components.okhttp;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.MyApplication;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.LoginEntity;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.util.SPUtil;

import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * created by hwt on 2020/12/8
 */
public class TokenInterceptor implements Interceptor {
    private Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi commonApi;

    public TokenInterceptor(Context context) {
        super();
        this.context = context;

    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);


        if (isTokenExpired(response)) {//根据和服务端的约定判断token过期
            //同步请求方式，获取最新的Token
            String newToken = getNewToken(chain);
            //使用新的Token，创建新的请求
            SPUtil spUtil = new SPUtil(context);
            UserStorage mUserStorage = new UserStorage(context, spUtil);
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("token", mUserStorage.getToken())
//                    .header("Authorization", mUserStorage.getToken())
                    .build();
            response.body().close();
            //重新请求
            if (KeyM) {
                return chain.proceed(newRequest);
            } else {
                return response;
            }
        }
        return response;

    }

    private boolean keyV = false;
    private boolean KeyM = true;

    private String getNewToken(Chain chain) throws IOException {
        SPUtil spUtil = new SPUtil(context);
        keyV = false;
        commonApi = new CommonApi(MyApplication.getContext());
        disposables.add(commonApi.userRefreshToken(spUtil.getTOKNE())
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<LoginEntity>, ObservableSource<LoginEntity>>() {
                    @Override
                    public ObservableSource<LoginEntity> apply(@io.reactivex.annotations.NonNull HttpResult<LoginEntity> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LoginEntity loginEntity) throws Exception {
                        SPUtil spUtil = new SPUtil(context);
                        UserStorage mUserStorage = new UserStorage(context, spUtil);
                        mUserStorage.setToken(loginEntity.getToken());
                        keyV = true;
                        KeyM = true;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        keyV = true;
                        KeyM = false;
                    }
                }));

        for (int i = 0; i < 1000; i++) {
            SystemClock.sleep(100);
            if (keyV) {
                return "";
            }
        }

        return "-999";
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        //注意 >>>>>>>>> okhttp3.4.1这里变成了 !HttpHeader.hasBody(response)
        if (!HttpHeaders.hasBody(response)) {
            //END HTTP
        } else {
            BufferedSource source = responseBody.source();
            try {
                source.request(Long.MAX_VALUE); // Buffer the entire body.
            } catch (IOException e) {
                e.printStackTrace();
            }
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                }
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                try {
                    if (!TextUtils.isEmpty(result)) {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject resultStatus = jsonObject.getJSONObject("resultStatus");
                        int code = resultStatus.getInt("code");
                        if (code == Constants.TOKEN_EXPIRED) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
