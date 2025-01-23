package com.rhotiz.container.demo;

import org.apache.maven.shared.invoker.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class MavenInvokerTest {

    @Test
    void a() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        String projectRelativePath = "./../spark-app";
        request.setPomFile(new File(projectRelativePath + "/pom.xml"));
        request.setJavaHome(new File(System.getProperty("java.home")));
        request.setBatchMode(true);
        request.setShellEnvironmentInherited(true);
        request.addArgs(List.of("clean", "package"));


        Invoker invoker = new DefaultInvoker();
        invoker.setMavenExecutable(new File(projectRelativePath + "/mvnw"));
        invoker.execute(request);
    }
}
