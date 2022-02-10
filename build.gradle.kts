version="0.59"
group="com.baremetalcloud"

plugins {
  kotlin("multiplatform") version("1.6.10") 
  id("com.vanniktech.maven.publish") version("0.15.1") 
  kotlin("plugin.serialization") version("1.6.10") 
}

repositories {
  mavenLocal()
  mavenCentral()
  maven(uri("https://dl.bintray.com/kotlin/kotlinx"))
}


kotlin {
  explicitApiWarning()
  jvm {
    compilations.all {
      kotlinOptions.freeCompilerArgs = listOf("-Xmulti-platform")
      kotlinOptions.allWarningsAsErrors
    }
  }
  js(LEGACY) {
    nodejs()
    useCommonJs()
  }
  val hostOs = System.getProperty("os.name")
      val isMingwX64 = hostOs.startsWith("Windows")
      val nativeTarget = when {
          hostOs == "Mac OS X" -> macosX64("native")
          hostOs == "Linux" -> linuxX64("native")
          isMingwX64 -> mingwX64("native")
          else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
      }
  explicitApiWarning()
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.kotlinx.datetime)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation("com.baremetalcloud:multiplatform-runblocking:1.1")
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(kotlin("stdlib-js"))
      }
    }
    val jsTest by getting {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }
    val nativeMain by getting {
      dependencies {
      }
    }
    val nativeTest by getting {
      dependencies {
      }
    }
  }
}


plugins.withId("com.vanniktech.maven.publish") {
  mavenPublish {
    sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
  }
}



