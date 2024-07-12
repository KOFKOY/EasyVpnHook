package com.wsj.easyhook;

import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class IXposedHookAbstract implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    public String packageName = "";

    public String TAG = "日志XposedHood";
    /**
     * 每次修改代码最好改下，看日志方便知道修改的代码是否生效。不改也没影响
     */
    public int version = 1;
    public boolean debug = true;
    public Gson gson = new Gson();

    public void log(String log) {
        if (debug) {
            XposedBridge.log(TAG + ": " + log);
        }
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }

    public void initHideAd(String className,ClassLoader classLoader, List<Integer> adList) {
        Class<?> viewClass = XposedHelpers.findClass(className, classLoader);

        XposedHelpers.findAndHookMethod(viewClass, "onMeasure", Integer.TYPE,Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View) param.thisObject;
                    int id = view.getId();
                    if (adList.contains(id)) {
                        view.setVisibility(View.GONE);
                        log("View类型 已隐藏广告ID:" + id);
                    }
            }
        });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        log("启用hook");
        log("代码版本:" + version);
        hook(lpparam);
    }

    public void hook(XC_LoadPackage.LoadPackageParam lpparam){

    }

    public boolean canHook(String packageName) {
        return this.packageName.equals(packageName);
    }
}
