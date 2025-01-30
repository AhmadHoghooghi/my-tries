# Resources
Note: consider that resource allocation is referenced by `scheduling` in these documents. If you wnat
to understand `scheduler` in this terminology consider that in these concepts, things like Mesos, Yarn and K8s are
mentioned as scheduler.

Review basic concepts again from:
* Apache Spark 3 Fundamentals (Pluralsight):
    * 026-Spark Application Concepts Jobs Stages and Tasks-0NiG-git.ir.mp4
    * 051-Dynamic Resource Allocation-Gp3Z-git.ir.mp4
    * 052-Resource Allocation Using Fair Scheduling-4O7j-git.ir.mp4
* Spark Documentation:
    * https://spark.apache.org/docs/3.5.4/cluster-overview.html#glossary

Then read this part from spark documentation:
Resource Allocation in `spark-application` level:
* Resource Scheduling: https://spark.apache.org/docs/3.5.4/spark-standalone.html#resource-scheduling

Resource Allocation for each executor:
* Executors Scheduling: https://spark.apache.org/docs/3.5.4/spark-standalone.html#executors-scheduling

Having Different amount of resource for each stage of application.
* Stage Level Scheduling Overview: https://spark.apache.org/docs/3.5.4/spark-standalone.html#stage-level-scheduling-overview

# Resource Allocation Basics
I see some configuration points for resource allocation:

## worker-memory
| SPARK_WORKER_MEMORY | Env Varialbe | total memory minus 1 GiB |
|---------------------|--------------|--------------------------|

Total amount of memory to allow Spark applications to use on the machine, e.g. 1000m, 2g
(default: total memory minus 1 GiB);
note that each application's individual memory is configured using
its `spark.executor.memory` property.

## worker-core
| SPARK_WORKER_CORES | Env Varialbe | all available cores |
|---------------------|-------------|---------------------|

Total number of cores to allow Spark applications to use on the machine.

## executor-memory
| spark.executor.memory | submit option  |
|-----------------------|----------------|

## executor-cores
| spark.executor.cores | submit option | 
|----------------------|---------------|

```shell
docker exec -it spark-master /opt/bitnami/spark/bin/spark-submit \
--master spark://spark-master:7077 \
--deploy-mode cluster \
--class $SPARK_APP \
--conf spark.eventLog.enabled=true \
--conf spark.eventLog.dir=file:///tmp/spark-events \
--conf spark.executor.memory=512m \
--conf spark.executor.cores=2 \
/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar
```

## driver-memory
| spark.driver.memory | submit option | 
|---------------------|---------------|

## driver-cores
| spark.driver.cores | submit option | 
|--------------------|---------------|

```shell
$ curl -XPOST http://IP:PORT/v1/submissions/create \
--header "Content-Type:application/json;charset=UTF-8" \
--data '{
  "appResource": "",
  "sparkProperties": {
    "spark.master": "spark://master:7077",
    "spark.app.name": "Spark Pi",
    "spark.driver.memory": "1g",
    "spark.driver.cores": "1",
    "spark.jars": ""
  },
  "clientSparkVersion": "",
  "mainClass": "org.apache.spark.deploy.SparkSubmit",
  "environmentVariables": { },
  "action": "CreateSubmissionRequest",
  "appArgs": [ "/opt/spark/examples/src/main/python/pi.py", "10" ]
}'

```

## application default cores
| spark.deploy.defaultCores | SPARK_MASTER_OPTS | infinite |
|---------------------------|-------------------|----------|

Default number of cores to give to applications in Spark's standalone mode if
they don't set spark.cores.max. If not set, applications always get all
available cores unless they configure `spark.cores.max` themselves.
Set this lower on a shared cluster to prevent users from grabbing the whole
cluster by default.

## application cores
| spark.cores.max | sparkConf |
|-----------------|-----------|
```
val conf = new SparkConf()
  .setMaster(...)
  .setAppName(...)
  .set("spark.cores.max", "10")
val sc = new SparkContext(conf)
```


# QA

The paragraph describes **Stage Level Scheduling** in Spark's standalone mode and how it behaves with and without dynamic allocation. Let’s break it down into smaller, more comprehensible parts:

---

### 1. **Stage Level Scheduling Overview**
- **What is Stage Level Scheduling?**
    - Spark normally assigns resources (memory, CPU, etc.) at the application level. Stage Level Scheduling allows you to specify **different resource requirements for each stage** within the same application.
    - This is useful when different stages of a job require varying amounts of resources (e.g., one stage requires more CPU, another requires more memory).

---

### 2. **Behavior When Dynamic Allocation is Disabled**
- **Dynamic allocation disabled**:
    - Executors are fixed after the application starts.
    - In this mode, stage-level scheduling can only adjust **task-level resource requirements** (e.g., CPU cores or memory needed per task) but cannot change the total number of executors allocated.
    - The resource requirements for each stage are fulfilled using the same executors that were originally requested at the start of the application.

---

### 3. **Behavior When Dynamic Allocation is Enabled**
- **Dynamic allocation enabled**:
    - Executors can be dynamically added or removed as needed during application execution.
    - **ResourceProfile IDs**: When using multiple resource profiles (one for each stage), Spark assigns each ResourceProfile an ID.
        - **Scheduling Order**: The Spark Master schedules executors for ResourceProfiles in ascending order of their IDs (ResourceProfiles with smaller IDs are prioritized).
    - This usually doesn’t matter because Spark finishes one stage before starting another. However:
        - In a **job server-like scenario** (multiple jobs running simultaneously in the same Spark context), the scheduling order might impact resource allocation and performance.

#### Example:
- If Stage A has ResourceProfile ID 1 and Stage B has ID 2:
    - Executors will first satisfy the requirements of Stage A before Stage B.

---

### 4. **Built-in vs. Custom Resources**
- **Built-in Executor Resources**:
    - Includes `executor memory`, `executor cores`, and `executor overhead memory`.
    - When scheduling executors, Spark considers only `executor memory` and `executor cores` from the ResourceProfile.
    - **OffHeap memory** and other built-in resources like `memoryOverhead` do not affect scheduling decisions.

- **Custom Resources**:
    - These are user-defined resource requirements (e.g., GPU, FPGA). These are considered in addition to built-in executor resources.

---

### 5. **Default ResourceProfile Behavior**
- The **base default ResourceProfile** is created when you submit the application using Spark configurations (e.g., `spark.executor.memory`, `spark.executor.cores`).
- For **custom ResourceProfiles**:
    - Executor memory and executor cores from the default profile can be inherited.
    - Other custom resources defined in the default profile (e.g., GPUs) **cannot be propagated** to custom profiles.

---

### Key Points to Note
- If dynamic allocation is **disabled**, stage-level scheduling works within the boundaries of the fixed executors allocated at startup.
- If dynamic allocation is **enabled**, the Spark Master schedules based on ResourceProfile IDs and considers only specific built-in resources (`executor memory` and `executor cores`) for scheduling.
- ResourceProfiles allow fine-grained control but require careful consideration, especially when using custom resources or job server scenarios.

# Description About Resource Profile
A **ResourceProfile** in Spark defines the **resource requirements** for executing tasks within an application. It allows you to specify resources like memory, CPU cores, or custom hardware (e.g., GPUs) at a more granular level—either at the application level or even at the stage level within an application.

### What Problem Does ResourceProfile Solve?
Before ResourceProfiles, Spark allowed resource specification only for the entire application. For example, you could allocate a fixed amount of memory and CPU for executors when submitting an application. However:
- Different stages in a Spark job often have varying resource needs.
    - Example: A **shuffle-heavy stage** might need more memory, while a **CPU-intensive stage** might require more cores.
- ResourceProfiles let you define **different resource needs per stage**, optimizing resource utilization.

---

### Key Components of ResourceProfile
1. **Executor Resources**: These are the resources assigned to executors.
    - **Built-in Resources**:
        - **`executor memory`**: Memory available to each executor.
        - **`executor cores`**: Number of CPU cores for each executor.
        - **`memory overhead`**: Additional memory for off-heap storage and Spark internals.
    - **Custom Resources**:
        - You can define custom resource requirements (e.g., GPUs, specialized hardware) with specific configurations.

2. **Task Resources**: Resources assigned to each task running on an executor.
    - For example, each task may require a certain amount of CPU and memory.

---

### How ResourceProfiles Work
1. **Default ResourceProfile**:
    - Spark creates a default ResourceProfile using the `spark.executor.memory`, `spark.executor.cores`, and similar properties defined at application submission.
    - This profile applies to all stages of your application unless you explicitly define custom ResourceProfiles.

2. **Custom ResourceProfiles**:
    - You can define additional ResourceProfiles for specific stages of your job.
    - For example:
      ```scala
      import org.apache.spark.resource.ResourceProfileBuilder
 
      val profile = new ResourceProfileBuilder()
        .requireExecutorCores(4)
        .requireExecutorMemory("8g")
        .build()
      ```
    - Spark will allocate resources based on this profile for stages that use it.

---

### Use Case Example: Stage Level Scheduling with ResourceProfiles

#### Scenario
Imagine you’re running a Spark application with two stages:
1. Stage A performs a **shuffle-heavy aggregation** and needs more memory.
2. Stage B processes **simple transformations** and requires more CPU cores but less memory.

#### Solution: Using ResourceProfiles
- **Default ResourceProfile**: 2 CPU cores, 4GB executor memory.
- **Custom ResourceProfile for Stage A**:
    - 1 CPU core, 8GB executor memory.
- **Custom ResourceProfile for Stage B**:
    - 4 CPU cores, 2GB executor memory.

You can apply the ResourceProfiles to the stages as follows:
```scala
val stageA = rddA.map(...).withResources(profileA)
val stageB = rddB.map(...).withResources(profileB)
```

---

### Scheduling with ResourceProfiles
When **dynamic allocation** is enabled:
- Executors are added or removed dynamically to meet the ResourceProfile requirements.
- Spark Master prioritizes ResourceProfiles with lower IDs when scheduling.

When **dynamic allocation** is disabled:
- Executors are fixed, and ResourceProfiles adjust only the task-level resource usage. (described more below:)

---

### Benefits of ResourceProfiles
1. **Stage-level Optimization**: Assign the right resources for each stage.
2. **Improved Resource Utilization**: Avoid over-provisioning resources for simple stages.
3. **Custom Resource Support**: Enable the use of specialized hardware like GPUs for specific workloads.

---

When **dynamic allocation** is disabled in Spark, the number of executors allocated to the application is fixed and cannot change during the application's execution. This impacts how **ResourceProfiles** function:

### Executors Are Fixed
1. **What does "fixed executors" mean?**
    - The number of executors assigned to the application is specified during the job submission and remains constant for the application's entire lifetime.
    - For example, if you request `10 executors` when submitting the application, Spark will not request or release additional executors during runtime, regardless of workload changes.

2. **How does this relate to ResourceProfiles?**
    - Since the number of executors is fixed, Spark cannot dynamically allocate more executors to satisfy the resource requirements of different stages with custom ResourceProfiles.
    - Instead, all stages must share the same pool of executors.

---

### ResourceProfiles Adjust Only Task-Level Resource Usage
When executors are fixed, **ResourceProfiles** control the allocation of resources **at the task level** instead of modifying executor-level resources dynamically.

1. **Task-Level Resource Allocation**
    - Each task within an executor is assigned a specific amount of resources based on the active ResourceProfile.
    - For example:
        - Stage A might require each task to use **2 cores** and **4GB memory**.
        - Stage B might require each task to use **1 core** and **2GB memory**.
    - Spark schedules tasks within the fixed set of executors, ensuring that tasks adhere to the specified resource requirements.

2. **No Changes to Executor Configuration**
    - The memory, cores, and custom resources allocated to the executors (e.g., `executor-memory` or `executor-cores`) remain unchanged during the application.
    - If a custom ResourceProfile requires more resources than are available in the fixed executors, tasks may fail or run inefficiently.

---

### Example Scenario
#### Fixed Executors Setup
- You submit a Spark application with:
    - `--num-executors 5`
    - Each executor has `4 cores` and `8GB memory`.

#### ResourceProfile Configuration
- **Default ResourceProfile**: 1 core, 2GB memory per task.
- **Custom ResourceProfile for Stage A**: 2 cores, 4GB memory per task.
- **Custom ResourceProfile for Stage B**: 1 core, 1GB memory per task.

#### Task Execution
- Spark schedules tasks for Stage A and Stage B within the 5 fixed executors:
    - Stage A tasks will consume **2 cores and 4GB memory** each.
    - Stage B tasks will consume **1 core and 1GB memory** each.
- If Stage A requires more resources than the fixed executors can provide (e.g., too many tasks demanding 2 cores each), Spark will limit the number of parallel tasks based on available resources.

---

### Limitations When Executors Are Fixed
1. **Resource Contention**:
    - Stages with larger resource requirements (e.g., many tasks needing 2 cores) might experience delays since tasks have to wait for resources to free up.

2. **No Dynamic Scaling**:
    - Spark cannot allocate additional executors to handle stages with custom ResourceProfiles that need more resources than the fixed pool of executors can provide.

3. **Manual Tuning Required**:
    - You must carefully configure executor memory and cores during job submission to ensure they accommodate the most resource-intensive ResourceProfile.

---

### Why Use ResourceProfiles Without Dynamic Allocation?
Despite these limitations, ResourceProfiles can still provide:
- **Efficient Task Scheduling**: Ensures that stages with different resource needs use available resources optimally within the fixed pool.
- **Custom Resource Utilization**: Enables stages to use specialized resources (e.g., GPUs) within the fixed executor setup.

Would you like a code example showing task-level resource adjustments with fixed executors?


