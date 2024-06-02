
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}
group = "com.example.blogmultiplatform"
version = "1.0-SNAPSHOT"


kotlin {
    js(IR) {browser()}
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {}
        }

        val jsMain by getting {
            dependencies {}
        }

        val jvmMain by getting {
            dependencies {}
        }
    }
}
