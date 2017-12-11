package lifeng.gankqzone;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;

import lifeng.gankqzone.base.BaseActivity;

/**
 * Created by lifeng on 2017/10/25.
 *
 * @description
 */
public class IndexActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ScreenUtils.setFullScreen(this);//设置为全屏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }
}
