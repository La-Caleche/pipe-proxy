dependencies {
    api(project(":library:Pipe"))

    compileOnlyApi(libs.velocity.api)

    api(libs.cloud.velocity)
    api(libs.adventure.text.minimessage)
}
