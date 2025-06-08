plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ll"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    testImplementation("org.springframework.security:spring-security-test")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    //actuator 설정
    implementation ("org.springframework.boot:spring-boot-starter-actuator")

    // Spring Boot Admin 클라이언트 의존성
    implementation ("de.codecentric:spring-boot-admin-starter-client:3.1.7")

    // 모니터링 도구들
    implementation ("io.micrometer:micrometer-registry-prometheus")

    // JPA 쿼리 모니터링을 위한 도구들
    implementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")
    implementation ("com.github.gavlyukovskiy:datasource-proxy-spring-boot-starter:1.9.0")

    // Zipkin 분산 추적을 위한 의존성 (Spring Boot 3.x)
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
