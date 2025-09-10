plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "audit.aggregator"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Audit Trail"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("software.amazon.awssdk:s3:2.20.143") // latest stable v2
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("software.amazon.awssdk:core:2.20.143")
	implementation("software.amazon.awssdk:auth:2.20.143")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
