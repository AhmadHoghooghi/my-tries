# Q1:Package

## Solution 1: external App

## Solution 2: How to run the test in verify space

# Q2: Have history server executed

How to append something to entry point?
article: [Choosing Between RUN, CMD, and ENTRYPOINT](https://www.docker.com/blog/docker-best-practices-choosing-between-run-cmd-and-entrypoint/)

* `CMD`: default startup command: overriden by `x` argument in `docker run image x`
* `ENTRYPOINT` default executable for the container. Any arguments supplied to the docker run command are appended to the `ENTRYPOINT` command.
* `CMD`+ `ENTRYPOINT`: The `CMD`instruction can be used to provide default arguments to an`ENTRYPOINT`if it is specified in the exec form. This setup allows the entry point to be the main executable and`CMD` to specify additional arguments that can be overridden by the user.

What is @ in commands


* The syntax of passing additional command with docker
  * See the above article
* The syntax of passing additional command with docker compose
  * [Docker compose attribute: command](https://docs.docker.com/reference/compose-file/services/#command)
  * [Docker compose attribute: entrypoint](https://docs.docker.com/reference/compose-file/services/#entrypoint)
* The syntax of passing additional command with test container
