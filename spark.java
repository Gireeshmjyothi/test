public Object submitSparkJob(String namespace, String jobName, SparkSubmitDto sparkSubmitDto) {
        logger.info("Submit job started for jobName: {}, namespace: {}, sparkSubmitDto: {}",
                jobName, namespace, sparkSubmitDto);

        CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);

        // Convert DTO → SparkApplication spec (as Map)
        Map<String, Object> sparkApp = sparkJobMapper.toSparkApplicationSpec(sparkSubmitDto, jobName, namespace);

        try {
            CustomObjectsApi.APIcreateNamespacedCustomObjectRequest request =
                    customObjectsApi.createNamespacedCustomObject(
                    sparkSubmitDto.getGroup(),       // from DTO
                    sparkSubmitDto.getVersion(),     // from DTO
                    namespace,
                    "sparkapplications",             // CRD plural
                    sparkApp
            );

            logger.info("Spark job [{}] submitted successfully in namespace [{}]", jobName, namespace);
            return request.execute();

        } catch (Exception e) {
            logger.error("Unexpected exception during Spark job submission", e);
            return e.getMessage();
        }
    }


Map<String, Object> toSparkApplicationSpec(SparkSubmitDto sparkSubmitDto, String jobName, String namespace) {
        Map<String, Object> sparkApp = new HashMap<>();

        // Metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", jobName);
        metadata.put("namespace", namespace);

        sparkApp.put("apiVersion", sparkSubmitDto.getApiVersion());
        sparkApp.put("kind", "SparkApplication");
        sparkApp.put("metadata", metadata);

        // Spec
        Map<String, Object> spec = new HashMap<>();
        spec.put("type", sparkSubmitDto.getType());
        spec.put("mode", sparkSubmitDto.getMode());
        spec.put("image", sparkSubmitDto.getImage());
        spec.put("mainClass", sparkSubmitDto.getMainClass());
        spec.put("mainApplicationFile", sparkSubmitDto.getMainApplicationFile());
        spec.put("arguments", sparkSubmitDto.getArguments());
        spec.put("serviceAccount", sparkSubmitDto.getServiceAccount());

        // Driver
        Map<String, Object> driver = new HashMap<>();
        driver.put("cores", sparkSubmitDto.getDriverCores());
        driver.put("memory", sparkSubmitDto.getDriverMemory());
        driver.put("serviceAccount", sparkSubmitDto.getServiceAccount()); // recommended to set in driver spec too
        spec.put("driver", driver);

        // Executor
        Map<String, Object> executor = new HashMap<>();
        executor.put("cores", sparkSubmitDto.getExecutorCores());
        executor.put("instances", sparkSubmitDto.getExecutorInstances());
        executor.put("memory", sparkSubmitDto.getExecutorMemory());
        spec.put("executor", executor);

        sparkApp.put("spec", spec);


Message: 
HTTP response code: 403
HTTP response body: {
    "kind": "Status",
    "apiVersion": "v1",
    "metadata": {},
    "status": "Failure",
    "message": "sparkapplications.sparkoperator.k8s.io is forbidden: User \"system:serviceaccount:dev-rns:default\" cannot create resource \"sparkapplications\" in API group \"sparkoperator.k8s.io\" in the namespace \"dev-spark\"",
    "reason": "Forbidden",
    "details": {
        "group": "sparkoperator.k8s.io",
        "kind": "sparkapplications"
    },
    "code": 403
}

HTTP response headers: {audit-id=[e7f217ee-8d9d-4008-b373-998dbb9885ef
    ], cache-control=[no-cache, private
    ], content-length=[
        397
    ], content-type=[application/json
    ], date=[Mon,
        01 Sep 2025 11: 27: 17 GMT
    ], strict-transport-security=[max-age=31536000; includeSubDomains; preload
    ], x-content-type-options=[nosniff
    ], x-kubernetes-pf-flowschema-uid=[
        229489e7-103f-47ec-b39f-46b3b34b3b28
    ], x-kubernetes-pf-prioritylevel-uid=[c66b97b3-078b-4822-af28-524fea32adac
    ]
}
