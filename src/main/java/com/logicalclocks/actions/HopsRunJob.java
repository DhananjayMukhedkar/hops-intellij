package com.logicalclocks.actions;

//import io.hops.cli.action.JobRunAction;
import com.logicalclocks.actions.JobRunAction;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.newvfs.impl.VirtualFileImpl;
import com.logicalclocks.HopsUtils;
import io.hops.cli.action.FileUploadAction;
import io.hops.cli.config.HopsworksAPIConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HopsRunJob extends AnAction {


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

        //String localFilePath = "file://"+file;
        String localFilePath =e.getDataContext().getData("virtualFile").toString();
/*
         Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu();
        actionPopupMenu.setTargetComponent();

        FileChooserDescriptor fcd = new FileChooserDescriptor(true, false, true,
                true, false, false);
         FileChooser.chooseFile(fcd, ProjectImpl.)
        final Project project = ProjectUtil.guessCurrentProject();
        TreeFileChooserFactory.getInstance();*/

        try {
            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
            //upload app first
            FileUploadAction action = new FileUploadAction(hopsworksAPIConfig,destination,  localFilePath);
            action.execute();
            //execute run job

            PluginNoticifaction.notify(e.getProject()," Job "+jobName+": Attempting start");
            JobRunAction runJob=new JobRunAction(hopsworksAPIConfig,jobName,userArgs);
            int status=runJob.execute();

            if (status == 200 || status == 201) {
                StringBuilder sb=new StringBuilder(" Job Started: ").append(jobName)
                        .append(" | Execution Id:").append(runJob.getExecId());
                PluginNoticifaction.notify(e.getProject(),sb.toString());
            }
            else PluginNoticifaction.notify(e.getProject()," Job "+jobName+": Start Failed");
        } catch (IOException ex) {
            PluginNoticifaction.notify(e.getProject(),ex.getMessage());
            Logger.getLogger(JobRunAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (URISyntaxException ex) {

            Logger.getLogger(JobRunAction.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception ex) {

            Logger.getLogger(HopsRunJob.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }
}
