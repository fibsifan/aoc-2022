plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    wrapper {
        gradleVersion = "7.6"
        distributionType = Wrapper.DistributionType.ALL
    }
}

tasks.test {
    useJUnitPlatform()
}
