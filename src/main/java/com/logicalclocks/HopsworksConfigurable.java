package com.logicalclocks;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static com.logicalclocks.HopsUtils.*;


public class HopsworksConfigurable implements Configurable {


    private static int MAX_LAYOUT ;
    private final Project project;

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
    private JTextField driverMem  = new JTextField();
    private JTextField executorMem  = new JTextField();
    private JTextField execVC  = new JTextField();
    private JTextField driverVC  = new JTextField();
    private JTextField numExecutor  = new JTextField();
    private JTextField memory  = new JTextField();
    private JTextField cpuCore  = new JTextField();
    private JTextField jobManagerMem  = new JTextField();
    private JTextField jNumTaskManager  = new JTextField();
    private JTextField taskManagerMem  = new JTextField();
    private JTextField numSlots  = new JTextField();


/*    private static final String PATH_URL = HopsUtils.PATH_URL;
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
    private static final String PATH_MORE_PROP = HopsUtils.PATH_MORE_PROP;*/



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
    private JPanel additionalInputPanel = null;
    private JPanel advanceInputPanel = null;
    private JPanel superPanel = null;
    private JPanel jobConfigPanel = null;

    ButtonGroup group = new ButtonGroup();
    JRadioButton sparkBtn = new JRadioButton();
    JRadioButton flinkBtn = new JRadioButton();
    JRadioButton pythonBtn = new JRadioButton();
    JCheckBox advanceBtn = new JCheckBox();
    LinkedHashMap<String,JTextField> constantFieldsMap=new LinkedHashMap<String,JTextField>();
    LinkedHashMap<String,JTextField> advanceFieldmap=new LinkedHashMap<String,JTextField>();
    LinkedHashMap<String,JTextField> sparkConfigMap =new LinkedHashMap<String,JTextField>();
    LinkedHashMap<String,JTextField> flinkConfigMap =new LinkedHashMap<String,JTextField>();
    LinkedHashMap<String,JTextField> pythonConfigMap=new LinkedHashMap<String,JTextField>();
    LinkedHashMap<String,JTextField> sparkAddInputs=new LinkedHashMap<String,JTextField>();

    LinkedHashMap<String,JTextField> pythonAddInputs=new LinkedHashMap<String,JTextField>();

    public HopsworksConfigurable(Project project){

        this.project = project;
        if(project!=null){
            loadProperties(project);
        }

    }

    private static void loadProperties(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        storedUrl = properties.getValue(HopsUtils.PATH_URL);
        storedKey = properties.getValue(HopsUtils.PATH_KEY);
        storedProject = properties.getValue(HopsUtils.PATH_PROJECT);
        storedFile = properties.getValue(HopsUtils.PATH_FILE);
        storedJob = properties.getValue(HopsUtils.PATH_JOB);
        storedProgram = properties.getValue(HopsUtils.PATH_PROGRAM);
        storedUserArgs=properties.getValue(HopsUtils.PATH_USERARGS);
        storedMainClass=properties.getValue(HopsUtils.PATH_MAINCLASS);
        storedExecId=properties.getValue(HopsUtils.PATH_EXECID);
        //jobType=properties.getValue(PATH_JOBTYPE);
    }
    
    public void initPanelMaps(){

        //main input panel
        constantFieldsMap.put("Hopsworks Project: ",userProject);
        constantFieldsMap.put("Hopsworks URL: ",userUrl);
        constantFieldsMap.put("Hopsworks API Key: ",userKey);
        constantFieldsMap.put("Job Name: ",jobName);
        constantFieldsMap.put("Local Path for Logs: ", logFilePath);
        constantFieldsMap.put("Execution Id: ",execId);
        /*constantFieldsMap.put(HopsUtils.HDFS_LBL,hdfsPath);
        constantFieldsMap.put(HopsUtils.USER_ARGS_LBL,userArgs);
        constantFieldsMap.put(HopsUtils.MAIN_CLASS_LBL,mainClass);*/

        sparkAddInputs.put(HopsUtils.HDFS_LBL,hdfsPath);
        sparkAddInputs.put(HopsUtils.USER_ARGS_LBL,userArgs);
        sparkAddInputs.put(HopsUtils.MAIN_CLASS_LBL,mainClass);

        pythonAddInputs.put(HopsUtils.HDFS_LBL,hdfsPath);
        pythonAddInputs.put(HopsUtils.USER_ARGS_LBL,userArgs);





        // spark configs
        sparkConfigMap.put(HopsUtils.DRIVER_MEM_LBL,driverMem);
        sparkConfigMap.put(HopsUtils.EXECUTOR_MEM_LBL,executorMem);
        sparkConfigMap.put(HopsUtils.EXEC_VC_LBL,execVC);
        sparkConfigMap.put(HopsUtils.DRIVER_VC_LBL,driverVC);
        sparkConfigMap.put(HopsUtils.NUM_EXEC_LBL,numExecutor);

        // python configs
        pythonConfigMap.put(HopsUtils.MEMORY_LBL,memory);
        pythonConfigMap.put(HopsUtils.CPU_LBL,cpuCore);

        // flink configs
        flinkConfigMap.put(HopsUtils.JOB_MANAGER_MM_LBL,jobManagerMem);
        flinkConfigMap.put(HopsUtils.JNUM_TASK_LBL,jNumTaskManager);
        flinkConfigMap.put(HopsUtils.TASK_MANAGER_MM_LBL,taskManagerMem);
        flinkConfigMap.put(HopsUtils.NUM_SLOT_LBL,numSlots);

        //initiliase advance panel
        advanceFieldmap.put(HopsUtils.ARCHIVES_LBL,archives);
        advanceFieldmap.put(HopsUtils.JARS_LBL,additionalJars);
        advanceFieldmap.put(HopsUtils.PYTHON_LBL,pythonDepend);
        advanceFieldmap.put(HopsUtils.FILES_LBL,additionalFiles);
        advanceFieldmap.put(HopsUtils.MORE_PROP_LBL,moreProps);

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


        initPanelMaps();



        //add input text field panel
        GridLayoutManager layout = new GridLayoutManager(constantFieldsMap.size() + 1, 2);
        layout.setVGap(1);
        inputPanel = new JPanel(layout);
    /*    Set<Map.Entry<String, JTextField>> e = constantFieldsMap.entrySet();
        Iterator<Map.Entry<String, JTextField>> it = e.iterator();
        int i=0;
        while(it.hasNext()){
            Map.Entry<String, JTextField> pair = it.next();
            inputPanel=createField(inputPanel,i,pair.getKey(),pair.getValue());
            i++;
        }*/

        inputPanel=HopsUtils.createInputPanel(inputPanel,constantFieldsMap);

        MAX_LAYOUT=inputPanel.getComponents().length;

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



        //advanceInputPanel=createInputPanelFiels(advanceInputPanel,advanceFieldmap);
   /*     Component[] allComps = advanceInputPanel.getComponents();
        for (Component c:
                allComps) {
            c.setEnabled(false);
        }*/




        GridLayoutManager layout3 = new GridLayoutManager(sparkConfigMap.size() + 1, 2);
        layout3.setVGap(1);
        jobConfigPanel =new JPanel(layout3);
        jobConfigPanel =HopsUtils.createInputPanel(jobConfigPanel, sparkConfigMap);


        GridLayoutManager layout2=new GridLayoutManager(advanceFieldmap.size()+1, 2);
        layout2.setVGap(1);
        advanceInputPanel=new JPanel(layout2);

        //add checkbox button
        JPanel buttonPanel2=new JPanel();
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2,BoxLayout.X_AXIS));
        advanceBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        advanceBtn.setText("Advance Configurations");
        advanceBtn.setSelected(false);
        advanceBtn.addActionListener(advanceAction);
        buttonPanel2.add(advanceBtn);
        GridLayoutManager layout4 = null;
        if(jobType==null || jobType==SPARK)
         layout4 = new GridLayoutManager(sparkAddInputs.size() + 1, 2);
        else if (jobType==PYTHON)
            layout4 = new GridLayoutManager(pythonAddInputs.size() + 1, 2);



        layout4.setVGap(1);
        additionalInputPanel = new JPanel(layout4);

        additionalInputPanel=createInputPanel(additionalInputPanel,sparkAddInputs);


        //add all panels
        superPanel = new JPanel();
        superPanel.setLayout(new BoxLayout(superPanel,BoxLayout.Y_AXIS));
        superPanel.add(buttonPanel);
        superPanel.add(inputPanel);

        superPanel.add(additionalInputPanel);

        superPanel.add(jobConfigPanel);
        superPanel.add(buttonPanel2);
        superPanel.add(advanceInputPanel);



        return superPanel;

    }



    /*
    add each  button listners
     */

    ActionListener flinkAction= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==flinkBtn){
                //labels
                int l=HopsworksConfigurable.this.inputPanel.getComponents().length;
                additionalInputPanel.removeAll();
           /*     if(l==MAX_LAYOUT) {
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);
                }else{
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);
                    HopsworksConfigurable.this.inputPanel.remove(12);

                }*/
                /*HopsworksConfigurable.this.inputPanel.getComponent(8).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(10).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(12).setEnabled(false);
                //fields
                HopsworksConfigurable.this.inputPanel.getComponent(9).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(11).setEnabled(false);
                HopsworksConfigurable.this.inputPanel.getComponent(13).setEnabled(false);*/
                //HopsworksConfigurable.this.inputPanel.repaint();

                jobConfigPanel.removeAll();
                jobConfigPanel=createInputPanel(jobConfigPanel,flinkConfigMap);
                advanceBtn.setSelected(false);
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
                int l=HopsworksConfigurable.this.inputPanel.getComponents().length;
        /*        if(l==MAX_LAYOUT-6){
                    inputPanel=createField(inputPanel,6,HopsUtils.HDFS_LBL,hdfsPath);
                    inputPanel=createField(inputPanel,7, HopsUtils.USER_ARGS_LBL,userArgs);
                    inputPanel=createField(inputPanel,8,HopsUtils.MAIN_CLASS_LBL,mainClass);
                }else {
                    inputPanel=createField(inputPanel,8,HopsUtils.MAIN_CLASS_LBL,mainClass);

                }*/


                additionalInputPanel.removeAll();
                additionalInputPanel=createInputPanel(additionalInputPanel,sparkAddInputs);

                jobConfigPanel.removeAll();
                jobConfigPanel=createInputPanel(jobConfigPanel, sparkConfigMap);
                advanceBtn.setSelected(false);

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
                int l=inputPanel.getComponents().length;
              /*  if(l==MAX_LAYOUT){// if all components exists
                    inputPanel.remove(16);//field
                    inputPanel.remove(16);//label

                }else {//
                    inputPanel=createField(inputPanel,6,HopsUtils.HDFS_LBL,hdfsPath);
                    inputPanel=createField(inputPanel,7, HopsUtils.USER_ARGS_LBL,userArgs);

                }*/
                additionalInputPanel.removeAll();

                additionalInputPanel=createInputPanel(additionalInputPanel,pythonAddInputs);

                jobConfigPanel.removeAll();
                jobConfigPanel=createInputPanel(jobConfigPanel,pythonConfigMap);
                advanceBtn.setSelected(false);


                //HopsworksConfigurable.this.inputPanel.repaint();

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
                        advanceInputPanel=HopsUtils.createInputPanel(advanceInputPanel,advanceFieldmap);
//                        Component[] allComps = advanceInputPanel.getComponents();
//                        for (Component c: allComps) {
//                            c.setEnabled(true);
//                        }
                    }else if (pythonBtn.isSelected()){
                        /*HopsworksConfigurable.this.advanceInputPanel.getComponent(6).setEnabled(true);
                        HopsworksConfigurable.this.advanceInputPanel.getComponent(7).setEnabled(true);*/
                        advanceInputPanel=createField(advanceInputPanel,0,HopsUtils.FILES_LBL,advanceFieldmap.get(HopsUtils.FILES_LBL));
                    }else if (flinkBtn.isSelected()){
                     /*   HopsworksConfigurable.this.advanceInputPanel.getComponent(8).setEnabled(true);
                        HopsworksConfigurable.this.advanceInputPanel.getComponent(9).setEnabled(true);*/
                        advanceInputPanel=createField(advanceInputPanel,0,HopsUtils.MORE_PROP_LBL,advanceFieldmap.get(HopsUtils.MORE_PROP_LBL));
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
        properties.setValue(HopsUtils.PATH_URL, storedUrl);
        properties.setValue(HopsUtils.PATH_KEY, storedKey);
        properties.setValue(HopsUtils.PATH_PROJECT, storedProject);
        properties.setValue(HopsUtils.PATH_FILE, storedFile);
        properties.setValue(HopsUtils.PATH_JOB, storedJob);
        properties.setValue(HopsUtils.PATH_PROGRAM, storedProgram);
        properties.setValue(HopsUtils.PATH_USERARGS, storedUserArgs);
        properties.setValue(HopsUtils.PATH_MAINCLASS, storedMainClass);
        properties.setValue(HopsUtils.PATH_EXECID, storedExecId);
        properties.setValue(HopsUtils.PATH_JOBTYPE,jobType);


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
