pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "7.4.2" apply false
        id("com.android.library") version "7.4.2" apply false
        id("org.jetbrains.kotlin.android") version "2.0.21" apply false  // Update Kotlin version here
        id("com.google.devtools.ksp") version "2.0.21" apply false         // Update KSP version here
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Lab4"
include(":app")
