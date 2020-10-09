package com.logicalclocks.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.logicalclocks.HopsUtils;
import io.hops.cli.action.FileUploadAction;
import io.hops.cli.action.JobRunAction;
import io.hops.cli.action.JobStopAction;
import io.hops.cli.config.HopsworksAPIConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HopsStopJob extends AnAction {

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

        String localFilePath =e.getDataContext().getData("virtualFile").toString();

        try {
            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);

            JobStopAction stopJob=new JobStopAction(hopsworksAPIConfig,jobName);
            int status=stopJob.execute();

            if (status == 200 || status == 201) {
                PluginNoticifaction.notify(e.getProject(),"Job "+jobName +": Stopped");
            }
            else PluginNoticifaction.notify(e.getProject(),"Job "+jobName +": Stop failed");
        } catch (IOException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobStopAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (URISyntaxException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobStopAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(HopsStopJob.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }
}
