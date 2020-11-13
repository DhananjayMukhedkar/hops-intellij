package com.logicalclocks.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.logicalclocks.HopsPluginUtils;

import io.hops.cli.action.FileUploadAction;

import io.hops.cli.config.HopsworksAPIConfig;
//import io.hops.cli.action.JobCreateAction;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HopsCreateJob extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);

    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO : Check File Separator hardcoding
        HopsPluginUtils util=new HopsPluginUtils();
        Project proj=e.getProject();
        String hopsworksApiKey = util.getAPIKey(proj);
        String hopsworksUrl = util.getURL(proj);
        String projectName = util.getProjectName(proj);
        String jobName=util.getJobName(proj);
        String destination=util.getDestination(proj);
        String mainClass=util.getUserMainClass(proj);
        String userArgs=util.getUserArgs(proj);
        String localFilePath =e.getDataContext().getData("virtualFile").toString();

        File f = new File(localFilePath);
        String finalPath=destination+'/'+f.getName(); // Check File Separator hardcoding

        //String jobType=util.getJobType(f.getName());
        String jobType=util.getUserJobType(e.getProject());
        System.out.println(f.getName());
        System.out.println(finalPath);
        System.out.println(jobType);
        //String configType=util.getJobConfigType(jobType);
        //System.out.println(configType);

        try {
            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
            //upload program
            FileUploadAction action = new FileUploadAction(hopsworksAPIConfig,destination,  localFilePath);
            action.execute();
            //set program configs
            JobCreateAction.Args args = new JobCreateAction.Args();
            //remove as main class is set my job inspect if its manifested
   /*         if(jobType== HopsUtils.SPARK) { // for Spark job type
                args.setMainClass(mainClass);
            }else if (jobType== HopsUtils.PYSPARK){ // for PySpark job type
                args.setMainClass(HopsUtils.PYTHON_MAIN);
            }*/

            args.setMainClass(mainClass); //set user provides,overridden by inspect job config
            args.setAppPath(finalPath); //full app path
            args.setJobType(jobType);  // spark/pyspark/python
            args.setCommandArgs(userArgs.trim());
            if(jobType.equals(HopsPluginUtils.SPARK)){
                args.setDriverMemInMbs(Integer.parseInt(util.getDriverMemory(proj)));
                args.setDriverVC(Integer.parseInt(util.getDriverVC(proj)));
                args.setExecutorMemInMbs(Integer.parseInt(util.getExecutorMemory(proj)));
                args.setExecutorVC(Integer.parseInt(util.getExecutorVC(proj)));
                args.setNumExecutors(Integer.parseInt(util.getNumberExecutor(proj)));
                if(util.isAdvanced(proj)){
                    args.setAdvanceConfig(true);
                    args.setArchives(util.getAdvancedArchive(proj));
                    args.setFiles(util.getAdvancedFiles(proj));
                    args.setPythonDependency(util.getPythonDependency(proj));
                    args.setJars(util.getAdvancedJars(proj));
                    args.setProperties(util.getMoreProperties(proj));
                }
            }else if (jobType.equals(HopsPluginUtils.PYTHON)){
                args.setPythonMemory(Integer.parseInt(util.getPythonMemory(proj)));
                args.setCpusCores(Integer.parseInt(util.getPythonCpuCores(proj)));
                if(util.isAdvanced(proj)){
                    args.setAdvanceConfig(true);
                    args.setFiles(util.getAdvancedFiles(proj));
                }
            }else if (jobType.equals(HopsPluginUtils.FLINK)){
                args.setJobManagerMemory(Integer.parseInt(util.getJobManagerMemory(proj)));
                args.setTaskManagerMemory(Integer.parseInt(util.getTaskManagerMemory(proj)));
                args.setNumTaskManager(Integer.parseInt(util.getNumTaskManager(proj)));
                args.setNumSlots(Integer.parseInt(util.getNumberSlots(proj)));
                if(util.isAdvanced(proj)){
                    args.setAdvanceConfig(true);
                    args.setProperties(util.getMoreProperties(proj));
                }
            }
            //args.setConfigType(configType); // removed as set by inspect job config

            // create job
            JobCreateAction createJob = new JobCreateAction(hopsworksAPIConfig, jobName,args);
            int status =createJob.execute();
            PluginNoticifaction news=new PluginNoticifaction();
            if (status == 200 || status == 201) {
                 news.notify(e.getProject()," Job: "+jobName+" | Created");
            } else news.notify(e.getProject()," Job: "+jobName+" | Creation Failed");
        } catch (IOException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobCreateAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }catch (Exception ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(HopsCreateJob.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }


    }
}
