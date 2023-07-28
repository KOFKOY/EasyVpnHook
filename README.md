# EasyVpnHook
第一个hook应用
任意输入账号密码即可登录使用

* ### [app反编译工具jadx](https://github.com/skylot/jadx/releases)
* ### 1.创建空项目,引入xposed库，先修改settings.gradle,再修改app的build.gradle
```xml
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
    google()
    mavenCentral()
    maven { url 'https://api.xposed.info/' }  // 添加这一行即可
    }
    }
```
```xml
dependencies {
    //其他的依赖可以删掉
    compileOnly 'de.robv.android.xposed:api:82' 
    // compileOnly 'de.robv.android.xposed:api:82:sources' // 不要导入源码，这会导致idea无法索引文件，从而让语法提示失效
}
```
* ### 2.删除src/res/values/themes.xml 和 values-night 下的主题文件。并移除AndroidManifest.xml中的主题引用
* ### 3.创建模块作用域文件，在values目录下创建arrays.xml。内容如下
```xml
<resources>
    <string-array name="xposedscope" >
        <!-- 这里填写模块的作用域应用的包名，可以填多个。 -->
        <item>com.sangfor.vpn.client.phone</item> 
    </string-array>
</resources>
```
* ### 4.修改启动配置。在Run那里编辑一下启动配置，勾选Always install with package manager并且将Launch Options改成Nothing
* ### 5.声明模块  修改AndroidManifest.xml
```xml
<application ... > 
        <!-- 是否为Xposed模块 -->
        <meta-data
            android:name="xposedmodule"
            android:value="true"/>
        <!-- 模块的简介（在框架中显示） -->
        <meta-data
            android:name="xposeddescription"
            android:value="我是Xposed模块简介" />
        <!-- 模块最低支持的Api版本 一般填54即可 -->
        <meta-data 
            android:name="xposedminversion"     
            android:value="54"/>
        <!-- 模块作用域 -->
        <meta-data
            android:name="xposedscope"
            android:resource="@array/xposedscope"/>
</appication>
```
* ### 6.声明入口类。在src/main目录下创建一个文件夹名叫assets，并且创建一个文件叫xposed_init，注意，它没有后缀名！！在里面写入一会新建的入口类的路径，如 com.wsj.easyvpnhook.MainHook
* ### 7.新建入口类。创建类 实现IXposedHookLoadPackage接口。 
* ### 8.以上差不多就是xposed模块开发的初始化操作。然后就是反编译app，查看要hook的类 方法，编写代码 测试, 在lsp中打印日志的方法是 XposedBridge.log("");    adb logcat | grep "查看日志"

[参考的博客](https://blog.ketal.icu/cn/Xposed%E6%A8%A1%E5%9D%97%E5%BC%80%E5%8F%91%E5%85%A5%E9%97%A8%E4%BF%9D%E5%A7%86%E7%BA%A7%E6%95%99%E7%A8%8B/)

## ========补充知识点=========
## [xposed官方文档](https://api.xposed.info/reference/packages.html)
### xposed 工作原理
Android系统里有一个叫“Zygote”的进程，它是Android运行时的核心，每个Android应用都通过它的副本的形式被fork出来。Zygote进程在系统启动时被/init.rc脚本启动，同时启动了用来加载必要的classes和执行初始化方法的/system/bin/app_process进程。

这就到了Xposed起作用的地方，当Xposed framework安装成功后，一个扩展的app_process被拷贝到/system/bin.这个扩展的app_process添加了一个额外的jar到classpath中，以在一定的时机调用jar里的方法。比如，在VM创建完成后，甚至Zygote的main方法被调用前。这样我们就可以在这个上下文执行我们想要的操作了。

这个jar位于/data/data/de.robv.android.xposed.installer/bin/XposedBridge.jar，它的源码在[这里](https://github.com/rovo89/XposedBridge)。打开[XposedBridge](https://github.com/rovo89/XposedBridge/blob/master/src/de/robv/android/xposed/XposedBridge.java)这个类，可以看到main方法，它在app_process启动时被调用。一些初始化和modules的加载也在这个时机被完成（稍后会将module的加载）。

### 如果要完全替换一个method，可以使用 XC_MethodReplacement，它继承 XC_MethodHook，只需重写replaceHookedMethod即可

### 优先级:  XC_MethodHook 有两个构造方法  XC_MethodHook() 和  XC_MethodHook(int priority)  priority是优先级，值越高优先级越高，越先执行，比如A优先级大于B A.before -> B.before -> original method -> B.after -> A.after。A可以修改B看到的参数，A也可以决定最终的结果。

### 调用执行java方法
```java
//执行本类方法
Object returnObj = XposedHelpers.callMethod(param.thisObject, "方法名", new Class[]{String.class, Integer.class}, "第一个参数", 2);
//执行外部方法 1
Class<?> clzss = XposedHelpers.findClassIfExists("全路径的class", classLoad未测试);
XposedHelpers.callMethod(clzss.getInterfaces(), "method name", "args");
//执行外部方法 2
Class<?> clzss = XposedHelpers.findClass("全路径的class", classLoad未测试);
Method method_name = XposedHelpers.findMethodBestMatch(clzss, "method name", String.class);
Object returnObj = method_name.invoke(clzss.getInterfaces(), "传参");
```

