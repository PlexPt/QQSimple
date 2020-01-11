package proxy.com.tencent.mobileqq.troop.honor;

import android.util.SparseArray;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;

import reflectx.IProxyClass;
import reflectx.annotations.SetField;
import reflectx.annotations.Source;

@Source
public interface ITroopHonorConfig extends IProxyClass {

    @Nullable
    default Method parseConfig() {
        Class<?> sourceClass = requireSourceClass();
        for (Method method : sourceClass.getMethods()) {
            if (method.getReturnType() == sourceClass) {
                return method;
            }
        }
        return null;
    }

    @SetField
    ITroopHonorConfig setHonorMap(SparseArray a);

    @SetField
    ITroopHonorConfig setSupport(boolean a);
}