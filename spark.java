public String submitSparkJob(SparkSubmitDto request, String nameSpace) throws Exception {
        // 1. Build Kubernetes ApiClient dynamically
        ApiClient client = buildApiClient(request.getMasterUrl(), request.getBearerToken());

        // 2. Create Generic API client for SparkApplication CRD
        OkHttpClient httpClient = client.getHttpClient().newBuilder()
                .readTimeout(0, TimeUnit.SECONDS)
                .build();
        client.setHttpClient(httpClient);

        Configuration.setDefaultApiClient(client);
        
        V1Pod pod = new V1Pod()
                .metadata(new V1ObjectMeta()
                        .name("spark-job-driver-" + System.currentTimeMillis())
                        .namespace(nameSpace))
                .spec(new V1PodSpec()
                        .serviceAccountName(request.getServiceAccount())
                        .containers(Collections.singletonList(
                                new V1Container()
                                        .name("spark-driver")
                                        .image(request.getImage())
                                        .args(request.getArguments())
                                        .resources(new V1ResourceRequirements()
                                                .putRequestsItem("cpu", new Quantity(request.getDriverCores()))
                                                .putRequestsItem("memory", new Quantity(request.getDriverMemory()))
                                        )
                        ))
                        .restartPolicy("Never"));

        CoreV1Api api = new CoreV1Api();
        V1Pod createdPod = api.createNamespacedPod(nameSpace, pod).execute();

        return "Pod Created: " + createdPod.getMetadata().getName();
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
