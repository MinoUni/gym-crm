plugins {
    id("java")
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

extra["springCloudVersion"] = libs.versions.springCloud.get()

dependencies {
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc")
    implementation(libs.springCloudStarterEurekaClient)

    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springSecurityTest)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.test {
    useJUnitPlatform()
}