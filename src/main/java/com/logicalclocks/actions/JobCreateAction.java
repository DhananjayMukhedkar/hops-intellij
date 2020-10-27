package com.logicalclocks.actions;

import com.logicalclocks.HopsUtils;
import io.hops.cli.config.HopsworksAPIConfig;
import net.minidev.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.*;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.IOException;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class JobCreateAction extends JobAction {


  @XmlRootElement
  public static class Args {
    private int numExecutors=1;
    private int cpusPerExecutor=1;
    private int cpusPerDriver=1;
    private int driverMemInMbs = 2048;
    private int executorMemInMbs = 2048;
    private int gpusPerExecutor = 0;
    private String commandArgs = "";
    private String jvmArgs = "";
    private String[] files = {};
    private String[] jars = {};
    private String[] archives = {};
    private String[] sparkConf = {};
    //add
    private String appPath="";
    private String mainClass="";
    private String configType="";
    private String jobType="";
    //private final String SPARK_CONFIG="sparkJobConfiguration";
    //private final String SPARK="SPARK";

    public Args() {}

    public int getNumExecutors() {
      return numExecutors;
    }

    public void setNumExecutors(int numExecutors) {
      this.numExecutors = numExecutors;
    }

    public int getCpusPerExecutor() {
      return cpusPerExecutor;
    }

    public void setCpusPerExecutor(int cpusPerExecutor) {
      this.cpusPerExecutor = cpusPerExecutor;
    }

    public int getCpusPerDriver() {
      return cpusPerDriver;
    }

    public void setCpusPerDriver(int cpusPerDriver) {
      this.cpusPerDriver = cpusPerDriver;
    }

    public int getDriverMemInMbs() {
      return driverMemInMbs;
    }

    public void setDriverMemInMbs(int driverMemInMbs) {
      this.driverMemInMbs = driverMemInMbs;
    }

    public int getExecutorMemInMbs() {
      return executorMemInMbs;
    }

    public void setExecutorMemInMbs(int executorMemInMbs) {
      this.executorMemInMbs = executorMemInMbs;
    }

    public int getGpusPerExecutor() {
      return gpusPerExecutor;
    }

    public void setGpusPerExecutor(int gpusPerExecutor) {
      this.gpusPerExecutor = gpusPerExecutor;
    }

    public String getCommandArgs() {
      return commandArgs;
    }

    public void setCommandArgs(String commandArgs) {
      this.commandArgs = commandArgs;
    }

    public String getJvmArgs() {
      return jvmArgs;
    }

    public void setJvmArgs(String jvmArgs) {
      this.jvmArgs = jvmArgs;
    }

    public String[] getFiles() {
      return files;
    }

    public void setFiles(String[] files) {
      this.files = files;
    }

    public String[] getJars() {
      return jars;
    }

    public void setJars(String[] jars) {
      this.jars = jars;
    }

    public String[] getArchives() {
      return archives;
    }

    public void setArchives(String[] archives) {
      this.archives = archives;
    }

    public String[] getSparkConf() {
      return sparkConf;
    }

    public void setSparkConf(String[] sparkConf) {
      this.sparkConf = sparkConf;
    }

    //add
    public String getAppPath() {
      return appPath;
    }

    public void setAppPath(String s) {
      this.appPath = s;
    }

    public String getMainClass() {
      return mainClass;
    }

    public void setMainClass(String s) {
      this.mainClass = s;
    }

    public String getConfigType() {
      return configType;
    }

    public void setConfigType(String s) {
      this.configType = s;
    }

    public String getJobType() {
      return jobType;
    }

    public void setJobType(String s) {
      this.jobType = s;
    }
  }

  
  private static final Logger logger = LoggerFactory.getLogger(JobCreateAction.class);
  
  private final JsonObject payload;

  //test
  private final HopsworksAPIConfig hopsworksAPIConfig;
  private final String jobName;

  public JobCreateAction(HopsworksAPIConfig hopsworksAPIConfig, String jobName) throws IOException {
    this(hopsworksAPIConfig, jobName, new Args());
  }

  public JobCreateAction(HopsworksAPIConfig hopsworksAPIConfig, String jobName, Args args) throws IOException {
    super(hopsworksAPIConfig, jobName);

    this.hopsworksAPIConfig=hopsworksAPIConfig;
    this.jobName=jobName;
    String path=args.getAppPath().split("hdfs://")[1];

    if(args.getJobType()==HopsUtils.SPARK )// get job config from inspect API if SPARK
      payload = getJobConfig(args.getJobType(),path,args.getMainClass(),args.getAppPath());
    else if (args.getJobType()==HopsUtils.PYTHON)
      payload = getPythonJobConfig(args);
    else payload=getFlinkJobConfig();



  }

/*  public JsonObjectBuilder createConfigObj(JsonObjectBuilder objectBuilder, Args args, String jobName){

      objectBuilder.add("type", args.getConfigType())
              .add("appName", jobName)
              .add("amQueue", "default")
              .add("amMemory", args.getDriverMemInMbs())
              .add("amVCores", 1)
              .add("jobType", args.getJobType())
              .add("appPath", args.getAppPath())
              .add("mainClass", args.getMainClass())
              .add("spark.executor.instances", 1)
              .add("spark.executor.cores", 1)
              .add("spark.executor.memory", args.getExecutorMemInMbs())
              .add("spark.executor.gpus", 0)
              .add("spark.dynamicAllocation.enabled", false)
              .add("spark.dynamicAllocation.minExecutors", 1)
              .add("spark.dynamicAllocation.maxExecutors", 10)
              .add("spark.dynamicAllocation.initialExecutors", 1);

    return objectBuilder;


  }*/

  private JsonObject getJobConfig(String jobType,String programPath,String mainClass,String appPath) throws IOException {
    //inspect job config
    JsonObject respConfig = inspectJobConfig(jobType.toLowerCase(), programPath);

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    for(String key : respConfig.keySet()) {
      if(key.equalsIgnoreCase("appPath")){ // add full app path
        objectBuilder.add(key, appPath);
      }else
        objectBuilder.add(key, respConfig.get(key));
    }
    if(!respConfig.containsKey("mainClass")) { // if no main class add from user input
      objectBuilder.add("mainClass", mainClass);
    }


    return objectBuilder.build();
  }


  public JsonObject getFlinkJobConfig(){

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("type", "flinkJobConfiguration")
            .add("amQueue", "default")
            .add("jobmanager.heap.size",1024)
            .add("amVCores", 1)
            .add("numberOfTaskManagers",1)
            .add("taskmanager.heap.size",1024)
            .add("taskmanager.numberOfTaskSlots",1)
            .add("appName",jobName);
            //.add("localResources","");

    return objectBuilder.build();

  }

  public JsonObject getPythonJobConfig(Args args){

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("type", "pythonJobConfiguration")
            .add("appName",jobName)
            .add("memory",2028)
            .add("jobType","PYTHON")
            .add("appPath",args.getAppPath())
            .add("defaultArgs",args.getCommandArgs());
    //.add("localResources","");b
    return objectBuilder.build();

  }

  
  @Override
  public int execute() throws Exception {

    CloseableHttpClient getClient = getClient();
    //HttpPut request = new HttpPut(getJobUrl() + "/" + getJobName());
    HttpPut request = new HttpPut(getJobUrl());

    request.addHeader("User-Agent", USER_AGENT);
    request.addHeader("Authorization", "ApiKey " + hopsworksAPIConfig.getApiKey());
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-type", "application/json");

    StringEntity entity = new StringEntity(payload.toString());
    //StringEntity entity = new StringEntity(respConfig.toString());
    request.setEntity(entity);

    CloseableHttpResponse response = getClient.execute(request);
    int status = readJsonRepoonse(response);
    response.close();

    return status;
  }


}
