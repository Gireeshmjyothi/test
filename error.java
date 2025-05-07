// apache sshd for test scope
	testImplementation 'org.apache.sshd:sshd-core:2.1.0'
	testImplementation 'org.apache.sshd:sshd-sftp:2.1.0'

	// apache sshd for compile scope (RELEASE is not recommended in Gradle, use a fixed version instead)
	implementation 'org.apache.sshd:sshd-core:2.1.0'
	implementation 'org.apache.sshd:sshd-sftp:2.1.0'

	// https://mvnrepository.com/artifact/com.github.mwiede/jsch
	implementation("com.github.mwiede:jsch:0.2.25")
