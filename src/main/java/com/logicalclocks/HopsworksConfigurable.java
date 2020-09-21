package com.logicalclocks;

import com.intellij.ide.actions.ShowSettingsUtilImpl;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.logicalclocks.actions.FileCopyDialogAction;
import io.hops.cli.action.FileUploadAction;
import io.hops.cli.action.JobRunAction;
import io.hops.cli.config.HopsworksAPIConfig;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HopsworksConfigurable implements Configurable {

    private final Project project;
    private  JTextField userKey = new JTextField();
    private JTextField userUrl = new JTextField();
    private JTextField userProject = new JTextField();
    private JTextField filePath = new JTextField();
    private JTextField jobName = new JTextField();
    private JTextField programPath = new JTextField();


    private static final String PATH_URL = "hops.url";
    private static final String PATH_KEY = "hops.key";
    private static final String PATH_PROJECT = "hops.project";
    private static final String PATH_FILE = "hops.file";
    private static final String PATH_JOB = "hops.job";
    private static final String PATH_PROGRAM = "hops.program";


    public static String storedUrl = null;
    public static String storedKey = null;
    public static String storedProject = null;
    public static String storedFile = null;
    public static String storedJob = null;
    public static String storedProgram = null;

    public HopsworksConfigurable(Project project){

        this.project = project;
        if(project!=null){
            loadProperties(project);
        }

    }

    private static void loadProperties(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        storedUrl = properties.getValue(PATH_URL);
        storedKey = properties.getValue(PATH_KEY);
        storedProject = properties.getValue(PATH_PROJECT);
        storedFile = properties.getValue(PATH_FILE);
        storedJob = properties.getValue(PATH_JOB);
        storedProgram = properties.getValue(PATH_PROGRAM);

    }
    
    
    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "HOPS SETTINGS";
    }



    @Override
    public @Nullable JComponent createComponent() {
         userKey = new JTextField();
         userUrl = new JTextField();
         userProject = new JTextField();
         filePath=new JTextField();
         jobName=new JTextField();
         programPath=new JTextField();

        JPanel container = new JPanel(new GridLayoutManager(7, 2));

        //1
        GridConstraints pathLabelConstraint0a = new GridConstraints();
        pathLabelConstraint0a.setRow(0);
        pathLabelConstraint0a.setColumn(0);
        pathLabelConstraint0a.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0a.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("Hopsworks Project: "), pathLabelConstraint0a);

        GridConstraints pathFieldConstraint0a = new GridConstraints();
        pathFieldConstraint0a.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0a.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0a.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0a.setRow(0);
        pathFieldConstraint0a.setColumn(1);
        pathFieldConstraint0a.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(userProject, pathFieldConstraint0a);
        //2
        GridConstraints pathLabelConstraint0b = new GridConstraints();
        pathLabelConstraint0b.setRow(1);
        pathLabelConstraint0b.setColumn(0);
        pathLabelConstraint0b.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0b.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("Hopsworks URL: "), pathLabelConstraint0b);

        GridConstraints pathFieldConstraint0b = new GridConstraints();
        pathFieldConstraint0b.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0b.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0b.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0b.setRow(1);
        pathFieldConstraint0b.setColumn(1);
        pathFieldConstraint0b.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(userUrl, pathFieldConstraint0b);

        //3
        GridConstraints pathLabelConstraint0c = new GridConstraints();
        pathLabelConstraint0c.setRow(2);
        pathLabelConstraint0c.setColumn(0);
        pathLabelConstraint0c.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0c.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("Hopsworks API Key: "), pathLabelConstraint0c);

        GridConstraints pathFieldConstraint0c = new GridConstraints();
        pathFieldConstraint0c.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0c.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0c.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0c.setRow(2);
        pathFieldConstraint0c.setColumn(1);
        pathFieldConstraint0c.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(userKey, pathFieldConstraint0c);
        //4
        GridConstraints pathLabelConstraint0d = new GridConstraints();
        pathLabelConstraint0d.setRow(3);
        pathLabelConstraint0d.setColumn(0);
        pathLabelConstraint0d.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0d.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("Local file path: "), pathLabelConstraint0d);

        GridConstraints pathFieldConstraint0d = new GridConstraints();
        pathFieldConstraint0d.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0d.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0d.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0d.setRow(3);
        pathFieldConstraint0d.setColumn(1);
        pathFieldConstraint0d.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(filePath, pathFieldConstraint0d);
        //5
        GridConstraints pathLabelConstraint0e = new GridConstraints();
        pathLabelConstraint0e.setRow(4);
        pathLabelConstraint0e.setColumn(0);
        pathLabelConstraint0e.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0e.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("Hopsworks Job Name: "), pathLabelConstraint0e);

        GridConstraints pathFieldConstraint0e = new GridConstraints();
        pathFieldConstraint0e.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0e.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0e.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0e.setRow(4);
        pathFieldConstraint0e.setColumn(1);
        pathFieldConstraint0e.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(jobName, pathFieldConstraint0e);
        //6
        GridConstraints pathLabelConstraint0f = new GridConstraints();
        pathLabelConstraint0f.setRow(5);
        pathLabelConstraint0f.setColumn(0);
        pathLabelConstraint0f.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0f.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("HDFS destination path: "), pathLabelConstraint0f);

        GridConstraints pathFieldConstraint0f = new GridConstraints();
        pathFieldConstraint0f.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0f.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0f.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0f.setRow(5);
        pathFieldConstraint0f.setColumn(1);
        pathFieldConstraint0f.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(programPath, pathFieldConstraint0f);

        JPanel spacer = new JPanel();
        GridConstraints spacerConstraints = new GridConstraints();
        spacerConstraints.setRow(6);
        spacerConstraints.setFill(GridConstraints.FILL_BOTH);
        container.add(spacer, spacerConstraints);
        return container;

    }

    @Override
    public boolean isModified() {
        if (userKey!=null || userProject !=null || userUrl!=null) {
            return true;
        }
        // check for all other variables

        // check last value != current return true

        return false;
    }

    @Override
    public void apply() throws ConfigurationException {


        storedUrl = userUrl.getText().trim();
        storedKey = userKey.getText().trim();
        storedProject= userProject.getText().trim();
        storedFile=filePath.getText().trim();
        storedJob=jobName.getText().trim();
        storedProgram=programPath.getText().trim();

        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        properties.setValue(PATH_URL, storedUrl);
        properties.setValue(PATH_KEY, storedKey);
        properties.setValue(PATH_PROJECT, storedProject);
        properties.setValue(PATH_FILE, storedFile);
        properties.setValue(PATH_JOB, storedJob);
        properties.setValue(PATH_PROGRAM, storedProgram);

        //RUN
/*        if (storedKey!=null && storedKey!=null && storedProject!=null) {
            try {
                String localFilePath = "file:///"+storedFile;

                HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig(storedKey, storedUrl, storedProject);
                FileUploadAction action = new FileUploadAction(hopsworksAPIConfig, storedProgram, localFilePath);
                action.execute();
                JobRunAction runJob=new JobRunAction(hopsworksAPIConfig,storedJob,"");
                runJob.execute();
            } catch (IOException ex) {
                Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/


    }

    @Override
    public void reset() {
        if (userKey != null) {
            userKey.setText(storedKey);
        }
        if (userUrl != null) {
            userUrl.setText(storedUrl);
        }
        if (userProject != null) {
            userProject.setText(storedProject);
        }
        if (filePath != null) {
            filePath.setText(storedFile);
        }
        if (jobName != null) {
            jobName.setText(storedJob);
        }
        if (programPath != null) {
            programPath.setText(storedProgram);
        }
    }


    static String getStoredProject(Project project) {
        if (storedProject==null && project!=null){
            loadProperties(project);
        }
        return storedProject;
    }

    static String getStoredUrl(Project project) {
        if (storedUrl==null && project!=null){
            loadProperties(project);
        }
        return storedUrl;
    }

    static String getStoredAPIKey(Project project) {
        if (storedKey==null && project!=null){
            loadProperties(project);
        }
        return storedKey;
    }


}
