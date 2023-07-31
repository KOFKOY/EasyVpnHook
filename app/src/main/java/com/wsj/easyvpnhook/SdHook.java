package com.wsj.easyvpnhook;
import android.app.Activity;
import android.content.Intent;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SdHook implements IXposedHookLoadPackage {
    private String packageName = "cn.ktidata.redappm.sd";
    private final String TAG = "数独: ";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!packageName.equals(lpparam.packageName)) {
            return;
        }
        log("hook success");
        hook(lpparam);
    }

    private void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        skipAd(lpparam);
    }

    private void skipAd(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(Activity.class, "startActivity", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Intent originalIntent = (Intent) param.args[0];
                String originalClassName = originalIntent.getComponent().getClassName();
                log("hook 广告");
                if (originalClassName.equals("com.study.learningsudoku.feiniuad.RewardVideoActivity")) {
                    Intent newIntent = new Intent();
                    newIntent.setClassName(originalIntent.getComponent().getPackageName(), "com.study.learningsudoku.studyhelper.StudyHelperMainActivity");
                    param.args[0] = newIntent;
                }
            }
        });
    }

    private void log(String str) {
        XposedBridge.log(TAG + str);
    }
}
