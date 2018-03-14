package me.zpp0196.qqsimple.hook;

/**
 * Created by zpp0196 on 2017/12/11.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.BuildConfig;

/**
 * 这种方案建议只在开发调试的时候使用，因为这将损耗一些性能(需要额外加载apk文件)，调试没问题后，直接修改xposed_init文件为正确的类即可
 * 可以实现免重启，由于存在缓存，需要杀死宿主程序以后才能生效
 * 这种免重启的方式针对某些特殊情况的hook无效
 * 例如我们需要implements IXposedHookZygoteInit并将自己的一个服务注册为系统服务，这种就必须重启了
 * Created by DX on 2017/10/4.
 */

public class HookLoader implements IXposedHookLoadPackage {

    //新建hook模块时，需要按照实际情况修改下面三项的值
    private String thisAppPackage = BuildConfig.APPLICATION_ID;
    private String handleHookClass = MainHook.class.getName();
    private String handleHookMethod = "handleLoadPackage";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        invokeHandleHookMethod(thisAppPackage, handleHookClass, handleHookMethod, loadPackageParam);
    }

    /**
     * 安装app以后，系统会在/data/app/下备份了一份.apk文件，通过动态加载这个apk文件，调用相应的方法
     * 这样就可以实现，只需要第一次重启，以后修改hook代码就不用重启了
     *
     * @param thisAppPackage   当前app的packageName
     * @param handleHookClass  指定由哪一个类处理相关的hook逻辑
     * @param loadPackageParam 传入XC_LoadPackage.LoadPackageParam参数
     * @throws Exception
     */
    private void invokeHandleHookMethod(String thisAppPackage, String handleHookClass, String handleHookMethodName, XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        File apkFile = findApkFile(thisAppPackage);
        PathClassLoader pathClassLoader = new PathClassLoader(apkFile.getAbsolutePath(), ClassLoader.getSystemClassLoader());
        try {
            Class<?> cls = Class.forName(handleHookClass, true, pathClassLoader);
            Object instance = cls.newInstance();
            Method method = cls.getDeclaredMethod(handleHookMethodName, XC_LoadPackage.LoadPackageParam.class);
            method.invoke(instance, loadPackageParam);
        } catch (Exception e) {
            //throw e;
        }
    }

    /**
     * 寻找这个Android设备上的当前apk文件,不受其它Xposed模块hook SDK_INT的影响
     *
     * @param thisAppPackage
     * @return
     * @throws Exception
     */
    private File findApkFile(String thisAppPackage) throws Exception {
        File apkFile = null;
        try {
            apkFile = findApkFileAfterApi_21(thisAppPackage);
        } catch (Exception e) {
            try {
                apkFile = findApkFileBeforeApi_21(thisAppPackage);
            } catch (Exception ignored) {

            }
        }
        if (apkFile == null) {
            throw new FileNotFoundException("没在/data/app/下找到文件对应的apk文件");
        }
        return apkFile;
    }

    /**
     * 寻找apk文件(api_21之后)
     * 在Android sdk21以及之后，apk文件的路径发生了变化
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    private File findApkFileAfterApi_21(String packageName) throws Exception {
        File apkFile = null;
        File path = new File(String.format("/data/app/%s-%s", packageName, "1"));
        if (!path.exists()) {
            path = new File(String.format("/data/app/%s-%s", packageName, "2"));
        }
        if (!path.exists() || !path.isDirectory()) {
            throw new FileNotFoundException(String.format("没找到目录/data/app/%s-%s", packageName, "1/2"));
        }
        apkFile = new File(path, "base.apk");
        if (!apkFile.exists() || apkFile.isDirectory()) {
            throw new FileNotFoundException(String.format("没找到文件/data/app/%s-%s/base.apk", packageName, "1/2"));
        }
        return apkFile;
    }

    /**
     * 寻找apk文件(api_21之前)
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    private File findApkFileBeforeApi_21(String packageName) throws Exception {
        File apkFile = new File(String.format("/data/app/%s-%s.apk", packageName, "1"));
        if (!apkFile.exists()) {
            apkFile = new File(String.format("/data/app/%s-%s.apk", packageName, "2"));
        }
        if (!apkFile.exists() || apkFile.isDirectory()) {
            throw new FileNotFoundException(String.format("没找到文件/data/app/%s-%s.apk", packageName, "1/2"));
        }
        return apkFile;
    }
}