package proxy.com.tencent.mobileqq.activity;

import java.lang.reflect.Method;

import reflectx.IProxyClass;
import reflectx.annotations.FindMethod;
import reflectx.annotations.Source;

@Source("com.tencent.mobileqq.activity.Conversation")
public interface IConversation extends IProxyClass {

    @Deprecated
    @FindMethod
    Method initMiniAppEntryLayout();
}
