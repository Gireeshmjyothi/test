class java.util.LinkedHashMap cannot be cast to class io.kubernetes.client.util.generic.dynamic.DynamicKubernetesObject (java.util.LinkedHashMap is in module java.base of loader 'bootstrap'; io.kubernetes.client.util.generic.dynamic.DynamicKubernetesObject is in unnamed module of loader 'app')

public String submitSparkJob(SparkSubmitDto request, String nameSpace) {
        try {
            // 1. Build Kubernetes ApiClient dynamically
            logger.info("Build Kubernetes ApiClient dynamically with request : {}", request);
            ApiClient client = buildApiClient(request.getMasterUrl(), request.getBearerToken());

            // 2. Create Generic API client for SparkApplication CRD
            GenericKubernetesApi<DynamicKubernetesObject, DynamicKubernetesListObject> sparkApi = new GenericKubernetesApi<>(DynamicKubernetesObject.class, DynamicKubernetesListObject.class, request.getGroup(), request.getVersion(), "sparkapplications", client);
            logger.info("Generic kubernetes API : {}", sparkApi);
            // 3. Build SparkApplication manifest
            Map<String, Object> sparkApp = new HashMap<>();
            sparkApp.put("apiVersion", request.getApiVersion());
            sparkApp.put("kind", "SparkApplication");

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("name", "spark-job-" + System.currentTimeMillis());
            metadata.put("namespace", nameSpace);
            sparkApp.put("metadata", metadata);

            logger.info("Build SparkApplication manifest sparkAPI: {}, metaData: {}", sparkApp, metadata);

            Map<String, Object> spec = new HashMap<>();
            spec.put("type", request.getType());
            spec.put("mode", request.getMode());
            spec.put("image", request.getImage());
            spec.put("mainClass", request.getMainClass());
            spec.put("mainApplicationFile", request.getMainApplicationFile());
            spec.put("arguments", request.getArguments());
            spec.put("serviceAccount", request.getServiceAccount());

            logger.info("Mapped spec request : {}", spec);

            Map<String, Object> driver = new HashMap<>();
            driver.put("cores", request.getDriverCores());
            driver.put("memory", request.getDriverMemory());
            driver.put("serviceAccount", request.getServiceAccount());

            logger.info("Driver : {}", driver);

            Map<String, Object> executor = new HashMap<>();
            executor.put("cores", request.getExecutorCores());
            executor.put("instances", request.getExecutorInstances());
            executor.put("memory", request.getExecutorMemory());
            logger.info("Executor : {}", executor);

            spec.put("driver", driver);
            spec.put("executor", executor);
            sparkApp.put("spec", spec);

            // 4. Submit CRD to Kubernetes
            ObjectMapper mapper = new ObjectMapper();
            Object crd = mapper.convertValue(sparkApp, Object.class);

            logger.info("Submit CRD to Kubernetes : {}", crd);
            sparkApi.create((DynamicKubernetesObject) crd);
        }catch (Exception ex){
            logger.error("Error while submitting spark job using k8s client API : {}", ex.getMessage());
            ex.printStackTrace();
            throw  new OpsException(GENERIC_ERROR_CODE, MessageFormat.format(GENERIC_ERROR_MESSAGE_WITH_REASON, ex.getMessage()));
        }
        return "Spark job submitted successfully!";
    }

    private ApiClient buildApiClient(String masterUrl, String bearerToken) {
        ApiClient client = new ClientBuilder()
                .setBasePath(masterUrl)
                .setVerifyingSsl(false)
                //.setAuthentication(new AccessTokenAuthentication(bearerToken))
                .build();
        Configuration.setDefaultApiClient(client);
        return client;
    }
        
