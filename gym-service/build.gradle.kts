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
val jjwtVersion = libs.versions.jjwt.get()

extra["springCloudVersion"] = libs.versions.springCloud.get()

dependencies {
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }

    implementation(libs.springCloudStarterEurekaClient)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.springAspects)
    implementation(libs.springAop)
    implementation(libs.hypersistenceUtils)
    implementation(libs.datasourceProxySpringBootStarter)
    implementation(libs.apacheCommonsLang3)
    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql")
    implementation(libs.mapstruct)
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

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
    systemProperty("spring.profiles.active", "test")
    useJUnitPlatform()
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
    return file.readLines()
        .filter { it.isNotBlank() && !it.startsWith("#") && it.contains("=") }.associate {
            val (key, value) = it.split("=", limit = 2)
            key to value
        }
}