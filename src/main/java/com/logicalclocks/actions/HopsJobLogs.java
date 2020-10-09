package com.logicalclocks.actions;

//import io.hops.cli.action.JobLogsAction;
import com.logicalclocks.actions.JobLogsAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.logicalclocks.HopsUtils;

import io.hops.cli.action.JobStopAction;
import io.hops.cli.config.HopsworksAPIConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HopsJobLogs extends AnAction {

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
        String localPathLogs=util.getLocalFile(proj);
        String jobName=util.getJobName(proj);
        String destination=util.getDestination(proj);
        String userArgs=util.getUserArgs(proj);

        String localFilePath =e.getDataContext().getData("virtualFile").toString();

        try {
            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);

            JobLogsAction logsJob = new JobLogsAction(hopsworksAPIConfig, jobName);
            int status=logsJob.execute();
            //PluginNoticifaction news=new PluginNoticifaction();
            if (status == 200 || status == 201) {
                String stdOut = logsJob.getStdOut();
                String pathStdOut = logsJob.getPathToStdOut();
                String stdErr = logsJob.getStdErr();
                String pathStdErr=logsJob.getPathToStdErr();
                int execId = logsJob.getExecutionId();
                String localLogPath=localPathLogs;//"C:"+ File.separator+"hopsworks_projects";
                StringBuilder sb=new StringBuilder(jobName).append("_")
                        .append(String.valueOf(execId)).append("_stdOut.log");
                StringBuilder sb2=new StringBuilder(jobName).append("_")
                        .append(String.valueOf(execId)).append("_stdErr.log");

                writeFile(localLogPath,sb.toString(),stdOut);
                writeFile(localLogPath,sb2.toString(),stdErr);

                PluginNoticifaction.notify(e.getProject()," Job "+jobName+": Logs downloaded");
            } else PluginNoticifaction.notify(e.getProject()," Job "+jobName+": Get Logs Failed");
        } catch (IOException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobLogsAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (URISyntaxException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobLogsAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(HopsJobLogs.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public void writeFile (String logPath,String filename,String content) throws IOException{
        File out=new File(logPath,filename);
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(out));
        bw.write(content);
        bw.close();
    }

}
