  @Value("${security.cors.allowed.origins:*}")
    private String allowedOrigins;

    /**
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain     Provides access to the next filter in the chain for this filter to pass the request and response
     *                        to for further processing
     * @throws IOException      Exception
     * @throws ServletException Exception
     */
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

    /**
     * Create build error response string
     * @return response string
     */
    private String buildErrorResponse() {
        try {
            ErrorDto errorDto = ErrorDto.builder().errorCode(ErrorConstants.CORS_ERROR_CODE).errorMessage(ErrorConstants.CORS_ERROR_MESSAGE).build();
            AdminResponse<String> adminResponse = AdminResponse.<String>builder().status(AdminConstants.FAILURE_RESPONSE_CODE).errors(List.of(errorDto)).build();
            return objectMapper.writeValueAsString(adminResponse);
        } catch (JsonProcessingException e) {
            logger.error("Json format error while generating error message");
            return StringUtils.EMPTY;
        }
    }
