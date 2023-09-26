package com.wsj.easyhook.app;
import com.wsj.easyhook.IXposedHookAbstract;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 数独去广告
 */
public class SgHook extends IXposedHookAbstract{

    public SgHook(){
        packageName = "com.gale.sanguokill.hd";
        TAG = "三国";
        version = 2;
        debug = false;
    }

    public  void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        skipAd(lpparam);
    }

    private  void skipAd(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookConstructor("com.sanguoq.android.sanguokill.util.f", lpparam.classLoader, Integer.TYPE, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log("WaitingDialog 时间：" + param.args[0]);
                param.args[0] = 2;
            }
        });
    }
}
