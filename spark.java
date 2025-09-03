ObjectMapper mapper = new ObjectMapper();
ObjectNode sparkApp = mapper.createObjectNode();
sparkApp.put("apiVersion", request.getApiVersion());
sparkApp.put("kind", "SparkApplication");

ObjectNode metadata = sparkApp.putObject("metadata");
metadata.put("name", "spark-job-" + System.currentTimeMillis());
metadata.put("namespace", nameSpace);

ObjectNode spec = sparkApp.putObject("spec");
spec.put("type", request.getType());
spec.put("mode", request.getMode());
spec.put("image", request.getImage());
spec.put("mainClass", request.getMainClass());
spec.put("mainApplicationFile", request.getMainApplicationFile());
spec.putArray("arguments").addAll(
        mapper.valueToTree(request.getArguments())
);
spec.put("serviceAccount", request.getServiceAccount());

ObjectNode driver = spec.putObject("driver");
driver.put("cores", request.getDriverCores());
driver.put("memory", request.getDriverMemory());
driver.put("serviceAccount", request.getServiceAccount());

ObjectNode executor = spec.putObject("executor");
executor.put("cores", request.getExecutorCores());
executor.put("instances", request.getExecutorInstances());
executor.put("memory", request.getExecutorMemory());

// wrap into DynamicKubernetesObject
DynamicKubernetesObject crd = new DynamicKubernetesObject(sparkApp);

sparkApi.create(crd);
