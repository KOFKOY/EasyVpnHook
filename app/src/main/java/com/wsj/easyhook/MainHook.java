package com.wsj.easyhook;

import com.wsj.easyhook.app.BiuBiuHook;
import com.wsj.easyhook.app.BkHook;
import com.wsj.easyhook.app.EvHook;
import com.wsj.easyhook.app.F555Hook;
import com.wsj.easyhook.app.FanQieHook;
import com.wsj.easyhook.app.FrpHook;
import com.wsj.easyhook.app.GsHook;
import com.wsj.easyhook.app.SdHook;
import com.wsj.easyhook.app.SettingHook;
import com.wsj.easyhook.app.SgHook;
import com.wsj.easyhook.app.XmlaHook;
import com.wsj.easyhook.app.YydqHook;
import com.wsj.easyhook.app.ZqHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    List<IXposedHookAbstract> list = new ArrayList<>();

    public MainHook(){
        list.add(new EvHook());
//        list.add(new FanQieHook());
//        list.add(new FrpHook());
//        list.add(new SdHook());
//        list.add(new ZqHook());
//        list.add(new BkHook());
//        list.add(new F555Hook());
//        list.add(new SgHook());
        list.add(new SettingHook());
//        list.add(new XmlaHook());
//        list.add(new GsHook());
//        list.add(new YydqHook());
//        list.add(new BiuBiuHook());
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (IXposedHookAbstract hookAbstract : list) {
            if (hookAbstract.canHook(lpparam.packageName)) {
                hookAbstract.handleLoadPackage(lpparam);
            }
        }
    }
}