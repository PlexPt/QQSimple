package proxy.com.tencent.mobileqq.activity;

import java.lang.reflect.Method;

import me.zpp0196.reflectx.proxy.MethodGetter;
import me.zpp0196.reflectx.proxy.ProxyFactory;
import me.zpp0196.reflectx.proxy.Source;

@Source(IConversation.CLASS_NAME)
public interface IConversation {

    String CLASS_NAME = "com.tencent.mobileqq.activity.Conversation";

    String METHOD_INIT_MINI_APP_ENTRY_LAYOUT = "initMiniAppEntryLayout";

    static IConversation proxy() {
        return ProxyFactory.proxyClass(IConversation.class);
    }

    @MethodGetter(value = METHOD_INIT_MINI_APP_ENTRY_LAYOUT)
    Method initMiniAppEntryLayout();
}
