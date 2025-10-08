plugins {
    id("java")
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.lombok)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

val mockitoAgent = configurations.create("mockitoAgent")

extra["springCloudVersion"] = libs.versions.springCloud.get()

dependencies {
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }

    implementation("org.springframework.boot:spring-boot-starter-artemis")
    implementation(libs.springCloudStarterEurekaClient)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.hypersistenceUtils)
    implementation(libs.datasourceProxySpringBootStarter)
    implementation(libs.apacheCommonsLang3)
    implementation("org.liquibase:liquibase-core")
    implementation(libs.mapstruct)

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor(libs.mapstructProcessor)
    annotationProcessor(libs.lombokMapstructBinding)

    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springSecurityTest)
    testImplementation(libs.cucumberJunit)
    testImplementation(libs.cucumberSpring)
    testImplementation(libs.cucumberJava)
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.junit.platform:junit-platform-suite-api")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    systemProperty("spring.profiles.active", "test")
    useJUnitPlatform()
    systemProperty("cucumber.junit-platform.naming-strategy", "long")
}

tasks.bootRun {
    val envVars = loadEnvVars(file("${rootProject.projectDir}/.env"))
    if (envVars.isEmpty()) {
        throw GradleException(".env file is missing or empty")
    }
    systemProperty("spring.profiles.active", "dev")
    environment(envVars)
}

fun loadEnvVars(file: File): Map<String, String> {
    if (!file.exists()) {
        return emptyMap()
    }
    return file.readLines().filter { it.isNotBlank() && !it.startsWith("#") && it.contains("=") }.associate {
        val (key, value) = it.split("=", limit = 2)
        key to value
    }
}