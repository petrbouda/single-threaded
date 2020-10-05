# Single-Thread App and its Testing 

- Build the base image locally - `https://github.com/petrbouda/openjdk-x-dbg-asyncprofiler`

```
sudo sh -c 'echo 1 >/proc/sys/kernel/perf_event_paranoid'
sudo sh -c 'echo 0 >/proc/sys/kernel/kptr_restrict'
```

```
-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints
```

or if we want to profile a startup of the application.

```
-agentpath:"/async-profiler/build/libasyncProfiler.so=start,event=alloc,file=allocation-flame-graph.svg,svg,title=Allocation profile,width=1600"
```

Build a docker image with async-profiler 
```
docker build -t openjdk-15-asyncprofiler:1.8.1 ./async-profiler-image
```

Copy `libasyncProfiler.so` to be on the same place as in the docker container
```
mkdir /tmp/asyncprofiler && cp ./async-profiler-image/libasyncProfiler.so /tmp/asyncprofiler/
```

