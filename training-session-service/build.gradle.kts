plugins {
    id("java")
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.lombok)
}

val mockitoAgent = configurations.create("mockitoAgent")
extra["springCloudVersion"] = libs.versions.springCloud.get()

dependencies {
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }

    implementation(libs.springCloudStarterEurekaClient)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.mapstruct)

    runtimeOnly("com.h2database:h2")

    annotationProcessor(libs.mapstructProcessor)
    annotationProcessor(libs.lombokMapstructBinding)

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
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}