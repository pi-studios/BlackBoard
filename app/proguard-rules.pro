-keep class com.pistudiosofficial.myclass.objects.UserObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.StudentClassObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.ResourceBucketObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.PostObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.PollOptionValueLikeObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.NotificationStoreObj.** {*;}
-keep class com.pistudiosofficial.myclass.objects.MasterPostObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.HelloListObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.FeedbackMainAdminObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.FeedbackExportObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.CommentObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.ClassObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.ChatObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.ChatListObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.ChatListMasterObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.AdminClassObject.** {*;}
-keep class com.pistudiosofficial.myclass.objects.AccountListObject.** {*;}


-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Hide warnings about references to newer platforms in the library
-dontwarn android.support.v7.**
# don't process support library
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-dontwarn com.opencsv.**
-dontwarn org.apache.commons.beanutils.**
-dontwarn org.apache.commons.collections.**
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-dontoptimize

-keep class org.jetbrains.kotlin.** { *; }
-keep class org.jetbrains.annotations.** { *; }
-keepclassmembers class ** {
  @org.jetbrains.annotations.ReadOnly public *;
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-keep class com.shockwave.**






