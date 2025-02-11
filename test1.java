Caused by: java.lang.IllegalStateException: Failed to introspect Class [com.sbi.epay.logging.utility.LoggingInterceptor] from ClassLoader [jdk.internal.loader.ClassLoaders$AppClassLoader@5a07e868]
	at org.springframework.util.ReflectionUtils.getDeclaredMethods(ReflectionUtils.java:483)
	at org.springframework.util.ReflectionUtils.doWithLocalMethods(ReflectionUtils.java:320)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.checkLookupMethods(AutowiredAnnotationBeanPostProcessor.java:476)
	... 33 common frames omitted
Caused by: java.lang.NoClassDefFoundError: javax/servlet/http/HttpServletRequest
	at java.base/java.lang.Class.getDeclaredMethods0(Native Method)
	at java.base/java.lang.Class.privateGetDeclaredMethods(Class.java:3578)
	at java.base/java.lang.Class.getDeclaredMethods(Class.java:2676)
	at org.springframework.util.ReflectionUtils.getDeclaredMethods(ReflectionUtils.java:465)
	... 35 common frames omitted
Caused by: java.lang.ClassNotFoundException: javax.servlet.http.HttpServletRequest
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
