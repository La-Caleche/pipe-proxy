plugins {
    id("fr.lacaleche.caldle") version "1.2.3"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(libs.caleche.pipe)

    compileOnlyApi(libs.velocity.api)

    api(libs.cloud.velocity)
    api(libs.adventure.text.minimessage)
}
