package proxy.mqq.app;

import java.lang.reflect.Method;

import me.zpp0196.reflectx.proxy.IProxyClass;
import me.zpp0196.reflectx.proxy.MethodGetter;
import me.zpp0196.reflectx.proxy.Source;
import proxy.com.tencent.qphone.base.remote.ISimpleAccount;

@Source("mqq.app.AppRuntime")
public interface IAppRuntime extends IProxyClass {
    @MethodGetter(parameterTypes = {IMobileQQ.class, IMainService.class, ISimpleAccount.class})
    Method init();
}
