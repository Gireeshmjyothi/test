 Algorithm negotiation fail: algorithmName="server_host_key" jschProposal="ssh-ed25519,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521,rsa-sha2-512,rsa-sha2-256" serverProposal="ssh-rsa"


// jsch
	implementation("com.github.mwiede:jsch:0.2.25")
this is used in another service (RandSService) to connecto sftp
and the blow deplendeces in other SFTP service which give me the localhost sftp server to connect
	// apache sshd for compile scope (RELEASE is not recommended in Gradle, use a fixed version instead)
	implementation 'org.apache.sshd:sshd-core:2.1.0'
	implementation 'org.apache.sshd:sshd-sftp:2.1.0'

	// https://mvnrepository.com/artifact/com.github.mwiede/jsch
	implementation("com.github.mwiede:jsch:0.2.25")

	// apache sshd for test scope
	testImplementation 'org.apache.sshd:sshd-core:2.1.0'
	testImplementation 'org.apache.sshd:sshd-sftp:2.1.0'


earlier it was working with 	// jsch
	implementation 'com.jcraft:jsch:0.1.54'

but now it is not working with updated version which is mentioned above

