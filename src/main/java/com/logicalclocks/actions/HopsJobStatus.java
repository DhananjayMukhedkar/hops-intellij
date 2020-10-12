package com.logicalclocks.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.logicalclocks.HopsUtils;
import io.hops.cli.config.HopsworksAPIConfig;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HopsJobStatus extends AnAction {


    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);

    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        HopsUtils util=new HopsUtils();
        Project proj=e.getProject();
        String hopsworksApiKey = util.getAPIKey(proj);
        String hopsworksUrl = util.getURL(proj);
        String projectName = util.getProjectName(proj);
        String jobName=util.getJobName(proj);
        String userExecId=util.getUserExecId(proj);

        try {
            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
            JobStatusAction jobStatus = new JobStatusAction(hopsworksAPIConfig, jobName,userExecId);
            int run=jobStatus.execute();

            if(run==0){
                String[] arr=jobStatus.getJobStatusArr();

                StringBuilder sb=new StringBuilder("Job :").append(jobName).append(" ;Execution Id :").append(jobStatus.getExecutionId()).append(" State: ").append(arr[0]).append( "| Final Status: ").append(arr[1]);
                PluginNoticifaction.notify(e.getProject(),sb.toString());
            }else PluginNoticifaction.notify(e.getProject(),"Failed to get job status");

        } catch (IOException ex) {

            Logger.getLogger(JobStatusAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
        }catch (Exception ex) {

            Logger.getLogger(JobStatusAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
        }
    }
}
