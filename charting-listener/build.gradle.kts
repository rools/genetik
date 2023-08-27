plugins {
    kotlin("jvm") version "1.9.0"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.knowm.xchart:xchart:3.8.5")

    implementation(project(":genetik"))
}