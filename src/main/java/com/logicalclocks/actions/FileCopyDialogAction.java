package com.logicalclocks.actions;

import com.intellij.ide.util.TreeFileChooserFactory;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.project.impl.ProjectImpl;
import com.logicalclocks.cli.action.FileUploadAction;
import com.logicalclocks.cli.config.HopsworksAPIConfig;
import com.logicalclocks.cli.main.CommandLineMain;
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
        // Using the event, evaluate the context, and enable or disable the action.
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Using the event, implement an action. For example, create and show a dialog.

        String hopsworksApiKey = "30R1l8EcIGNwmv24.k4kauIlhvh54uHdUcr5QLoPXN357YUY5TZMzV4F9oaHiy9YktE7dmRTBVfeLVyCw";
        String hopsworksUrl = "https://56036df0-9aa8-11ea-9678-abe5a0842abf.aws.hopsworks.ai";
        String projectName = "demo_featurestore_jim00000";
        String localFilePath = "file:///C:/pom.xml";
        String destDatasetPath = "/Resources";

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

//        ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu();
//        actionPopupMenu.setTargetComponent();

//        FileChooserDescriptor fcd = new FileChooserDescriptor(true, false, true,
//                true, false, false);
//         FileChooser.chooseFile(fcd, ProjectImpl.)
//        final Project project = ProjectUtil.guessCurrentProject();
//        TreeFileChooserFactory.getInstance()


        try {
            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
            FileUploadAction action = new FileUploadAction(hopsworksAPIConfig,destDatasetPath,  localFilePath);
            action.execute();
        } catch (IOException ex) {
            Logger.getLogger(CommandLineMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CommandLineMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CommandLineMain.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}