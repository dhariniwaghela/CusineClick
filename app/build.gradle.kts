plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}
android {
    namespace = "com.example.cusineclick"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cusineclick"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
buildFeatures{
    viewBinding=true
}
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("com.google.code.gson:gson:2.8.7")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("com.google.firebase:firebase-database-ktx:20.2.2")
   implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation("com.android.car.ui:car-ui-lib:2.5.1")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.core:core-ktx:+")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("androidx.core:core-ktx:+")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.1")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    testImplementation("junit:junit:4.13.2")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.facebook.android:facebook-login:5.15.3")
    //glide dependancy
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //circle image
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //image slider
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


}