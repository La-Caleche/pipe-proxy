plugins {
    id("fr.lacaleche.caldle")
}

dependencies {
    api(libs.caleche.pipe)

    compileOnlyApi(libs.velocity.api)

    api(libs.cloud.velocity)
    api(libs.adventure.text.minimessage)
}
