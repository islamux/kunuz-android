# kotlinx-serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep all @Serializable classes in the app
-keep @kotlinx.serialization.Serializable class com.islamux.kunuz.** {
    *** Companion;
}
-keepclassmembers @kotlinx.serialization.Serializable class com.islamux.kunuz.** {
    *** Companion;
}
-keepclassmembers class com.islamux.kunuz.**$$serializer { *; }

# Keep serial names
-keepclassmembers class com.islamux.kunuz.** {
    *** Companion;
    <fields>;
    <methods>;
    *** INSTANCE;
}

# kotlinx.serialization internals
-keep class kotlinx.serialization.json.** { *; }
-keep class kotlinx.serialization.KSerializer { *; }
-keep class kotlinx.serialization.internal.** { *; }
-keep class kotlinx.serialization.descriptors.** { *; }
-keep class kotlinx.serialization.modules.** { *; }

# Don't warn about kotlinx-serialization
-dontwarn kotlinx.serialization.**
