package proxy.com.tencent.mobileqq.troop.honor;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

import me.zpp0196.reflectx.proxy.IProxyClass;
import me.zpp0196.reflectx.proxy.ProxyFactory;
import me.zpp0196.reflectx.proxy.Source;

@Source
public interface ITroopHonorManager extends IProxyClass {
    @Nullable
    default ITroopHonorConfig config() {
        Class<?> sourceClass = getSourceClass();
        for (Field field : sourceClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                sourceClass.getMethod("a", field.getType(), boolean.class);
                Object config = field.get(get());
                if (config == null) {
                    continue;
                }
                return ProxyFactory.proxyObject(ITroopHonorConfig.class, config);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
