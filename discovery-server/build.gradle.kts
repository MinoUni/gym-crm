plugins {
    id("java")
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

extra["springCloudVersion"] = libs.versions.springCloud.get()

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")

    testImplementation(libs.springBootStarterTest)

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