tasks.register('thinJar', Jar) {
    group = 'build'
    description = 'Creates a thin JAR for Spark'
    archiveClassifier.set('thin')
    from sourceSets.main.output
    manifest {
        attributes 'Main-Class': 'com.rajput.SpringbootJspSparkDemoApplication'
    }
}
