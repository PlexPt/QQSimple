package me.zpp0196.qqsimple.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.activity.base.BaseAppCompatActivity;
import me.zpp0196.qqsimple.fragment.AboutFragment;
import me.zpp0196.qqsimple.fragment.ChatPreferenceFragment;
import me.zpp0196.qqsimple.fragment.GroupPreferenceFragment;
import me.zpp0196.qqsimple.fragment.MainFragment;
import me.zpp0196.qqsimple.fragment.MainUIPreferenceFragment;
import me.zpp0196.qqsimple.fragment.OtherPreferenceFragment;
import me.zpp0196.qqsimple.fragment.QZonePreferenceFragment;
import me.zpp0196.qqsimple.fragment.SettingPreferenceFragment;
import me.zpp0196.qqsimple.fragment.SidebarPreferenceFragment;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;
import me.zpp0196.qqsimple.util.ShellUtil;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_ENTER_MODULE_TIMES;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_OPEN_DRAWER;
import static me.zpp0196.qqsimple.util.CommUtil.isInVxp;
import static me.zpp0196.qqsimple.util.ShellUtil.Result.DEFAULT;

/**
 * Created by zpp0196 on 2018/5/25 0025.
 */

public class MainActivity extends BaseAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final Handler drawerHandler = new Handler();
    private DrawerLayout drawerLayout;

    public boolean isModuleActive() {
        return false;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int setRootViewId() {
        return R.id.content_frame;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        long enterModuleTimes = getPrefs().getLong(PREFS_KEY_ENTER_MODULE_TIMES, 0);
        getEditor().putLong(PREFS_KEY_ENTER_MODULE_TIMES, enterModuleTimes + 1)
                .apply();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu()
                .getItem(0)
                .setChecked(true);

        if (!isModuleActive()) {
            navigationView.getMenu()
                    .removeGroup(R.id.main_items);
        }

        // 防止横竖屏切换时切换Fragment
        if (savedInstanceState == null) {
            drawerHandler.removeCallbacksAndMessages(null);
            drawerHandler.postDelayed(() -> navigate(R.id.nav_main), 250);

            boolean openDrawer = getPrefs().getBoolean(PREFS_KEY_OPEN_DRAWER, false);

            if (openDrawer) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawers();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isInVxp(this)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reboot_qq:
                rebootQQ();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint ("HandlerLeak")
    private void rebootQQ() {
        ShellUtil shellUtil = new ShellUtil(this);
        shellUtil.setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ShellUtil.Result result = (ShellUtil.Result) msg.obj;
                switch (msg.what) {
                    case DEFAULT:
                        if (result.getResult()
                                .contains(getString(R.string.shell_stop_finish))) {
                            setWorldReadable();
                            launchApp(PACKAGE_NAME_QQ);
                        } else {
                            showExceptionTip(new Exception(result.getResult()));
                        }
                        break;
                }
            }
        });
        shellUtil.forceStop(PACKAGE_NAME_QQ, getString(R.string.qq_name));
    }

    @SuppressWarnings ("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerHandler.removeCallbacksAndMessages(null);
        drawerHandler.postDelayed(() -> navigate(item.getItemId()), 250);
        drawerLayout.closeDrawers();
        return true;
    }

    private void navigate(final int itemId) {
        Fragment navFragment = null;
        switch (itemId) {
            case R.id.nav_main:
                navFragment = new MainFragment();
                break;
            case R.id.nav_mainui:
                navFragment = new MainUIPreferenceFragment();
                break;
            case R.id.nav_sidebar:
                navFragment = new SidebarPreferenceFragment();
                break;
            case R.id.nav_qzone:
                navFragment = new QZonePreferenceFragment();
                break;
            case R.id.nav_chat:
                navFragment = new ChatPreferenceFragment();
                break;
            case R.id.nav_group:
                navFragment = new GroupPreferenceFragment();
                break;
            case R.id.nav_other:
                navFragment = new OtherPreferenceFragment();
                break;
            case R.id.nav_setting:
                navFragment = new SettingPreferenceFragment();
                break;
            case R.id.nav_about:
                navFragment = new AboutFragment();
                break;
        }

        if (navFragment != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            try {
                ActionBar ab = getSupportActionBar();
                if (ab != null) {
                    int title;
                    if (navFragment instanceof BasePreferenceFragment) {
                        title = ((BasePreferenceFragment) navFragment).getTitleId();
                    } else {
                        title = ((BaseFragment) navFragment).getTitleId();
                    }
                    ab.setTitle(title);
                }
                transaction.replace(R.id.content_frame, navFragment)
                        .commit();
            } catch (IllegalStateException ignored) {

            }
        }
    }
}

