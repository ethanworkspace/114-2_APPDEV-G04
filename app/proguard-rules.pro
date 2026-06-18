# Fish Market App ProGuard Rules
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.fishmarket.app.data.model.** { *; }
-keep class com.google.gson.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**
