package me.zpp0196.qqpurify.demo

import android.content.Context

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

import me.zpp0196.qqpurify.pro.ProguardMappingFactory
import me.zpp0196.reflectx.proxy.ProxyClass

import org.json.JSONObject

import proxy.android.app.IApplication

import proxy.com.tencent.mobileqq.activity.IConversation
import proxy.com.tencent.mobileqq.troop.honor.ITroopHonorConfig

import java.io.File
import java.lang.Exception

/**
 * @author zpp0196
 */
class DemoEntry : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.tencent.mobileqq") return

        hookBefore(IApplication.proxy().attach()) {
            val context = it.arg<Context>()
            ProguardMappingFactory.init(context)
            ProxyClass.setDefaultClassLoader(context.classLoader)

            replaceMethod(IConversation.proxy().initMiniAppEntryLayout())
            hookBefore(ITroopHonorConfig.proxy().parseConfig()) { param ->
                val configPath = context.getExternalFilesDir("honor")
                File(configPath, "config.json.bak").writeText(param.arg())
                File(configPath, "config.json").readText().run {
                    if (isNullOrEmpty()) {
                        return@run
                    }
                    param.args[0] = try {
                        JSONObject(this).toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return@run
                    }
                }
            }
        }
    }
}