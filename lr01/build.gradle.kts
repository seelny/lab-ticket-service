plugins {
    application
}

application {
    mainClass.set("org.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}






dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")
}

tasks.test {
    useJUnitPlatform()
}