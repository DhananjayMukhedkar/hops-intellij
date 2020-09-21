package com.logicalclocks;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.logicalclocks.actions.FileCopyDialogAction;
import io.hops.cli.action.FileUploadAction;
import io.hops.cli.config.HopsworksAPIConfig;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JobsConfigurable implements Configurable {

    private final Project project;
    private  JTextField userFile = new JTextField();
    private JTextField userDestination = new JTextField();
    private JTextField userProject = new JTextField();


    private static final String PATH_URL = "hops.url";
    private static final String PATH_KEY = "hops.key";
    private static final String PATH_PROJECT = "hops.project";
    private static final String PATH_FILE = "hops.file";
    private static final String PATH_DEST = "hops.dest";

    public static String storedUrl = null;
    public static String storedKey = null;
    public static String storedProject = null;
    public static String storedFile = null;
    public static String storedDest = null;


    public JobsConfigurable(Project project){

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
        storedDest=properties.getValue(PATH_DEST);



    }
    
    
    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "HOPS SETTINGS";
    }



    @Override
    public @Nullable JComponent createComponent() {
         userFile = new JTextField();
         userDestination = new JTextField();



        JPanel container = new JPanel(new GridLayoutManager(3, 2));


        GridConstraints pathLabelConstraint0a = new GridConstraints();
        pathLabelConstraint0a.setRow(0);
        pathLabelConstraint0a.setColumn(0);
        pathLabelConstraint0a.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0a.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("Input file path: "), pathLabelConstraint0a);

        GridConstraints pathFieldConstraint0a = new GridConstraints();
        pathFieldConstraint0a.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0a.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0a.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0a.setRow(0);
        pathFieldConstraint0a.setColumn(1);
        pathFieldConstraint0a.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);

        container.add(userFile, pathFieldConstraint0a);

        GridConstraints pathLabelConstraint0b = new GridConstraints();
        pathLabelConstraint0b.setRow(1);
        pathLabelConstraint0b.setColumn(0);
        pathLabelConstraint0b.setFill(GridConstraints.FILL_HORIZONTAL);
        pathLabelConstraint0b.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(new JLabel("Destination: "), pathLabelConstraint0b);

        GridConstraints pathFieldConstraint0b = new GridConstraints();
        pathFieldConstraint0b.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        pathFieldConstraint0b.setFill(GridConstraints.FILL_HORIZONTAL);
        pathFieldConstraint0b.setAnchor(GridConstraints.ANCHOR_WEST);
        pathFieldConstraint0b.setRow(1);
        pathFieldConstraint0b.setColumn(1);
        pathFieldConstraint0b.setVSizePolicy(GridConstraints.SIZEPOLICY_CAN_SHRINK);
        container.add(userDestination, pathFieldConstraint0b);


        JPanel spacer = new JPanel();
        GridConstraints spacerConstraints = new GridConstraints();
        spacerConstraints.setRow(2);
        spacerConstraints.setFill(GridConstraints.FILL_BOTH);
        container.add(spacer, spacerConstraints);
        return container;
    }

    @Override
    public boolean isModified() {
        if (userFile!=null ) {
            return true;
        }

        // check last value != current return true

        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        storedFile = userFile.getText().trim();
        System.out.println(" storedFile "+storedFile);
        storedDest = userDestination.getText().trim();
        System.out.println(" key "+storedDest);


        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        properties.setValue(PATH_FILE, storedFile);
        properties.setValue(PATH_DEST, storedDest);

        /*
        call FileUpload
         */
        HopsUtils util=new HopsUtils();
        String hopsworksApiKey = util.getAPIKey(project);
        String hopsworksUrl = util.getURL(project);
        String projectName = util.getProjectName(project);

        String localFilePath = "file:///"+userFile.getText();

        try {

            HopsworksAPIConfig hopsworksAPIConfig = new HopsworksAPIConfig( hopsworksApiKey, hopsworksUrl, projectName);
            FileUploadAction action = new FileUploadAction(hopsworksAPIConfig,userDestination.getText(),  localFilePath);
            action.execute();
        } catch (IOException ex) {
            Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FileCopyDialogAction.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void disposeUIResources() {
        userDestination=null;
        userFile=null;
    }

    @Override
    public void reset() {
        if (userFile != null) {
            userFile.setText(storedFile);
        }
        if (userDestination != null) {
            userDestination.setText(storedDest);
        }

    }


}
