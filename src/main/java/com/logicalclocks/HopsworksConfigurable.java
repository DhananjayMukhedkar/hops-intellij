package com.logicalclocks;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class HopsworksConfigurable implements Configurable {

    private static final String ARCHIVES_LBL = "Archives :";
    private static final String JARS_LBL = "Jars :";
    private static final String FILES_LBL = "Files :";
    private static final String PYTHON_LBL = "Python :";
    private static final String MORE_PROP_LBL = "More Properties :";
    private static final String HDFS_LBL = "HDFS destination path: ";
    private static final String USER_ARGS_LBL = "User Arguments: " ;
    private static final String MAIN_CLASS_LBL ="Main Class: ";
    private final Project project;
    HopsUtils util=new HopsUtils();
    private  JTextField userKey = new JTextField();
    private JTextField userUrl = new JTextField();
    private JTextField userProject = new JTextField();
    private JTextField logFilePath = new JTextField();
    private JTextField jobName = new JTextField();
    private JTextField hdfsPath = new JTextField();
    private JTextField userArgs = new JTextField();
    private JTextField mainClass = new JTextField();
    private JTextField execId = new JTextField();
    private JTextField additionalFiles = new JTextField();
    private JTextField archives = new JTextField();
    private JTextField additionalJars = new JTextField();
    private JTextField pythonDepend  = new JTextField();
    private JTextField moreProps  = new JTextField();


    private static final String PATH_URL = HopsUtils.PATH_URL;
    private static final String PATH_KEY = HopsUtils.PATH_KEY;
    private static final String PATH_PROJECT = HopsUtils.PATH_PROJECT;
    private static final String PATH_FILE = HopsUtils.PATH_FILE;
    private static final String PATH_JOB = HopsUtils.PATH_JOB;
    private static final String PATH_PROGRAM = HopsUtils.PATH_PROGRAM;
    private static final String PATH_USERARGS = HopsUtils.PATH_USERARGS;
    private static final String PATH_MAINCLASS = HopsUtils.PATH_MAINCLASS;
    private static final String PATH_EXECID = HopsUtils.PATH_EXECID;
    private static final String PATH_JOBTYPE = HopsUtils.PATH_JOBTYPE;
    private static final String PATH_ADDFILE = HopsUtils.PATH_ADDFILE;
    private static final String PATH_ADDJAR = HopsUtils.PATH_ADDJAR;
    private static final String PATH_ARCHIVE = HopsUtils.PATH_ARCHIVE;
    private static final String PATH_PYTHON_DEPEND = HopsUtils.PATH_PYTHON_DEPEND;
    private static final String PATH_MORE_PROP = HopsUtils.PATH_MORE_PROP;


    private static String storedUrl = null;
    private static String storedKey = null;
    private static String storedProject = null;
    private static String storedFile = null;
    private static String storedJob = null;
    private static String storedProgram = null;
    private static String storedUserArgs = null;
    private static String storedMainClass = null;
    private static String storedExecId = null;
    private static String jobType=null;
    private static String storedAddFile=null;
    private static String storedAddJar=null;
    private static String storedPythonDepend=null;
    private static String storedArchive=null;
    private static String storedMoreProp=null;


    private JPanel inputPanel = null;
    private JPanel advanceInputPanel = null;
    private JPanel superPanel = null;

    ButtonGroup group = new ButtonGroup();
    JRadioButton sparkBtn = new JRadioButton();
    JRadioButton flinkBtn = new JRadioButton();
    JRadioButton pythonBtn = new JRadioButton();
    JCheckBox advanceBtn = new JCheckBox();
    LinkedHashMap<String,JTextField> advanceFieldmap=new LinkedHashMap<String,JTextField>();


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
        storedExecId=properties.getValue(PATH_EXECID);
        //jobType=properties.getValue(PATH_JOBTYPE);
    }
    
    
    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Hopsworks Settings";
    }



    @Override
    public @Nullable JComponent createComponent() {
       /*  userKey = new JTextField();
         userUrl = new JTextField();
         userProject = new JTextField();
         logFilePath =new JTextField();
         jobName=new JTextField();
         hdfsPath =new JTextField();
         userArgs=new JTextField();
         mainClass=new JTextField();
         execId=new JTextField();*/

        LinkedHashMap<String,JTextField> constantFieldsMap=new LinkedHashMap<String,JTextField>();
        constantFieldsMap.put("Hopsworks Project: ",userProject);
        constantFieldsMap.put("Hopsworks URL: ",userUrl);
        constantFieldsMap.put("Hopsworks API Key: ",userKey);
        constantFieldsMap.put("Job Name: ",jobName);
        constantFieldsMap.put("Local Path for Logs: ", logFilePath);
        constantFieldsMap.put("Execution Id: ",execId);


        //add input text field panel
        inputPanel = new JPanel(new GridLayoutManager(constantFieldsMap.size()+4, 2));
        Set<Map.Entry<String, JTextField>> e = constantFieldsMap.entrySet();
        Iterator<Map.Entry<String, JTextField>> it = e.iterator();
        int i=0;
        while(it.hasNext()){
            Map.Entry<String, JTextField> pair = it.next();
            inputPanel=createField(inputPanel,i,pair.getKey(),pair.getValue());
            i++;
        }
        JPanel spacer = new JPanel();
        GridConstraints spacerConstraints = new GridConstraints();
        spacerConstraints.setRow(i);
        spacerConstraints.setFill(GridConstraints.FILL_BOTH);
        inputPanel.add(spacer, spacerConstraints);

        //radio buttons
        sparkBtn.setText("SPARK");
        flinkBtn.setText("FLINK");
        pythonBtn.setText("PYTHON");
        group.add(sparkBtn);
        group.add(flinkBtn);
        group.add(pythonBtn);

        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
        JLabel label = new JLabel("Job Type : ");
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(label);
        sparkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        pythonBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        flinkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(sparkBtn);
        buttonPanel.add(flinkBtn);
        buttonPanel.add(pythonBtn);
        //add listeners
        flinkBtn.addActionListener(flinkAction);
        sparkBtn.addActionListener(sparkAction);
        pythonBtn.addActionListener(pythonAction);

        // advanced config panels
        int num_fields=4;


        //advanceInputPanel=createInputPanelFiels(advanceInputPanel,advanceFieldmap);
   /*     Component[] allComps = advanceInputPanel.getComponents();
        for (Component c:
                allComps) {
            c.setEnabled(false);
        }*/


        JPanel buttonPanel2=new JPanel();
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2,BoxLayout.X_AXIS));

        advanceBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        advanceBtn.setText("Advance Configurations");
        advanceBtn.setSelected(false);
        advanceBtn.addActionListener(advanceAction);
        buttonPanel2.add(advanceBtn);

        //add all panels
        superPanel = new JPanel();
        superPanel.setLayout(new BoxLayout(superPanel,BoxLayout.Y_AXIS));
        superPanel.add(buttonPanel);
        superPanel.add(inputPanel);
        superPanel.add(buttonPanel2);
        //initiliase advance panel
        advanceFieldmap.put(ARCHIVES_LBL,archives);
        advanceFieldmap.put(JARS_LBL,additionalJars);
        advanceFieldmap.put(PYTHON_LBL,pythonDepend);
        advanceFieldmap.put(FILES_LBL,additionalFiles);
        advanceFieldmap.put(MORE_PROP_LBL,moreProps);
        //

        advanceInputPanel=new JPanel(new GridLayoutManager(advanceFieldmap.size()+1, 2));
        superPanel.add(advanceInputPanel);

        return superPanel;

    }

    private JPanel createInputPanelFiels(JPanel panel, LinkedHashMap<String, JTextField> map) {

        Set<Map.Entry<String, JTextField>> e2 = map.entrySet();
        Iterator<Map.Entry<String, JTextField>> it2 = e2.iterator();
        int j=0;
        while(it2.hasNext()){
            Map.Entry<String, JTextField> pair = it2.next();
            panel=createField(panel,j,pair.getKey(),pair.getValue());
            j++;
        }

        JPanel spacer = new JPanel();
        GridConstraints spacerConstraints = new GridConstraints();
        spacerConstraints.setRow(j);
        spacerConstraints.setFill(GridConstraints.FILL_BOTH);
        panel.add(spacer, spacerConstraints);

        return panel;


    }

    /*
    add each  button listners
     */

    ActionListener flinkAction= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==flinkBtn){
                //labels
                HopsworksConfigurable.this.inputPanel.remove(8);
                HopsworksConfigurable.this.inputPanel.repaint();
                HopsworksConfigurable.this.inputPanel.remove(8);
                HopsworksConfigurable.this.inputPanel.repaint();
                HopsworksConfigurable.this.inputPanel.remove(8);
                HopsworksConfigurable.this.inputPanel.repaint();
                HopsworksConfigurable.this.inputPanel.remove(8);
                HopsworksConfigurable.this.inputPanel.repaint();
                HopsworksConfigurable.this.inputPanel.remove(8);
                HopsworksConfigurable.this.inputPanel.repaint();
                HopsworksConfigurable.this.inputPanel.remove(8);
                HopsworksConfigurable.this.inputPanel.repaint();



                /*HopsworksConfigurable.this.inputPanel.getComponent(8).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(10).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(12).setEnabled(false);
                //fields
                HopsworksConfigurable.this.inputPanel.getComponent(9).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(11).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(13).setEnabled(false);*/

            }
        }
    };

    ActionListener sparkAction= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==sparkBtn){
                //fields
           /*     HopsworksConfigurable.this.inputPanel.getComponent(9).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(11).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(13).setEnabled(true);
                //labels
                HopsworksConfigurable.this.inputPanel.getComponent(8).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(10).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(12).setEnabled(true);*/

                inputPanel=createField(inputPanel,6,HDFS_LBL,hdfsPath);
                inputPanel=createField(inputPanel,7, USER_ARGS_LBL,userArgs);
                inputPanel=createField(inputPanel,8,MAIN_CLASS_LBL,mainClass);

            }
        }
    };

    ActionListener pythonAction= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==pythonBtn){
                /*HopsworksConfigurable.this.inputPanel.getComponent(8).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(10).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(9).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(11).setEnabled(true);
                HopsworksConfigurable.this.inputPanel.getComponent(12).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(13).setEnabled(false);*/

                //remove fields










            }
        }
    };

    ActionListener advanceAction= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==advanceBtn){
                if(advanceBtn.isSelected()){
                    advanceInputPanel.removeAll();
                    if(sparkBtn.isSelected()){
                        advanceInputPanel=createInputPanelFiels(advanceInputPanel,advanceFieldmap);
//                        Component[] allComps = advanceInputPanel.getComponents();
//                        for (Component c: allComps) {
//                            c.setEnabled(true);
//                        }
                    }else if (pythonBtn.isSelected()){
                        /*HopsworksConfigurable.this.advanceInputPanel.getComponent(6).setEnabled(true);
                        HopsworksConfigurable.this.advanceInputPanel.getComponent(7).setEnabled(true);*/
                        advanceInputPanel=createField(advanceInputPanel,0,FILES_LBL,advanceFieldmap.get(FILES_LBL));
                    }else if (flinkBtn.isSelected()){
                     /*   HopsworksConfigurable.this.advanceInputPanel.getComponent(8).setEnabled(true);
                        HopsworksConfigurable.this.advanceInputPanel.getComponent(9).setEnabled(true);*/
                        advanceInputPanel=createField(advanceInputPanel,0,MORE_PROP_LBL,advanceFieldmap.get(MORE_PROP_LBL));
                    }
                } else { //check box disable
                 /*   Component[] allComps = advanceInputPanel.getComponents();
                    for (Component c: allComps) {
                        c.setEnabled(false);
                    }*/
                    advanceInputPanel.removeAll();
                }
            }
        }
    };







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
        storedFile= logFilePath.getText().trim();
        storedJob=jobName.getText().trim();
        storedProgram= hdfsPath.getText().trim();
        storedUserArgs=userArgs.getText().trim();
        storedMainClass=mainClass.getText().trim();
        storedExecId=execId.getText().trim();
        if (sparkBtn.isSelected()) jobType= sparkBtn.getText();
        else if (pythonBtn.isSelected()) jobType = pythonBtn.getText();
        else  jobType= flinkBtn.getText();

        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        properties.setValue(PATH_URL, storedUrl);
        properties.setValue(PATH_KEY, storedKey);
        properties.setValue(PATH_PROJECT, storedProject);
        properties.setValue(PATH_FILE, storedFile);
        properties.setValue(PATH_JOB, storedJob);
        properties.setValue(PATH_PROGRAM, storedProgram);
        properties.setValue(PATH_USERARGS, storedUserArgs);
        properties.setValue(PATH_MAINCLASS, storedMainClass);
        properties.setValue(PATH_EXECID, storedExecId);
        properties.setValue(PATH_JOBTYPE,jobType);


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
        if (logFilePath != null) {
            logFilePath.setText(storedFile);
        }
        if (jobName != null) {
            jobName.setText(storedJob);
        }
        if (hdfsPath != null) {
            hdfsPath.setText(storedProgram);
        }
        if (userArgs != null) {
            userArgs.setText(storedUserArgs);
        }
        if (mainClass != null) {
            mainClass.setText(storedMainClass);
        }
        if (execId != null) {
            execId.setText(storedExecId);
        }


        if ( jobType==null || jobType ==  HopsUtils.SPARK) sparkBtn.setSelected(true);
        else if (jobType == HopsUtils.PYTHON) pythonBtn.setSelected(true);
        else  flinkBtn.setSelected(true);
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
