package com.wsj.easyhook;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public abstract class IXposedHookAbstract implements IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    public String packageName = "";

    public String TAG = "日志XposedHood";

    public void log(String log) {
        XposedBridge.log(TAG + ": " + log);
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {

    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        log("启用hook");
        hook(lpparam);
    }


    public void hook(XC_LoadPackage.LoadPackageParam lpparam){

    }

    public boolean canHook(String packageName) {
        return this.packageName.equals(packageName);
    }
}
