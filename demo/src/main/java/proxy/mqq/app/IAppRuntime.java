package proxy.mqq.app;

import java.lang.reflect.Method;

import proxy.com.tencent.qphone.base.remote.ISimpleAccount;

import reflectx.IProxyClass;
import reflectx.annotations.FindMethod;
import reflectx.annotations.Source;

@Source("mqq.app.AppRuntime")
public interface IAppRuntime extends IProxyClass {
    @FindMethod(parameterTypes = {IMobileQQ.class, IMainService.class, ISimpleAccount.class})
    Method init();
}
