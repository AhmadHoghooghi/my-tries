package com.rhotiz.container.demo.spark.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SubmissionStatusResponseDto {
    /**
     * Always should be : SubmissionStatusResponse
     */
    private String action ; // Action performed
    private DriverState driverState;                        // State of the driver (e.g., RUNNING, FINISHED)
    private String serverSparkVersion;                 // Version of the Spark server
    private String submissionId;                       // ID of the submitted application
    private boolean success;                           // Whether the status check was successful
    private String workerHostPort;                     // Host and port of the worker
    private String workerId;                           // ID of the worker
}