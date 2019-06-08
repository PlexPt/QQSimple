package me.zpp0196.qqpurify.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import me.zpp0196.qqpurify.R;
import me.zpp0196.qqpurify.fragment.AboutPreferenceFragment;
import me.zpp0196.qqpurify.fragment.ChatPreferenceFragment;
import me.zpp0196.qqpurify.fragment.ExtensionPreferenceFragment;
import me.zpp0196.qqpurify.fragment.MainuiPreferenceFragment;
import me.zpp0196.qqpurify.fragment.ReadmeFragment;
import me.zpp0196.qqpurify.fragment.SettingPreferenceFragment;
import me.zpp0196.qqpurify.fragment.SidebarPreferenceFragment;
import me.zpp0196.qqpurify.fragment.TroopPreferenceFragment;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.utils.Setting;
import me.zpp0196.qqpurify.utils.ThemeUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * Created by zpp0196 on 2019/5/15.
 */
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        ViewPager.OnPageChangeListener, OnTabSelectListener, Constants {

    private TextView mTitleTextView;

    public List<TabFragment> mRefreshedFragment = new ArrayList<>();
    private List<TabFragment> mFragments = new ArrayList<>();
    private boolean mRequestedPermission = false;

    private static final int RC_PERMISSION_STORAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtils.getStyleId(this));
        setContentView(R.layout.activity_main);
        initTabLayout();
        initToolbar();
        initSetting();
    }

    private void initTabLayout() {
        if (getIntent().getBooleanExtra(INTENT_LAUNCH, false)) {
            mFragments.add(new MainuiPreferenceFragment());
            mFragments.add(new SidebarPreferenceFragment());
            mFragments.add(new ChatPreferenceFragment());
            mFragments.add(new TroopPreferenceFragment());
            mFragments.add(new ExtensionPreferenceFragment());
        } else {
            mFragments.add(new ReadmeFragment());
        }
        mFragments.add(new SettingPreferenceFragment());
        mFragments.add(new AboutPreferenceFragment());

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), mFragments));
        viewPager.addOnPageChangeListener(this);
        SlidingTabLayout slidingTabLayout = findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setViewPager(viewPager);
        slidingTabLayout.setOnTabSelectListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mTitleTextView = toolbar.findViewById(R.id.title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        updateTitle(0);
    }

    @AfterPermissionGranted(RC_PERMISSION_STORAGE)
    private void initSetting() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (EasyPermissions.hasPermissions(this, permissions)) {
            try {
                Setting.init();
                // 重新加载数据
                if (mRequestedPermission) {
                    recreate();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return;
        }
        mRequestedPermission = true;
        EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, RC_PERMISSION_STORAGE, permissions)
                .setRationale("模块需要存储空间权限来读写配置文件，拒绝授予该权限意味着模块将无法正常运行，请点击授权按钮给予权限")
                .setPositiveButtonText("授权")
                .setNegativeButtonText("")
                .build());
    }

    private void updateTitle(int position) {
        String actionBarTitle = mFragments.get(position).getToolbarTitle();
        mTitleTextView.setText(actionBarTitle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            initSetting();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initSetting();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("警告")
                    .setRationale("模块未获得存储空间权限将无法正常运行，请点击授权按钮给予权限")
                    .setPositiveButton("授权")
                    .build()
                    .show();
        } else {
            initSetting();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        // 返回修改时间判断是否需要重启QQ
        intent.putExtra(KEY_LAST_MODIFIED, Setting.getLong(KEY_LAST_MODIFIED, System.currentTimeMillis()));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        updateTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelect(int position) {
        updateTitle(position);
    }

    @Override
    public void onTabReselect(int position) {
        updateTitle(position);
    }

    public interface TabFragment {
        String getTabTitle();

        String getToolbarTitle();

        Fragment getFragment();
    }


    public static class MainAdapter extends FragmentPagerAdapter {

        private List<TabFragment> mFragmentList;

        MainAdapter(@NonNull FragmentManager fm, List<TabFragment> fragmentList) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mFragmentList = fragmentList;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentList.get(position).getTabTitle();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
