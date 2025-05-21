 Task :shadowJar FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':shadowJar'.
> org.apache.tools.zip.Zip64RequiredException: archive contains more than 65535 entries.

  To build this archive, please enable the zip64 extension.
  See: https://docs.gradle.org/8.9/dsl/org.gradle.api.tasks.bundling.Zip.html#org.gradle.api.tasks.bundling.Zip:zip64

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.9/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 1m 52s
