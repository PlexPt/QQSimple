package me.zpp0196.qqsimple.fragment.base;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.zpp0196.qqsimple.activity.MainActivity;

/**
 * Created by zpp0196 on 2018/5/26 0026.
 */

public abstract class BaseFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(setLayoutId(), container, false);
        init();
        return rootView;
    }

    @LayoutRes
    protected abstract int setLayoutId();

    @StringRes
    public abstract int getTitleId();

    @SuppressWarnings ("unchecked")
    protected <T extends View> T findViewById(@IdRes int id) {
        return (T) getRootView().findViewById(id);
    }

    protected void init() {}

    public View getRootView() {
        return rootView;
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public SharedPreferences getPrefs() {
        return getMainActivity().getPrefs();
    }

    public SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }

    @Override
    public void onPause() {
        super.onPause();
        getMainActivity().setWorldReadable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMainActivity().setWorldReadable();
    }
}
