
-keep class com.baoshen.common.component.** { *; } #自定义控件不参与混淆
-keep class com.baoshen.common.event.EventType {*;} #消息不混
#-keep class com.baoshen.common.component.bubbleseekba.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {
    com.baoshen.common.DontConfuse *;
}
#手动启用support keep注解
-dontskipnonpubliclibraryclassmembers
-printconfiguration
-keep,allowobfuscation @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application