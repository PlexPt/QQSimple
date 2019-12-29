package me.zpp0196.qqpurify.pro

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import me.zpp0196.qqpurify.pro.ProguardProxyClass.TYPE_FIELD
import me.zpp0196.qqpurify.pro.ProguardProxyClass.TYPE_METHOD
import me.zpp0196.reflectx.proxy.IProguardMapping
import me.zpp0196.reflectx.proxy.ProxyClass
import java.lang.Exception

class ProguardMappingFactory private constructor() : IProguardMapping {

    private val mMappingList = arrayListOf<ProguardMapping>()

    override fun getSource(name: String, signature: String, hashcode: Long): String {
        val result = getSource0(name, signature, hashcode)
        Log.v(TAG, "getSource() called with: name = [$name], " +
                "signature = [$signature], hashcode = [$hashcode], result = [$result]")
        return result ?: name
    }

    private fun getSource0(name: String, signature: String, hashcode: Long): String? {
        return when (mMappingList.isEmpty()) {
            true -> name
            false -> {
                for (mapping in mMappingList) {
                    return when (signature.isEmpty()) {
                        true -> getSourceClass(mapping, name)
                        false -> getSourceMember(mapping, name, signature, hashcode)
                    } ?: continue
                }
                null
            }
        }
    }

    private fun getSourceClass(mapping: ProguardMapping, name: String): String? {
        return mapping.classes[name]?.clazz
    }

    private fun getSourceMember(mapping: ProguardMapping, name: String, className: String, type: Long): String? {
        return mapping.classes[className]?.run {
            when (type) {
                TYPE_FIELD -> fields[name]
                TYPE_METHOD -> methods[name]
                else -> null
            }
        }
    }

    companion object {

        private const val TAG = "ProguardMappingFactory"
        var instance: ProguardMappingFactory? = null
            private set

        fun init(context: Context) {
            instance = ProguardMappingFactory().apply {
                mMappingList.apply {
                    context.getExternalFilesDir("proguard")?.listFiles { file ->
                        if (file.name.matches(Regex("\\d+\\.json"))) {
                            try {
                                add(Gson().fromJson(file.readText(), ProguardMapping::class.java))
                            } catch (ignored: Exception) {
                            }
                        }
                        false
                    }
                    sortByDescending { mapping -> mapping.version }
                    forEach { mapping ->
                        Log.d(TAG, "loaded mapping version: ${mapping.version}")
                    }
                }
                ProxyClass.addProguardMapping(this)
            }
        }
    }
}