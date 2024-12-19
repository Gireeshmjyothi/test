FAILURE: Build failed with an exception.
* What went wrong:
Execution failed for task ':compileJava'.
> Could not resolve all files for configuration ':compileClasspath'.
   > Could not resolve org.springframework.boot:spring-boot-starter-webflux:3.3.3.
     Required by:
         project :
      > Could not resolve org.springframework.boot:spring-boot-starter-webflux:3.3.3.
         > Could not get resource 'https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-starter-webflux/3.3.3/spring-boot-starter-webflux-3.3.3.pom'.
            > Could not GET 'https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-starter-webflux/3.3.3/spring-boot-starter-webflux-3.3.3.pom'.
               > Got SSL handshake exception during request. It might be caused by SSL misconfiguration
                  > PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.
BUILD FAILED in 3s
