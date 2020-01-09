package proxy.com.tencent.mobileqq.app;

import androidx.annotation.Nullable;

import me.zpp0196.reflectx.proxy.RunWithCatch;
import me.zpp0196.reflectx.proxy.Source;
import proxy.mqq.IManager;
import proxy.mqq.app.IAppRuntime;

@Source(value = "com.tencent.mobileqq.app.QQAppInterface", initialize = false)
public interface IQQAppInterface extends IAppRuntime {
    String getCurrentAccountUin();

    @Nullable
    @RunWithCatch
    IManager getManager(int i);
}
