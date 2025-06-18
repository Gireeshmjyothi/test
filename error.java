
tasks.withType(JavaExec).configureEach {
	jvmArgs += [
			"--add-exports", "java.base/sun.util.calendar=ALL-UNNAMED",
            "--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED",
			"--add-exports", "java.base/sun.security.action=ALL-UNNAMED"
	]
}
