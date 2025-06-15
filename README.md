📱 Social Guard App
Social Guard is an Android application built using Kotlin that helps users protect their social media presence by detecting and removing hateful comments from their posts. The app integrates with platforms like Instagram, Facebook, and Twitter via their APIs, and uses a lightweight machine learning model to identify toxic or abusive content in real time. Designed with a clean UI and seamless login experience, the app promotes a safer and more respectful online environment.

Key Features:

Social media login and post access

Hate speech detection using ML

Option  hide negative comments

Built with Kotlin and Android’s latest practices



Important Note: This project was built using a specific version of Gradle and Android Gradle Plugin. If you face Gradle sync issues or version compatibility errors, please follow these steps:

Open the project in your Android Studio.

When prompted, choose "Update Gradle Plugin" or "Update Gradle Wrapper".

Alternatively, manually adjust the following files:

gradle/wrapper/gradle-wrapper.properties → update distributionUrl to match your local Gradle version.

build.gradle (Project level) → update classpath 'com.android.tools.build:gradle:X.X.X' based on your Android Studio version.
