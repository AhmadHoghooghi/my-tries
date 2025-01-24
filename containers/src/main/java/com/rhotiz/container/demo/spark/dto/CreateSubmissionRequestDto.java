package com.rhotiz.container.demo.spark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubmissionRequestDto {
    private String appResource;               // Path to the application JAR or Python file
    @Builder.Default
    private Map<String, String> sparkProperties = new HashMap<>(); // Spark configuration properties
    private String clientSparkVersion;        // Version of the Spark client
    private String mainClass;                 // Main class of the application
    @Builder.Default
    private Map<String, String> environmentVariables = new HashMap<>(); // Environment variables
    @Builder.Default
    private String action = "CreateSubmissionRequest"; // Action to perform
    @Builder.Default
    private List<String> appArgs= new ArrayList<>();                 // Command-line arguments for the application
}