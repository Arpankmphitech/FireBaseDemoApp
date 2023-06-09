# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

# Add this global rule
#-keep class com.example.firebasedemoapp.MainActivity
#-keep class com.example.firebasedemoapp.LoginActivity
#-keep class com.example.firebasedemoapp.EmployeeInfo
#-keepattributes Signature
#
#-keepclassmembers class com.example.firebasedemoapp.** {
#*;
#}

-keep class com.startapp.** {
 *;
}

-keep class com.truenet.** {
 *;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**

-dontwarn org.jetbrains.annotations.**

-keep class com.example.firebasedemoapp.EmployeeInfo { <init>(...); }
-keep class com.example.firebasedemoapp.LoginActivity { <init>(...); }
-keep class com.example.firebasedemoapp.MainActivity { <init>(...); }


#-adaptresourcefilenames    **.properties,**.gif,**.jpg
#-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF




