plugins {
  id("java");
  id("org.springframework.boot") version "3.5.14";
  id("io.spring.dependency-management") version "1.1.7";
}

group = "ghastlith";
version = "3.0.0";

repositories {
  mavenCentral();
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21);
  }
}

dependencies {
  // spring
  implementation("org.springframework.boot:spring-boot-starter-web");
  implementation("org.springframework.boot:spring-boot-starter-validation");

  // tests
  testImplementation("org.springframework.boot:spring-boot-starter-test");

  // lombok
  compileOnly("org.projectlombok:lombok:1.18.38");
  annotationProcessor("org.projectlombok:lombok:1.18.38");
  testCompileOnly("org.projectlombok:lombok:1.18.38");
  testAnnotationProcessor("org.projectlombok:lombok:1.18.38");
}

tasks.bootJar {
  archiveVersion.set("");
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform();

  testLogging {
    events("passed", "skipped", "failed");
  }
}
