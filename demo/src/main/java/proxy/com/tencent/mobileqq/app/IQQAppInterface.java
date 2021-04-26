package proxy.com.tencent.mobileqq.app;

import androidx.annotation.Nullable;

import proxy.mqq.IManager;
import proxy.mqq.app.IAppRuntime;
import reflectx.annotations.RunWithCatch;
import reflectx.annotations.Source;

@Source("com.tencent.mobileqq.app.QQAppInterface")
public interface IQQAppInterface extends IAppRuntime {
    String getCurrentAccountUin();

    @Nullable
    @RunWithCatch
    IManager getManager(int i);
}
