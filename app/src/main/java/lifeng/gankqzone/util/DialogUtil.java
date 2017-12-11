package lifeng.gankqzone.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import lifeng.gankqzone.R;

/**
 * Created by lifeng on 2017/10/26.
 *
 * @description
 */
public class DialogUtil {

    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        setDialogAttr(loadingDialog);
        loadingDialog.show();

        return loadingDialog;
    }
    
    /*** 无网络时弹出此对话框 ***/
    public static AlertDialog networkExceptionDialog(final Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_network_exception, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);// 创建自定义样式dialog
        builder.setCancelable(true);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        setAlertDialogAttr(alertDialog);
        setDialogDismiss(btnCancel,alertDialog);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到网络设置界面
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        return alertDialog;
    }

    private static void setAlertDialogAttr(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.PopWindowAnimStyle);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            lp.alpha = .8f;//设置的是dialog的背景透明度
            window.setAttributes(lp);
        }
    }

    private static void setDialogAttr(Dialog loadingDialog) {
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
    }

    /**
     * 关闭dialog
     * @param dialog
     */
    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 取消弹出框
     *
     * @param view
     * @param alertDialog
     * @param <T>
     */
    public static <T extends View> void setDialogDismiss(T view, final AlertDialog alertDialog) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
