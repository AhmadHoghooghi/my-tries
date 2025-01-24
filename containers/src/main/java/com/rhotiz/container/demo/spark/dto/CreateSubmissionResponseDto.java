package com.rhotiz.container.demo.spark.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CreateSubmissionResponseDto {
    /**
     * Always should be CreateSubmissionResponse
     */
    private String action; // Action performed
    private String message;                            // Message describing the result
    private String serverSparkVersion;                 // Version of the Spark server
    private String submissionId;                       // ID of the submitted application
    private boolean success;                           // Whether the submission was successful
}