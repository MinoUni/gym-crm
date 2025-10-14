plugins {
    id("java")
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(libs.springBootStarterWeb)

    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.cucumberJunit)
    testImplementation(libs.cucumberSpring)
    testImplementation(libs.cucumberJava)
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.junit.platform:junit-platform-suite-api")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
