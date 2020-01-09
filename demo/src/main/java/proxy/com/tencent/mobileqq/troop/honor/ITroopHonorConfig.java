package proxy.com.tencent.mobileqq.troop.honor;

import android.util.SparseArray;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;

import me.zpp0196.reflectx.proxy.IProxyClass;
import me.zpp0196.reflectx.proxy.ProxySetter;
import me.zpp0196.reflectx.proxy.Source;

@Source
public interface ITroopHonorConfig extends IProxyClass {

    @Nullable
    default Method parseConfig() {
        Class<?> sourceClass = getSourceClass();
        for (Method method : sourceClass.getMethods()) {
            if (method.getReturnType() == sourceClass) {
                return method;
            }
        }
        return null;
    }

    @ProxySetter
    ITroopHonorConfig setHonorMap(SparseArray a);

    @ProxySetter
    ITroopHonorConfig setSupport(boolean a);
}