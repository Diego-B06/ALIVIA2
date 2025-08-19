import org.apache.tools.ant.util.JavaEnvUtils.VERSION_11

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
val buildToolsVersion by extra("36.0.0")
val sourceCompatibility by extra(VERSION_11)
