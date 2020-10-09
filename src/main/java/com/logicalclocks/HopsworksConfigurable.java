package com.logicalclocks;

import com.intellij.ide.actions.ShowSettingsUtilImpl;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.ui.popup.PopupFactoryImpl;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.logicalclocks.actions.FileCopyDialogAction;
import io.hops.cli.action.FileUploadAction;
import io.hops.cli.action.JobRunAction;
import io.hops.cli.config.HopsworksAPIConfig;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class HopsworksConfigurable implements Configurable {

    private final Project project;
    HopsUtils util=new HopsUtils();
    private  JTextField userKey = new JTextField();
    private JTextField userUrl = new JTextField();
    private JTextField userProject = new JTextField();
    private JTextField filePath = new JTextField();
    private JTextField jobName = new JTextField();
    private JTextField programPath = new JTextField();
    private JTextField userArgs = new JTextField();
    private JTextField mainClass = new JTextField();


    private static final String PATH_URL = HopsUtils.PATH_URL;
    private static final String PATH_KEY = HopsUtils.PATH_KEY;
    private static final String PATH_PROJECT = HopsUtils.PATH_PROJECT;
    private static final String PATH_FILE = HopsUtils.PATH_FILE;
    private static final String PATH_JOB = HopsUtils.PATH_JOB;
    private static final String PATH_PROGRAM = HopsUtils.PATH_PROGRAM;
    private static final String PATH_USERARGS = HopsUtils.PATH_USERARGS;
    private static final String PATH_MAINCLASS = HopsUtils.PATH_MAINCLASS;


    public static String storedUrl = null;
    public static String storedKey = null;
    public static String storedProject = null;
    public static String storedFile = null;
    public static String storedJob = null;
    public static String storedProgram = null;
    public static String storedUserArgs = null;
    public static String storedMainClass = null;

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
        storedUserArgs=properties.getValue(PATH_USERARGS);
        storedMainClass=properties.getValue(PATH_MAINCLASS);
    }
    
    
    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Hopsworks Settings";
    }



    @Override
    public @Nullable JComponent createComponent() {
         userKey = new JTextField();
         userUrl = new JTextField();
         userProject = new JTextField();
         filePath=new JTextField();
         jobName=new JTextField();
         programPath=new JTextField();
         userArgs=new JTextField();
         mainClass=new JTextField();

        JPanel container = new JPanel(new GridLayoutManager(9, 2));

        //1
        container=createField(container,0,"Hopsworks Project: ",userProject);
        //2
        container=createField(container,1,"Hopsworks URL: ",userUrl);
        //3
        container=createField(container,2,"Hopsworks API Key: ",userKey);
        //4
        container=createField(container,3,"Local Path for Logs: ",filePath);
        //5
        container=createField(container,4,"Hopsworks Job Name: ",jobName);
        //6
        container=createField(container,5,"HDFS destination path: ",programPath);
        //7
        container=createField(container,6,"User args: ",userArgs);
        //8
        container=createField(container,7,"Main Class:",mainClass);


        JPanel spacer = new JPanel();
        GridConstraints spacerConstraints = new GridConstraints();
        spacerConstraints.setRow(8);
        spacerConstraints.setFill(GridConstraints.FILL_BOTH);
        container.add(spacer, spacerConstraints);


        return container;

    }



    private JPanel createField(JPanel container,int row,String label,JTextField field ){

        if (container!=null){
            GridConstraints pathLabelConstraint = new GridConstraints();
            pathLabelConstraint.setRow(row);
            pathLabelConstraint.setColumn(0);
            pathLabelConstraint.setFill(GridConstraints.FILL_HORIZONTAL);
            pathLabelConstraint.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
            container.add(new JLabel(label), pathLabelConstraint);

            GridConstraints pathFieldConstraint = new GridConstraints();
            pathFieldConstraint.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
            pathFieldConstraint.setFill(GridConstraints.FILL_HORIZONTAL);
            pathFieldConstraint.setAnchor(GridConstraints.ANCHOR_WEST);
            pathFieldConstraint.setRow(row);
            pathFieldConstraint.setColumn(1);
            pathFieldConstraint.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
            container.add(field, pathFieldConstraint);
        }
        return container;

    }

    @Override
    public boolean isModified() {
        // TODO: insert action logic here

        return userKey != null || userProject != null || userUrl != null;
        // check for all other variables

        // check last value != current return true
    }

    @Override
    public void apply() throws ConfigurationException {


        storedUrl = userUrl.getText().trim();
        storedKey = userKey.getText().trim();
        storedProject= userProject.getText().trim();
        storedFile=filePath.getText().trim();
        storedJob=jobName.getText().trim();
        storedProgram=programPath.getText().trim();
        storedUserArgs=userArgs.getText().trim();
        storedMainClass=mainClass.getText().trim();

        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        properties.setValue(PATH_URL, storedUrl);
        properties.setValue(PATH_KEY, storedKey);
        properties.setValue(PATH_PROJECT, storedProject);
        properties.setValue(PATH_FILE, storedFile);
        properties.setValue(PATH_JOB, storedJob);
        properties.setValue(PATH_PROGRAM, storedProgram);
        properties.setValue(PATH_USERARGS, storedUserArgs);
        properties.setValue(PATH_MAINCLASS, storedMainClass);
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
        if (userArgs != null) {
            userArgs.setText(storedUserArgs);
        }
        if (mainClass != null) {
            mainClass.setText(storedMainClass);
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
