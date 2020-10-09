package com.logicalclocks.actions;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.MessageView;
import com.logicalclocks.HopsUtils;
import io.hops.cli.action.JobRemoveAction;
import io.hops.cli.config.HopsworksAPIConfig;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

public class HopsGetJobStatus extends AnAction {


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

        try {
            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
            JobStatus jobStatus = new JobStatus(hopsworksAPIConfig, jobName);

            int run=jobStatus.execute();
            PluginNoticifaction news=new PluginNoticifaction();
            if(run==0){
                String[] arr=jobStatus.getJobStatusArr();
                StringBuilder sb=new StringBuilder("Job State: ").append(arr[0]).append( "| Final Status: ").append(arr[1]);
                news.notify(e.getProject(),sb.toString());
            }else news.notify(e.getProject(),"Failed to get job status");

        } catch (IOException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobStatus.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }catch (Exception ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobStatus.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
