package proxy.com.tencent.mobileqq.troop.honor;

import java.lang.reflect.Method;

import me.zpp0196.reflectx.proxy.MethodGetter;
import me.zpp0196.reflectx.proxy.ProxyFactory;
import me.zpp0196.reflectx.proxy.Source;

@Source(ITroopHonorConfig.CLASS_NAME)
public interface ITroopHonorConfig {

    String CLASS_NAME = "com.tencent.mobileqq.troop.honor.TroopHonorConfig";

    String METHOD_PARSE_CONFIG = "parseConfig";

    static ITroopHonorConfig proxy() {
        return ProxyFactory.proxyClass(ITroopHonorConfig.class);
    }

    @MethodGetter(value = METHOD_PARSE_CONFIG, parameterTypes = String.class)
    Method parseConfig();
}
