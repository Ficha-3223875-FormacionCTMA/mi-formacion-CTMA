// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroidSetup) apply false
    alias(libs.plugins.kotlinComposeSetup) apply false
    alias(libs.plugins.ksp) apply false
}
