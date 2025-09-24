@Bean
public WebClient webClient(WebClient.Builder builder) {
    HttpClient httpClient = HttpClient.create()
            // Connection timeout (ms)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            // Response timeout (how long to wait for the server to send response headers)
            .responseTimeout(Duration.ofSeconds(60))
            // Add read/write timeouts on the connection
            .doOnConnected(conn -> 
                conn.addHandlerLast(new ReadTimeoutHandler(60))
                    .addHandlerLast(new WriteTimeoutHandler(60))
            )
            // Your existing SSL setup
            .secure(sslContextSpec -> {
                try {
                    sslContextSpec.sslContext(
                        SslContextBuilder.forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                            .build()
                    );
                } catch (SSLException e) {
                    throw new RuntimeException(e);
                }
            });

    return builder
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
}
