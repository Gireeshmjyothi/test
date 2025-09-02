public String submitSparkJob(SparkSubmitDto request, String nameSpace) throws Exception {
        // 1. Build Kubernetes ApiClient dynamically
        ApiClient client = buildApiClient(request.getMasterUrl(), request.getBearerToken());

        // 2. Create Generic API client for SparkApplication CRD
        GenericKubernetesApi<Object, Object> sparkApi = new GenericKubernetesApi<>(
                Object.class,
                Object.class,
                "sparkoperator.k8s.io",
                "v1beta1",
                "sparkapplications",
                client
        );

        // 3. Build SparkApplication manifest
        Map<String, Object> sparkApp = new HashMap<>();
        sparkApp.put("apiVersion", request.getApiVersion());
        sparkApp.put("kind", "SparkApplication");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", "spark-job-" + System.currentTimeMillis());
        metadata.put("namespace", nameSpace);
        sparkApp.put("metadata", metadata);

        Map<String, Object> spec = new HashMap<>();
        spec.put("type", request.getType());
        spec.put("mode", request.getMode());
        spec.put("image", request.getImage());
        spec.put("mainClass", request.getMainClass());
        spec.put("mainApplicationFile", request.getMainApplicationFile());
        spec.put("arguments", request.getArguments());
        spec.put("serviceAccount", request.getServiceAccount());

        Map<String, Object> driver = new HashMap<>();
        driver.put("cores", request.getDriverCores());
        driver.put("memory", request.getDriverMemory());
        driver.put("serviceAccount", request.getServiceAccount());

        Map<String, Object> executor = new HashMap<>();
        executor.put("cores", request.getExecutorCores());
        executor.put("instances", request.getExecutorInstances());
        executor.put("memory", request.getExecutorMemory());

        spec.put("driver", driver);
        spec.put("executor", executor);
        sparkApp.put("spec", spec);

        // 4. Submit CRD to Kubernetes
        ObjectMapper mapper = new ObjectMapper();
        Object crd = mapper.convertValue(sparkApp, Object.class);

        sparkApi.create(crd);

        return "Spark job submitted successfully!";
    }

    private ApiClient buildApiClient(String masterUrl, String bearerToken) {
        ApiClient client = new ClientBuilder()
                .setBasePath(masterUrl)
                .setVerifyingSsl(false)
                .setAuthentication(new AccessTokenAuthentication(bearerToken))
                .build();
        Configuration.setDefaultApiClient(client);
        return client;
    }
