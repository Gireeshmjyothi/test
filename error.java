ERROR org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Handler dispatch failed: java.lang.NoClassDefFoundError: scala/Serializable] with root cause
java.lang.ClassNotFoundException: scala.Serializable
	at jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641) ~[?:?]
	at jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188) ~[?:?]
implementation 'org.scala-lang:scala-library:2.12.15'
