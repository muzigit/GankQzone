package lifeng.gankqzone.util;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import lifeng.gankqzone.MyApplication;


/**
 * Toast统一管理类
 */
public class T {
    public static Toast toast = null;

    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *  注:这里根据项目需要已经更改为长时间显示Toast
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (TextUtils.isEmpty(message))
            return;
        if (isShow) {
            if (toast != null) {
                toast.setText(message);
                toast.setDuration(Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 120);
            }
            toast.show();
        }
    }


    /**
     * 长时间显示Toast
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (TextUtils.isEmpty(message))
            return;
        if (isShow) {
            Toast toast = Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 120);
            toast.show();
        }
    }


    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        if (TextUtils.isEmpty(message))
            return;
        if (isShow) {
            Toast toast = Toast.makeText(MyApplication.getContext(), message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    

  /*  public static void showFail(){
        if (isShow)
            Toast.makeText(MainApplication.getInstance().getApplicationContext(),MainApplication.getInstance().getApplicationContext().getResources().getString(R.string.request_fail), Toast.LENGTH_LONG).show();
    }*/

    /**
     * 使toast不在显示
     */
    public static void cancelMyToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}