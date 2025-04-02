 @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Checking origin header");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (StringUtils.isEmpty(request.getHeader(HttpHeaders.ORIGIN)) || Arrays.stream(allowedOrigins.split(",")).noneMatch(origin -> StringUtils.equalsIgnoreCase(origin.trim(), request.getHeader(HttpHeaders.ORIGIN)))) {
            logger.debug("Origin is invalid for URI: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(buildErrorResponse());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
