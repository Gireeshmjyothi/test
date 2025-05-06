jar {
    enabled = true
    archiveClassifier.set('thin')
    from sourceSets.main.output
    manifest {
        attributes 'Main-Class':'com.rajput.SpringbootJspSparkDemoApplication'
    }
