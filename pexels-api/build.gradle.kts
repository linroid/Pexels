import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
	alias(libs.plugins.multiplatform)
	id("com.android.library")
	alias(libs.plugins.kotlinx.serialization)
}

kotlin {
	androidTarget {
		compilations.all {
			kotlinOptions {
				jvmTarget = "${JavaVersion.VERSION_1_8}"
				freeCompilerArgs += "-Xjdk-release=${JavaVersion.VERSION_1_8}"
			}
		}
	}

	jvm()

	@OptIn(ExperimentalWasmDsl::class)
	wasmJs {
		browser()
		binaries.executable()
	}

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64()
	).forEach {
		it.binaries.framework {
			baseName = "pexels-api"
			isStatic = true
		}
	}

	sourceSets {
		commonMain.dependencies {
			implementation(libs.napier)
			implementation(libs.kotlinx.coroutines.core)
			implementation(libs.ktor.client.core)
			implementation(libs.ktor.client.logging)
			implementation(libs.ktor.client.auth)
			implementation(libs.ktor.client.content.negotiation)
			implementation(libs.ktor.client.serialization.kotlinx.json)
		}

		commonTest.dependencies {
			implementation(kotlin("test"))
			implementation(libs.kotlinx.coroutines.test)
		}

		androidMain.dependencies {
			implementation(libs.kotlinx.coroutines.android)
			implementation(libs.ktor.client.okhttp)
		}

		jvmMain.dependencies {
			implementation(libs.kotlinx.coroutines.swing)
			implementation(libs.ktor.client.okhttp)
		}

		iosMain.dependencies {
			implementation(libs.ktor.client.darwin)
		}
	}
}

android {
	namespace = "com.linroid.pexels.api"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	sourceSets["main"].apply {
		manifest.srcFile("src/androidMain/AndroidManifest.xml")
		res.srcDirs("src/androidMain/res")
	}
	// https://developer.android.com/studio/test/gradle-managed-devices
	@Suppress("UnstableApiUsage")
	testOptions {
		targetSdk = 34
		managedDevices.devices {
			maybeCreate<ManagedVirtualDevice>("pixel5").apply {
				device = "Pixel 5"
				apiLevel = 34
				systemImageSource = "aosp"
			}
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
}
