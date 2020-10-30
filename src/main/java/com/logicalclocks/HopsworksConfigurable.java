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
    //advance config
    private JTextField additionalFiles = new JTextField();
    private JTextField archives = new JTextField();
    private JTextField additionalJars = new JTextField();
    private JTextField pythonDepend  = new JTextField();
    private JTextField moreProps  = new JTextField();
    //job config params
    private JTextField driverMem  = new JTextField("2048");
    private JTextField executorMem  = new JTextField("4096");
    private JTextField execVC  = new JTextField("1");
    private JTextField driverVC  = new JTextField("1");
    private JTextField numExecutor  = new JTextField("1");
    //python configs
    private JTextField memory  = new JTextField("2028");
    private JTextField cpuCore  = new JTextField("1");
    //flink configs
    private JTextField jobManagerMem  = new JTextField("1024");
    private JTextField numTaskManager = new JTextField("1");
    private JTextField taskManagerMem  = new JTextField("1024");
    private JTextField numSlots  = new JTextField("1");


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
    private static String storedJobType =null;
    //advance configs
    private static String storedAddFile=null;
    private static String storedAddJar=null;
    private static String storedPythonDepend=null;
    private static String storedArchive=null;
    private static String storedMoreProp=null;
    private static boolean isAdvancedConfig=false;

    //spark job config params
    private static String stored_spDriverMem=null;
    private static String stored_spExecMem=null;
    private static String stored_spExecVC=null;
    private static String stored_spDriverVC=null;
    private static String stored_spNumExec=null;
    //python job config params
    private static String stored_pyMemory=null;
    private static String stored_pyCpuCore=null;
    //flink job config params
    private static String stored_flJobManMem=null;
    private static String stored_flNumTaskMan =null;
    private static String stored_flTaskManMem=null;
    private static String stored_flNumSlots=null;

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
        storedJobType =properties.getValue(PATH_JOBTYPE);
        //spark configs
        stored_spDriverMem=properties.getValue(PATH_SP_DRVERMEM);
        stored_spNumExec=properties.getValue(PATH_SP_NUM_EXEC);
        stored_spExecVC=properties.getValue(PATH_SP_EXEC_VC);
        stored_spDriverVC=properties.getValue(PATH_SP_DRIVER_VC);
        stored_spExecMem =properties.getValue(PATH_SP_EXECMEM);

        //python
        stored_pyMemory=properties.getValue(PATH_PY_MEMORY);
        stored_pyCpuCore=properties.getValue(PATH_PY_CPU_CORE);
        //flink
        stored_flTaskManMem=properties.getValue(PATH_FL_TASK_MANAGER_MEM);
        stored_flNumSlots=properties.getValue(PATH_FL_NUM_SLOTS);
        stored_flJobManMem=properties.getValue(PATH_FL_JOB_MANAGER_MEM);
        stored_flNumTaskMan=properties.getValue(PATH_FL_NUM_TASK_MANGER);
        //advance
        storedAddFile=properties.getValue(PATH_ADDFILE);
        storedAddJar=properties.getValue(PATH_ADDJAR);
        storedPythonDepend=properties.getValue(PATH_PYTHON_DEPEND);
        storedMoreProp=properties.getValue(PATH_MORE_PROP);
        storedArchive=properties.getValue(PATH_ARCHIVE);
        isAdvancedConfig=properties.getBoolean(PATH_IS_ADVANCED);

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
        flinkConfigMap.put(HopsUtils.NUM_TASK_LBL, numTaskManager);
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

        //add primary input text field panel
        GridLayoutManager layout = new GridLayoutManager(constantFieldsMap.size() + 1, 2);
        layout.setVGap(1);
        inputPanel = new JPanel(layout);
        inputPanel=HopsUtils.createInputPanel(inputPanel,constantFieldsMap);
        MAX_LAYOUT=inputPanel.getComponents().length;

    /*    Set<Map.Entry<String, JTextField>> e = constantFieldsMap.entrySet();
        Iterator<Map.Entry<String, JTextField>> it = e.iterator();
        int i=0;
        while(it.hasNext()){
            Map.Entry<String, JTextField> pair = it.next();
            inputPanel=createField(inputPanel,i,pair.getKey(),pair.getValue());
            i++;
        }*/



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

        //additional input fields panel
        GridLayoutManager lAdd2 = new GridLayoutManager(sparkAddInputs.size() + 1, 2);
        lAdd2.setVGap(1);
        additionalInputPanel = new JPanel(lAdd2);
        //job config parameters panel
        GridLayoutManager layout3 = new GridLayoutManager(sparkConfigMap.size() + 1, 2);
        layout3.setVGap(1);
        jobConfigPanel =new JPanel(layout3);
        //advance config panel
        GridLayoutManager layout2=new GridLayoutManager(advanceFieldmap.size()+1, 2);
        layout2.setVGap(1);
        advanceInputPanel=new JPanel(layout2);

        //add checkbox button
        JPanel buttonPanel2=new JPanel();
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2,BoxLayout.X_AXIS));
        advanceBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        advanceBtn.setText(HopsUtils.ADVANCED_LBL);

        if(isAdvancedConfig) advanceBtn.setSelected(true);
        else advanceBtn.setSelected(false);

        advanceBtn.addActionListener(advanceAction);
        buttonPanel2.add(advanceBtn);


        if(storedJobType ==null || storedJobType.equals(HopsUtils.SPARK)){
            additionalInputPanel=createInputPanel(additionalInputPanel,sparkAddInputs);
            jobConfigPanel =HopsUtils.createInputPanel(jobConfigPanel, sparkConfigMap);
        } else if (storedJobType.equals(HopsUtils.PYTHON)){
            additionalInputPanel=createInputPanel(additionalInputPanel,pythonAddInputs);
            jobConfigPanel =HopsUtils.createInputPanel(jobConfigPanel, pythonConfigMap);
        } else {
            additionalInputPanel=createInputPanel(additionalInputPanel,new LinkedHashMap<>());
            jobConfigPanel =HopsUtils.createInputPanel(jobConfigPanel, flinkConfigMap);
        }

        //add all panels
        superPanel = new JPanel();
        superPanel.setLayout(new BoxLayout(superPanel,BoxLayout.Y_AXIS));
        superPanel.add(buttonPanel); //job type buttons
        superPanel.add(inputPanel); // primary input fields
        superPanel.add(additionalInputPanel); // job config specific inputs 2nd panel
        superPanel.add(jobConfigPanel); // job config parameters 3rd panel
        superPanel.add(buttonPanel2); // checkbox
        superPanel.add(advanceInputPanel); // advance configs

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

              /*
              int l=inputPanel.getComponents().length;
              if(l==MAX_LAYOUT){// if all components exists
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
        PropertiesComponent properties = PropertiesComponent.getInstance(project);

        storedUrl = userUrl.getText().trim();
        storedKey = userKey.getText().trim();
        storedProject= userProject.getText().trim();
        storedFile= logFilePath.getText().trim();
        storedJob=jobName.getText().trim();
        storedProgram= hdfsPath.getText().trim();
        storedUserArgs=userArgs.getText().trim();
        storedMainClass=mainClass.getText().trim();
        storedExecId=execId.getText().trim();
        if (sparkBtn.isSelected()) {
            storedJobType = sparkBtn.getText();
            //job config params
            stored_spDriverMem=driverMem.getText().trim();
            stored_spDriverVC=driverVC.getText().trim();
            stored_spExecMem=executorMem.getText().trim();
            stored_spExecVC=execVC.getText().trim();
            stored_spNumExec=numExecutor.getText().trim();
            //jobconfigs
            properties.setValue(PATH_SP_DRIVER_VC,stored_spDriverVC);
            properties.setValue(PATH_SP_DRVERMEM,stored_spDriverMem);
            properties.setValue(PATH_SP_EXEC_VC,stored_spExecVC);
            properties.setValue(PATH_SP_EXECMEM,stored_spExecMem);
            properties.setValue(PATH_SP_NUM_EXEC,stored_spNumExec);

        }
        else if (pythonBtn.isSelected()){
            storedJobType = pythonBtn.getText();
            stored_pyCpuCore=cpuCore.getText().trim();
            stored_pyMemory=memory.getText().trim();
            properties.setValue(PATH_PY_MEMORY,stored_pyMemory);
            properties.setValue(PATH_PY_CPU_CORE,stored_pyCpuCore);
        }
        else {
            storedJobType = flinkBtn.getText();
            stored_flNumTaskMan = numTaskManager.getText().trim();
            stored_flJobManMem=jobManagerMem.getText().trim();
            stored_flNumSlots=numSlots.getText().trim();
            stored_flTaskManMem=taskManagerMem.getText().trim();
            properties.setValue(PATH_FL_JOB_MANAGER_MEM,stored_flJobManMem);
            properties.setValue(PATH_FL_NUM_TASK_MANGER, stored_flNumTaskMan);
            properties.setValue(PATH_FL_TASK_MANAGER_MEM,stored_flTaskManMem);
            properties.setValue(PATH_FL_NUM_SLOTS,stored_flNumSlots);
        }

        //advance config params
        if(advanceBtn.isSelected()) isAdvancedConfig=true;
        storedAddFile=additionalFiles.getText().trim();
        storedAddJar=additionalJars.getText().trim();
        storedPythonDepend=pythonDepend.getText().trim();
        storedArchive=archives.getText().trim();
        storedMoreProp=moreProps.getText().trim();

        //set into project properties
        properties.setValue(HopsUtils.PATH_URL, storedUrl);
        properties.setValue(HopsUtils.PATH_KEY, storedKey);
        properties.setValue(HopsUtils.PATH_PROJECT, storedProject);
        properties.setValue(HopsUtils.PATH_FILE, storedFile);
        properties.setValue(HopsUtils.PATH_JOB, storedJob);
        properties.setValue(HopsUtils.PATH_PROGRAM, storedProgram);
        properties.setValue(HopsUtils.PATH_USERARGS, storedUserArgs);
        properties.setValue(HopsUtils.PATH_MAINCLASS, storedMainClass);
        properties.setValue(HopsUtils.PATH_EXECID, storedExecId);
        properties.setValue(HopsUtils.PATH_JOBTYPE, storedJobType);
        //advance config
        properties.setValue(PATH_IS_ADVANCED,isAdvancedConfig);
        properties.setValue(PATH_ADDFILE, storedAddFile);
        properties.setValue(PATH_ADDJAR, storedAddJar);
        properties.setValue(PATH_MORE_PROP, storedMoreProp);
        properties.setValue(PATH_ARCHIVE, storedArchive);
        properties.setValue(PATH_PYTHON_DEPEND,storedPythonDepend);


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

        if ( storedJobType ==null || storedJobType.equals(HopsUtils.SPARK)){
            sparkBtn.setSelected(true);
            if (stored_spDriverMem!=null) driverMem.setText(stored_spDriverMem);
            if (stored_spDriverVC!=null) driverVC.setText(stored_spDriverVC);
            if (stored_spExecMem!=null) executorMem.setText(stored_spExecMem);
            if (stored_spExecVC!=null) execVC.setText(stored_spExecVC);
            if (stored_spNumExec!=null) numExecutor.setText(stored_spNumExec);
        } else if (storedJobType.equals(HopsUtils.PYTHON)) {
            pythonBtn.setSelected(true);
            if (stored_pyMemory!=null) memory.setText(stored_pyMemory);
            if (stored_pyCpuCore!=null) cpuCore.setText(stored_pyCpuCore);
        } else  {
            flinkBtn.setSelected(true);
            if (stored_flJobManMem!=null) jobManagerMem.setText(stored_flJobManMem);
            if (stored_flNumTaskMan!=null) numTaskManager.setText(stored_flNumTaskMan);
            if (stored_flTaskManMem!=null) taskManagerMem.setText(stored_flTaskManMem);
            if (stored_flNumSlots!=null) numSlots.setText(stored_flNumSlots);
        }

        advanceBtn.setSelected(isAdvancedConfig);

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
