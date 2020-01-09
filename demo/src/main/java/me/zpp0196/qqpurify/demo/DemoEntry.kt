package me.zpp0196.qqpurify.demo

import android.content.Context
import android.util.Log
import android.util.SparseArray

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

import me.zpp0196.reflectx.proxy.ProxyClass

import org.json.JSONObject

import proxy.android.app.IApplication

import proxy.com.tencent.mobileqq.app.IQQAppInterface
import proxy.com.tencent.mobileqq.troop.honor.ITroopHonorConfig
import proxy.com.tencent.mobileqq.troop.honor.ITroopHonorManager
import proxy.mqq.app.IAppRuntime

/**
 * @author zpp0196
 */
class DemoEntry : IXposedHookLoadPackage {

    private lateinit var mAppInterface: IQQAppInterface

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.tencent.mobileqq") return

        hookBefore(proxy<IApplication>().attach()) attach@{
            val context = it.arg<Context>() ?: return@attach
            val classLoader = context.classLoader
            // ProguardMappingFactory.init(context) // 暂时不需要
            ProxyClass.setDefaultClassLoader(classLoader)

            hookAfter(proxy<IAppRuntime>().init()) init@{ param ->
                mAppInterface = checkAndProxy(param.thisObject) ?: return@init
                closeMiniAppSwitch(context)
                closeTroopHonor()
            }
        }
    }

    private fun closeTroopHonor() {
        fun resetConfig(config: ITroopHonorConfig) {
            Log.i("TroopHonor.manager", "config: $config}")
            config.setSupport(false).setHonorMap(SparseArray<Any>())
        }

        val manager = mAppInterface.getManager(346) ?: return
        val honorManager: ITroopHonorManager = proxy(manager.get())
        val config = honorManager.config() ?: return
        resetConfig(config)
        hookAfter(config.parseConfig() ?: return) parseConfig@{ param ->
            resetConfig(proxy(param.result ?: return@parseConfig))
        }
    }

    private fun closeMiniAppSwitch(context: Context) {
        val fileName = "conf_${mAppInterface.currentAccountUin}_content_sharepref"
        val sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        sp.all.mapValues { (key, value) ->
            if (!key.startsWith("425")) {
                return@mapValues
            }
            val json = JSONObject(value.toString())
            json.keys().forEach { k ->
                if (k.endsWith("_mini_app_on")) {
                    json.put(k, 0)
                }
            }
            sp.edit().putString(key, json.toString()).apply()
        }
    }
}