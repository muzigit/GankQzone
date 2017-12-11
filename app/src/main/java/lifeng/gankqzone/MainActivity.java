package lifeng.gankqzone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import lifeng.gankqzone.activity.FengHVideoActivity;
import lifeng.gankqzone.base.BaseActivity;
import lifeng.gankqzone.fragment.main.FindFragment;
import lifeng.gankqzone.fragment.main.MineFragment;
import lifeng.gankqzone.fragment.main.PrettyImageFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content)
    FrameLayout mFrameLayout;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.container)
    LinearLayout mLlContainer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setListener();
        mNavigation.setSelectedItemId(R.id.navigation_home);//设置默认选中
    }

    private void setListener() {
        /*** 设置底部导航栏 ***/
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*** 设置侧滑菜单 ***/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_camera://

                break;
            case R.id.nav_video://视频
                ActivityUtils.startActivity(this, FengHVideoActivity.class);
                break;
            case R.id.nav_cartoon:

                break;
//            case R.id.nav_manage:
//
//                break;
            case R.id.nav_share:

                break;
            case R.id.nav_send:

                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private FindFragment mFindFragment;
    private PrettyImageFragment mPrettyImageFragment;
    private MineFragment mMineFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (mFindFragment != null) {
                fragmentTransaction.hide(mFindFragment);
            }
            if (mPrettyImageFragment != null) {
                fragmentTransaction.hide(mPrettyImageFragment);
            }
            if (mMineFragment != null) {
                fragmentTransaction.hide(mMineFragment);
            }

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (mFindFragment == null) {
                        mFindFragment = new FindFragment();
                        fragmentTransaction.add(R.id.content, mFindFragment);
                    } else {
                        fragmentTransaction.show(mFindFragment);
                    }
                    break;
                case R.id.navigation_dashboard:
                    if (mPrettyImageFragment == null) {
                        mPrettyImageFragment = new PrettyImageFragment();
                        fragmentTransaction.add(R.id.content, mPrettyImageFragment);
                    } else {
                        fragmentTransaction.show(mPrettyImageFragment);
                    }
                    break;
                case R.id.navigation_notifications:
                    if (mMineFragment == null) {
                        mMineFragment = new MineFragment();
                        fragmentTransaction.add(R.id.content, mMineFragment);
                    } else {
                        fragmentTransaction.show(mMineFragment);
                    }
                    break;
            }
            fragmentTransaction.commit();
            return true;
        }
    };

    private long lastTime;

    /**
     * 连续按回退键两次退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime = System.currentTimeMillis();
            if (curTime - lastTime < 2000) {
//                ActivityUtils.finishAllActivities();//结束所有activity
//                AppManager.getAppManager().AppExit(this);
                AppUtils.exitApp();
                return super.onKeyDown(keyCode, event);
            } else {
                lastTime = curTime;
                Toast.makeText(getApplicationContext(), R.string.exit_app, Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}
