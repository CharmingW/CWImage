# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Software\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 保留资源文件属性和行号
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# 保留所有重要组件
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# 保留所有含有特殊 Context 参数构造方法的类
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet, int);
}

## 保留所有 Parcelable 实现类的特殊属性.
-keepclassmembers class * implements android.os.Parcelable {
     static android.os.Parcelable$Creator CREATOR;
}

## 有些 Android support 包里面的 api 不是在所有平台都存在的，但是我们自己用的时候有数就行
-dontwarn android.support.**

## 用到枚举的地方
-keepclassmembers class * extends java.lang.Enum {
     public static **[] values();
        public static ** valueOf(java.lang.String);
}

#apache
-dontwarn org.apache.**

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

## 用到序列化的实体类
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
        static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

## for DexGuard only
#-keepresourcexmlelements manifest/application/meta-date@value=GlideModule

-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
#-dontwarn org.greenrobot.greendao.database.**

# If you do not use RxJava:
-dontwarn rx.**

-keep public class net.sqlcipher.** {
*;
}

-keep public class net.sqlcipher.database.** {
*;
}

#不混淆org.apache.http.legacy.jar
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**

# solve the exception:  java.lang.AssertionError: illegal type variable reference
-keepattributes EnclosingMethod

# Don't remove any GreenRobot classes
-dontwarn org.greenrobot.**

#使用注解需要添加
-keepattributes *Annotation*

-dontwarn javax.annotation.**

-dontwarn java.nio.file.**

-dontwarn org.codehaus.**

