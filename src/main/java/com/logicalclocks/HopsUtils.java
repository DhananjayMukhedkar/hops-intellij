package com.logicalclocks;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import net.minidev.json.JSONObject;
import org.apache.commons.io.FilenameUtils;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.swing.*;
import java.util.*;

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
    public static final String PATH_DRVERMEM = "hops.driverMem";
    public static final String PATH_EXECMEM = "hops.executorMem";
    public static final String PATH_EXEC_VC = "hops.executorVC";
    public static final String PATH_DRIVER_VC = "hops.driverVC";
    public static final String PATH_NUM_EXEC = "hops.numberExec";
    public static final String PATH_MEMORY = "hops.memory";
    public static final String PATH_CPU_CORE = "hops.cpuCore";

    public static final String SPARK="SPARK";
    public static final String PYSPARK="PYSPARK";
    public static final String FLINK="FLINK";
    public static final String PYTHON="PYTHON";
    public static final String PYTHON_MAIN="org.apache.spark.deploy.PythonRunner";
    public static final String PYTHON_CONFIG="pythonJobConfiguration";
    public static final String SPARK_CONFIG="sparkJobConfiguration";

    public static final String ARCHIVES_LBL = "Archives :";
    public static final String JARS_LBL = "Jars :";
    public static final String FILES_LBL = "Files :";
    public static final String PYTHON_LBL = "Python :";
    public static final String MORE_PROP_LBL = "More Properties :";
    public static final String HDFS_LBL = "HDFS destination path: ";
    public static final String USER_ARGS_LBL = "User Arguments: " ;
    public static final String MAIN_CLASS_LBL ="Main Class: ";
    public static final String DRIVER_MEM_LBL = "Driver memory (MB): ";
    public static final String EXECUTOR_MEM_LBL = "Executor memory (MB):";
    public static final String EXEC_VC_LBL = "Executor virtual cores:";
    public static final String DRIVER_VC_LBL = "Driver virtual cores: ";
    public static final String NUM_EXEC_LBL = "Number of executors: ";
    public static final String MEMORY_LBL = "Memory (MB):";
    public static final String CPU_LBL = "CPU cores:";
    public static final String JOB_MANAGER_MM_LBL = "JobManager memory (MB)";
    public static final String JNUM_TASK_LBL = "JNumber of TaskManagers";
    public static final String TASK_MANAGER_MM_LBL = "TaskManager memory (MB)";
    public static final String NUM_SLOT_LBL = "Number of slots";






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


    public static JPanel createInputPanel(JPanel panel, LinkedHashMap<String, JTextField> map) {

        Set<Map.Entry<String, JTextField>> e2 = map.entrySet();
        Iterator<Map.Entry<String, JTextField>> it2 = e2.iterator();
        int j=0;
        while(it2.hasNext()){
            Map.Entry<String, JTextField> pair = it2.next();
            panel=createField(panel,j,pair.getKey(),pair.getValue());
            j++;
        }

        JPanel spacer = new JPanel();
        GridConstraints spacerConstraints = new GridConstraints();
        spacerConstraints.setRow(j);
        spacerConstraints.setFill(GridConstraints.FILL_BOTH);
        //panel.add(spacer, spacerConstraints);

        return panel;


    }

    public static JPanel createField(JPanel container,int row,String label,JTextField field ){

        if (container!=null){
            GridConstraints pathLabelConstraint = new GridConstraints();
            pathLabelConstraint.setRow(row);
            pathLabelConstraint.setColumn(0);
            pathLabelConstraint.setFill(GridConstraints.FILL_HORIZONTAL);
            pathLabelConstraint.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
            container.add(new JLabel(label), pathLabelConstraint);

            GridConstraints pathFieldConstraint = new GridConstraints();
            pathFieldConstraint.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
            pathFieldConstraint.setFill(GridConstraints.FILL_HORIZONTAL);
            pathFieldConstraint.setAnchor(GridConstraints.ANCHOR_WEST);
            pathFieldConstraint.setRow(row);
            pathFieldConstraint.setColumn(1);
            pathFieldConstraint.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
            container.add(field, pathFieldConstraint);
        }
        return container;

    }





}
