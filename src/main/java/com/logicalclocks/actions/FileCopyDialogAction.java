package com.logicalclocks.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.logicalclocks.HopsPluginUtils;

import io.hops.cli.action.FileUploadAction;
import io.hops.cli.config.HopsworksAPIConfig;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileCopyDialogAction extends AnAction {



    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Using the event, implement an action. For example, create and show a dialog.

        HopsPluginUtils util=new HopsPluginUtils();
        Project proj=e.getProject();

        String hopsworksApiKey = util.getAPIKey(proj);
        String hopsworksUrl = util.getURL(proj);
        String projectName = util.getProjectName(proj);


        String localFilePath = "file:///C:/pom.xml";
        String destDatasetPath = "/Resources";

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

/*        ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu();
        actionPopupMenu.setTargetComponent();

        FileChooserDescriptor fcd = new FileChooserDescriptor(true, false, true,
                true, false, false);
         FileChooser.chooseFile(fcd, ProjectImpl.)
        final Project project = ProjectUtil.guessCurrentProject();
        TreeFileChooserFactory.getInstance();*/


        try {

            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
            FileUploadAction action = new FileUploadAction(hopsworksAPIConfig,destDatasetPath,  localFilePath);
            action.execute();
        } catch (IOException ex) {
            Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}