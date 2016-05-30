# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
-dontpreverify                                                                  # 混淆时是否做预校验
-verbose                                                                        # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法

#相关JAR包
-libraryjars libs/alipaySdk-20151215.jar
-libraryjars libs/AMap3DMap_3.3.1.jar
-libraryjars libs/fastjson-1.2.7.jar
-libraryjars libs/GetuiExt-2.0.3.jar
-libraryjars libs/GetuiSDK2.8.1.0.jar
-libraryjars libs/httpclient-4.5.1.jar
-libraryjars libs/httpcore-4.4.4.jar
-libraryjars libs/KJFrameForAndroid_v2.245.jar
-libraryjars libs/libammsdk.jar
-libraryjars libs/mta-sdk-1.6.2.jar
-libraryjars libs/open_sdk_r5509.jar
-libraryjars libs/ormlite-android-4.49-SNAPSHOT.jar
-libraryjars libs/ormlite-core-4.49-SNAPSHOT.jar
-libraryjars libs/pinyin4j-2.5.0.jar
-libraryjars libs/umeng-analytics-v5.6.7.jar
-libraryjars libs/umeng-update-v2.7.0.jar

-libraryjars src/main/jniLibs/libbspatch.so
-libraryjars src/main/jniLibs/libgdinamapv4sdk752.so
-libraryjars src/main/jniLibs/libgdinamapv4sdk752ex.so
-libraryjars src/main/jniLibs/libgetuiext.so

#友盟统计开始
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class [com.supaiclient.app].R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#友盟统计结束

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment


#友盟
-keep class com.umeng.**{*;}

#支付宝
-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.lib.ResourceMap{*;}

#信鸽推送
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}


#自己项目特殊处理代码

#忽略警告
-dontwarn com.veidy.mobile.common.**
#保留一个完整的包
-keep class com.veidy.mobile.common.** {
    *;
 }

-keep class  com.veidy.activity.login.WebLoginActivity{*;}
-keep class  com.veidy.activity.UserInfoFragment{*;}
-keep class  com.veidy.activity.HomeFragmentActivity{*;}
-keep class  com.veidy.activity.CityActivity{*;}
-keep class  com.veidy.activity.ClinikActivity{*;}

#如果引用了v4或者v7包
-dontwarn android.support.**

############混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature

#移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}