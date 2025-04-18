import io.github.tropheusj.gradle_testing.plugin.TestPlugin

plugins {
    id("java")
}

apply {
    plugin(TestPlugin::class)
}

group = "io.github.tropheusj"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.0") {
        attributes {
            attribute(TestPlugin.TRANSFORMED, true)
        }
    }
}

tasks.register("resolveRuntime") {
    val view = configurations.runtimeClasspath.get().incoming.artifactView {}.artifacts

    doLast {
        view.artifactFiles.forEach {
            println(it)
        }
    }
}

tasks.register("resolveCompileSources") {
    val view = configurations.compileClasspath.get().incoming.artifactView {
        withVariantReselection()

        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
            attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
            attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.SOURCES))
        }
    }.artifacts

    doLast {
        view.artifactFiles.forEach {
            println(it)
        }
    }
}
