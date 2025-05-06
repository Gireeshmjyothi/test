// Disable bootJar to avoid BOOT-INF layout
bootJar {
    enabled = false
}

jar {
    enabled = true
    manifest {
        attributes(
            'Main-Class': 'com.rajput.SpringbootJspSparkDemoApplication'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
