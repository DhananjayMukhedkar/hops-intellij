package com.logicalclocks;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import net.minidev.json.JSONObject;
import org.apache.commons.io.FilenameUtils;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;

public class HopsUtils {

    public static final String PATH_URL = "hops.url";
    public static final String PATH_KEY = "hops.key";
    public static final String PATH_PROJECT = "hops.project";
    public static final String PATH_FILE = "hops.file";
    public static final String PATH_JOB = "hops.job";
    public static final String PATH_PROGRAM = "hops.program";
    public static final String PATH_USERARGS = "hops.userAgrs";
    public static final String PATH_MAINCLASS = "hops.mainClass";
    public static final String PATH_EXECID = "hops.executionId";
    public static final String PATH_JOBTYPE="hops.jobType";
    public static final String PATH_ADDFILE = "hops.additionalFiles";
    public static final String PATH_PYTHON_DEPEND = "hops.pythonDependency";
    public static final String PATH_ARCHIVE = "hops.archive";
    public static final String PATH_ADDJAR = "hops.addJar";
    public static final String PATH_MORE_PROP = "hops.moreProp";

    public static final String SPARK="SPARK";
    public static final String PYSPARK="PYSPARK";
    public static final String FLINK="FLINK";
    public static final String PYTHON="PYTHON";
    public static final String PYTHON_MAIN="org.apache.spark.deploy.PythonRunner";
    public static final String PYTHON_CONFIG="pythonJobConfiguration";
    public static final String SPARK_CONFIG="sparkJobConfiguration";




    private final HashMap<String,Integer> jobTypeCode=new HashMap<String,Integer>();

    public HopsUtils(){
        // Setting JOB TYPE //TO DO configurable job type
        jobTypeCode.put("jar",1);
        jobTypeCode.put("py",2);
        jobTypeCode.put("ipynb",2);
    }

    public String getURL(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_URL);

    }

    public String getAPIKey(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_KEY);

    }

    public String getProjectName(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_PROJECT);

    }

    public String getLocalFile(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_FILE);

    }
    public String getJobName(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_JOB);

    }
    public String getDestination(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_PROGRAM);

    }

    public String getUserArgs(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_USERARGS);

    }

    public String getUserMainClass(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_MAINCLASS);

    }


    public String getUserExecId(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_EXECID);

    }

    public String getUserJobType(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        return properties.getValue(PATH_JOBTYPE);

    }

    public String getJobType(String fileName){

        String ext= FilenameUtils.getExtension(fileName);
        Integer c=jobTypeCode.get(ext);

            switch (c.toString()) {
                case "1":
                    return SPARK;
                case "2":
                    return PYSPARK;
                case "3":
                    return FLINK;
                case "4":
                    return PYTHON;
                default:
                    return null;
            }
    }

    public String getJobConfigType(String jobType){

        switch(jobType){
            case SPARK:
                return SPARK_CONFIG;
            case PYSPARK:
                return SPARK_CONFIG;
            case PYTHON:
                return PYTHON_CONFIG;
            default:
                return SPARK_CONFIG;
        }
    }





}
