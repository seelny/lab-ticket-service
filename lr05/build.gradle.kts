plugins {
    id("java")
}

group = "org"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lr01"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}