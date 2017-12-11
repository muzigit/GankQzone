package lifeng.gankqzone.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lifeng on 2017/10/30.
 *
 * @description
 */
public class DateFormatUtil {

    /***
     * 获取默认格式的日期字符串
     * @param date
     * @return
     */
    public static String getFormatDateStr(final Date date){
        if(null == date)return null;
        return DateFormat.getDateInstance(DateFormat.DEFAULT).format(date);
    }

    public static Date formatDateFromStr(final String dateStr){
        Date date = new Date();
        if(!TextUtils.isEmpty(dateStr)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            try {
                date = sdf.parse(dateStr);
            }catch (Exception e){
                System.out.print("Error,format Date error");
            }
        }
        return date;
    }
}
