plugins {
    id("java")
    id("org.springframework.boot") version "3.5.5" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {

    group = "com.epam.learn"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {

    tasks.withType<JavaCompile>().configureEach {
        options.release = 21
    }
}
