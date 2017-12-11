package lifeng.gankqzone.config;

/**
 * Created by lifeng on 2017/8/31.
 *
 * @description 网络请求超时时间配置
 */
public interface ConnectTimeoutConfig {

    /********************连接超时时间********************/
    long CONNECT_TIMEOUT = 30000L;
    /********************读取超时时间********************/
    long READ_TIMEOUT = 30000L;
    /********************写入超时时间********************/
    long WRITE_TIMEOUT = 30000L;

}
