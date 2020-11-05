package com.logicalclocks.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.logicalclocks.HopsPluginUtils;
import io.hops.cli.action.JobRemoveAction;
import io.hops.cli.config.HopsworksAPIConfig;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HopsRemoveJob extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);

    }


    @Override
    public void actionPerformed(AnActionEvent e) {

            HopsPluginUtils util=new HopsPluginUtils();
            Project proj=e.getProject();
            String hopsworksApiKey = util.getAPIKey(proj);
            String hopsworksUrl = util.getURL(proj);
            String projectName = util.getProjectName(proj);
            String jobName=util.getJobName(proj);

            try {
                HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
                JobRemoveAction rmJob = new JobRemoveAction(hopsworksAPIConfig, jobName);
                int status=rmJob.execute();
                PluginNoticifaction news=new PluginNoticifaction();
                news.notify(e.getProject(),"Job :"+jobName+" | Deleted");


              /*  if (status == 200 || status == 201 || status == 202) {
                   news.notify(e.getProject(),"Job :"+jobName+" | Deleted");
                } else news.notify(e.getProject(),"Job :"+jobName+" | Remove failed");*/

            } catch (IOException ex) {
                PluginNoticifaction.notify(e.getProject(),ex.getMessage());
                Logger.getLogger(JobRemoveAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }catch (Exception ex) {
                PluginNoticifaction.notify(e.getProject(),ex.getMessage());
                Logger.getLogger(HopsRemoveJob.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }

    }
}
