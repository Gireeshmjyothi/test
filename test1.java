@Override
public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    logger.info("Checking origin header");

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    String requestOrigin = request.getHeader(HttpHeaders.ORIGIN);

    if ("*".equals(allowedOrigins)) {
        // If allowedOrigins is '*', allow all requests
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    if (StringUtils.isEmpty(requestOrigin) || Arrays.stream(allowedOrigins.split(","))
            .noneMatch(origin -> StringUtils.equalsIgnoreCase(origin.trim(), requestOrigin))) {
        logger.debug("Origin '{}' is not allowed for URI: {}", requestOrigin, request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(buildErrorResponse());
        return;
    }

    filterChain.doFilter(servletRequest, servletResponse);
}
