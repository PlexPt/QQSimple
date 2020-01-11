package proxy.com.tencent.mobileqq.troop.honor;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

import reflectx.IProxyClass;
import reflectx.ProxyFactory;
import reflectx.Reflectx;
import reflectx.annotations.Source;

@Source(value = "azay", version = 1296)
@Source(value = "azcd", version = 1320)
public interface ITroopHonorManager extends IProxyClass {
    @Nullable
    default ITroopHonorConfig config() {
        Class<?> sourceClass = requireSourceClass();
        for (Field field : sourceClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                sourceClass.getMethod("a", field.getType(), boolean.class);
                Object config = field.get(get());
                if (config == null) {
                    continue;
                }
                Reflectx.putSourceClass(ITroopHonorConfig.class, config.getClass());
                return ProxyFactory.proxy(ITroopHonorConfig.class, config);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
