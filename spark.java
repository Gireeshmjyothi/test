package com.example.spark.service;

import com.example.spark.dto.SparkJobRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import io.kubernetes.client.util.generic.GenericKubernetesApi;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SparkJobService {

    public String submitSparkJob(SparkJobRequest request) throws Exception {
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
        sparkApp.put("apiVersion", "sparkoperator.k8s.io/v1beta1");
        sparkApp.put("kind", "SparkApplication");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", "spark-job-" + System.currentTimeMillis());
        metadata.put("namespace", request.getNamespace());
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

        sparkApi.create((KubernetesObject) crd);

        return "Spark job submitted successfully!";
    }

    private ApiClient buildApiClient(String masterUrl, String bearerToken) {
        ApiClient client = new ClientBuilder()
                .setBasePath(masterUrl)
                .setVerifyingSsl(false) // ⚠️ disable SSL for dev; replace with CA cert for prod
                .setAuthentication(new AccessTokenAuthentication(bearerToken))
                .build();
        Configuration.setDefaultApiClient(client);
        return client;
    }
}


package com.example.spark.controller;

import com.example.spark.dto.SparkJobRequest;
import com.example.spark.service.SparkJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spark")
@RequiredArgsConstructor
public class SparkSubmitController {

    private final SparkJobService sparkJobService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitSparkJob(@RequestBody SparkJobRequest request) {
        try {
            String result = sparkJobService.submitSparkJob(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to submit Spark job: " + e.getMessage());
        }
    }
}

package com.example.spark.dto;

import lombok.Data;
import java.util.List;

@Data
public class SparkJobRequest {
    private String masterUrl;
    private String bearerToken;
    private String namespace;

    private String type;
    private String mode;
    private String image;
    private String mainClass;
    private String mainApplicationFile;
    private List<String> arguments;
    private String serviceAccount;
    private String driverCores;
    private String driverMemory;
    private String executorCores;
    private String executorInstances;
    private String executorMemory;
}

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class SparkJobService {

    public String submitJob(SparkJobRequest req) throws Exception {
        // Build ApiClient dynamically
        ApiClient client = new ClientBuilder()
                .setBasePath(req.getMasterUrl())
                .setVerifyingSsl(false) // disable SSL verify if self-signed certs
                .setAuthentication(new io.kubernetes.client.util.credentials.AccessTokenAuthentication(req.getBearerToken()))
                .build();

        // increase timeout
        OkHttpClient httpClient = client.getHttpClient().newBuilder()
                .readTimeout(0, TimeUnit.SECONDS)
                .build();
        client.setHttpClient(httpClient);

        Configuration.setDefaultApiClient(client);

        // Example: create a Pod (you’ll replace with SparkApplication CRD later)
        V1Pod pod = new V1Pod()
                .metadata(new V1ObjectMeta()
                        .name("spark-job-driver-" + System.currentTimeMillis())
                        .namespace(req.getNamespace()))
                .spec(new V1PodSpec()
                        .serviceAccountName(req.getServiceAccount())
                        .containers(Collections.singletonList(
                                new V1Container()
                                        .name("spark-driver")
                                        .image(req.getImage())
                                        .args(req.getArguments())
                                        .resources(new V1ResourceRequirements()
                                                .putRequestsItem("cpu", new Quantity(req.getDriverCores()))
                                                .putRequestsItem("memory", new Quantity(req.getDriverMemory()))
                                        )
                        ))
                        .restartPolicy("Never"));

        CoreV1Api api = new CoreV1Api();
        V1Pod createdPod = api.createNamespacedPod(req.getNamespace(), pod, null, null, null, null);

        return "Pod Created: " + createdPod.getMetadata().getName();
    }
}
