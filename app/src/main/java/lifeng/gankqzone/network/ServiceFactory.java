package lifeng.gankqzone.network;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lifeng.gankqzone.MyApplication;
import lifeng.gankqzone.config.ApiConfig;
import lifeng.gankqzone.config.ConnectTimeoutConfig;
import lifeng.gankqzone.util.SizeConvertUtil;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static LogInterceptor sLogInterceptor;

    private static LogInterceptor getLogInterceptor() {
        /********************使用的自定义的网络请求日志********************/
        if (sLogInterceptor == null) {
            sLogInterceptor = new LogInterceptor();
        }
        return sLogInterceptor;
    }

    public static <T> T getService(Class<T> tClass) {
        return getRetrofit().create(tClass);
    }


    public static OkHttpClient getOkHttpClient() {
        /*** 设置缓存路径:/data/user/0/lifeng.gankqzone/cache 缓存大小:100M ***/
        Cache cache = new Cache(new File(MyApplication.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);

        Log.i("ServiceFactory", "缓存路径:" + MyApplication.getContext().getCacheDir());
        try {
            Log.i("ServiceFactory", "当前缓存大小:" + SizeConvertUtil.getPrintSize(cache.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new OkHttpClient.Builder().cache(cache)
                .connectTimeout(ConnectTimeoutConfig.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(ConnectTimeoutConfig.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(ConnectTimeoutConfig.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(getLogInterceptor())
                .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .build();
    }

    /**
     * 干货集中营
     * @return
     */
    public static Retrofit getRetrofit() {
        /********************配置网络环境BaseHost地址********************/
//        url="https://172.16.0.142:8080";
        return new Retrofit.Builder()
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(getOkHttpClient())
                //设置baseUrl
                .baseUrl(ApiConfig.GANK_API)
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 煎蛋网
     * @return
     */
    public static Retrofit getJianDanRetrofit(){
        return new Retrofit.Builder()
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(getOkHttpClient())
                //设置baseUrl
                .baseUrl(ApiConfig.JIANDAN_API)
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 凤凰视频
     * @return
     */
    public static Retrofit getFengHuangVideoRetrofit(){
        return new Retrofit.Builder()
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(getOkHttpClient())
                //设置baseUrl
                .baseUrl(ApiConfig.FH_VIDEO_API)
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

}
