package com.logicalclocks.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.logicalclocks.HopsUtils;

import io.hops.cli.action.FileUploadAction;

import io.hops.cli.config.HopsworksAPIConfig;
import com.logicalclocks.actions.JobCreateAction ;
import org.apache.commons.io.FilenameUtils;
//import io.hops.cli.action.JobCreateAction;

import java.io.File;
import java.io.IOException;

import java.nio.file.FileSystem;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HopsCreateJob extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);

    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

        HopsUtils util=new HopsUtils();
        Project proj=e.getProject();
        String hopsworksApiKey = util.getAPIKey(proj);
        String hopsworksUrl = util.getURL(proj);
        String projectName = util.getProjectName(proj);
        String file=util.getLocalFile(proj);
        String jobName=util.getJobName(proj);
        String destination=util.getDestination(proj);
        String userArgs=util.getUserArgs(proj);
        String mainClass=util.getUserMainClass(proj);

        String localFilePath =e.getDataContext().getData("virtualFile").toString();

        File f = new File(localFilePath);
        System.out.println(f.getName());
        String finalPath=destination+'/'+f.getName();
        System.out.println(finalPath);
        String jobType=util.getJobType(f.getName());
        System.out.println(jobType);
        String configType=util.getJobConfigType(jobType);
        System.out.println(configType);

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
            //args.setConfigType(configType); // removed as set by inspect job config

            // create job
            JobCreateAction createJob = new JobCreateAction(hopsworksAPIConfig, jobName,args);
            int status =createJob.execute();
            PluginNoticifaction news=new PluginNoticifaction();
            if (status == 200 || status == 201) {
                 news.notify(e.getProject()," Job "+jobName+": Created");
            } else news.notify(e.getProject()," Job "+jobName+": Creation Failed");
        } catch (IOException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobCreateAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }catch (Exception ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(HopsCreateJob.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }


    }
}
