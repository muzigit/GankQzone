package lifeng.gankqzone.util;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

/**
 * Created by 峰 on 10:53
 * logger日志
 */
public class LoggerUtils {

    /****************是否打印日志****************/
    private static boolean flag = true;

    /****************设置打印级别****************/
    private static int priority = Logger.WARN;

    /****************日志打印的前缀****************/
    private static final String TAG = "TTT";

    private static final String emptyMessage = "获取不到打印信息";//如果打印的空日志,打印此句

    public static void log(String message, Throwable throwable) {
        if (flag) {
            Logger.log(priority, TAG, message, throwable);
        }
    }

    public static void d(String tag, String message) {
        if (flag) {
            if (!TextUtils.isEmpty(message)) {
                Logger.t(tag).i(message);
                return;
            }
            Logger.t(TAG).d(message);
        }
    }

    public static void e(String tag, String message) {
        if (flag) {
            if (!TextUtils.isEmpty(message)) {
                Logger.t(tag).i(message);
                return;
            }
            Logger.t(TAG).e(message);
        }
    }

    public static void i(String message) {
        if (flag) {
            Logger.t(TAG).i(message);
        }
    }

    public static void i(String tag, String message) {
        if (flag) {
            if (!TextUtils.isEmpty(message)) {
                Logger.t(tag).i(message);
                return;
            }
            Logger.t(TAG).i(message);
        }
    }

    public static void v(String tag, String message) {
        if (flag) {
            if (!TextUtils.isEmpty(message)) {
                Logger.t(tag).i(message);
                return;
            }
            Logger.t(TAG).v(message);
        }
    }

    public static void w(String message) {
        if (flag) {
            Logger.t(TAG).w(message);
        }
    }

    public static void w(String tag, String message) {
        if (flag) {
            if (!TextUtils.isEmpty(message)) {
                Logger.t(tag).i(message);
                return;
            }
            Logger.t(TAG).w(message);
        }
    }

    public static void json(String json) {
        if (flag) {
            if (TextUtils.isEmpty(json)) {
                Logger.t(TAG).w(emptyMessage);
                return;
            }
            Logger.t(TAG).json(json);
        }
    }

    public static void xml(String xml) {
        if (flag) {
            if (TextUtils.isEmpty(xml)) {
                Logger.t(TAG).w(emptyMessage);
                return;
            }
            Logger.t(TAG).xml(xml);
        }
    }
}
