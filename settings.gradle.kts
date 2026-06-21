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
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // Repositorio local para los .aar de Unity
        flatDir {
            dirs("${rootDir}/unityExport/unityLibrary/libs")
        }
    }
}

rootProject.name = "dsa_android"
include(":app")

// Módulo Unity exportado
include(":unityLibrary")
project(":unityLibrary").projectDir = file("unityExport/unityLibrary")
