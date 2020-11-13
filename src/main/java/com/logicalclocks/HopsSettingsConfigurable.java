package com.logicalclocks;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

import static com.logicalclocks.HopsPluginUtils.*;


public class HopsSettingsConfigurable implements Configurable {


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
    private JTextArea moreProps  = new JTextArea();
    //job config params
    //private JTextField driverMem  = new JTextField(CONST_2048);
    private JFormattedTextField driverMem  = new JFormattedTextField("1111");

    private JTextField executorMem  = new JTextField(CONST_4096);
    private JTextField execVC  = new JTextField(CONST_1);
    private JTextField driverVC  = new JTextField(CONST_1);
    private JTextField numExecutor  = new JTextField(CONST_1);
    //python configs"4096"
    private JTextField memory  = new JTextField(CONST_2048);
    private JTextField cpuCore  = new JTextField(CONST_1);
    //flink configs
    private JTextField jobManagerMem  = new JTextField(CONST_1024);
    private JTextField numTaskManager = new JTextField(CONST_1);
    private JTextField taskManagerMem  = new JTextField(CONST_1024);
    private JTextField numSlots  = new JTextField(CONST_1);


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
    private static String storedLogFile = null;
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
    LinkedHashMap<String,Component> constantFieldsMap=new LinkedHashMap<String,Component>();
    LinkedHashMap<String,Component> advanceFieldmap=new LinkedHashMap<String,Component>();
    LinkedHashMap<String,Component> sparkConfigMap =new LinkedHashMap<String,Component>();
    LinkedHashMap<String,Component> flinkConfigMap =new LinkedHashMap<String,Component>();
    LinkedHashMap<String,Component> pythonConfigMap=new LinkedHashMap<String,Component>();
    LinkedHashMap<String,Component> sparkAddInputs=new LinkedHashMap<String,Component>();
    LinkedHashMap<String,Component> pythonAddInputs=new LinkedHashMap<String,Component>();
    int layoutVGAP=2;
    private static JSlider slider = new JSlider();

    public HopsSettingsConfigurable(Project project){
        this.project = project;
        if(project!=null){
            loadProperties(project);
        }
    }

    private static void loadProperties(Project project){
        PropertiesComponent properties = PropertiesComponent.getInstance(project);
        storedUrl = properties.getValue(HopsPluginUtils.PATH_URL);
        storedKey = properties.getValue(HopsPluginUtils.PATH_KEY);
        storedProject = properties.getValue(HopsPluginUtils.PATH_PROJECT);
        storedLogFile = properties.getValue(HopsPluginUtils.PATH_FILE);
        storedJob = properties.getValue(HopsPluginUtils.PATH_JOB);
        storedProgram = properties.getValue(HopsPluginUtils.PATH_PROGRAM);
        storedUserArgs=properties.getValue(HopsPluginUtils.PATH_USERARGS);
        storedMainClass=properties.getValue(HopsPluginUtils.PATH_MAINCLASS);
        storedExecId=properties.getValue(HopsPluginUtils.PATH_EXECID);
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
//       numberChecker(driverMem);

        numberChecker(driverVC);
        numberChecker(executorMem);
        numberChecker(execVC);
        numberChecker(cpuCore);
        numberChecker(numExecutor);
        numberChecker(numSlots);
        numberChecker(numTaskManager);
        numberChecker(taskManagerMem);
        numberChecker(jobManagerMem);
        numberChecker(memory);

        //add primary input text field panel
        GridLayoutManager layout = new GridLayoutManager(constantFieldsMap.size() + 1, 2);
        layout.setVGap(layoutVGAP);
        //layout.setHGap(150);
        Insets margin = new Insets(5, 0, 0, 0);
        layout.setMargin(margin);
        inputPanel = new JPanel(layout);
        Border bb = BorderFactory.createEmptyBorder();
        Border br = BorderFactory.createTitledBorder("Primary Inputs");
        inputPanel.setBorder(br);
        inputPanel= HopsPluginUtils.createInputPanel(inputPanel,constantFieldsMap);
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
        sparkBtn.setText(SPARK_BTN_LBL);
        pythonBtn.setText(PYTHON_BTN_LBL);
        flinkBtn.setText(FLINK_BTN_LBL);

        group.add(sparkBtn);
        group.add(pythonBtn);
        group.add(flinkBtn);


        JPanel buttonPanel=new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        //JLabel label = new JLabel("Job Type: ");
        //label.setAlignmentX(Component.LEFT_ALIGNMENT);
        //buttonPanel.add(label);
        buttonPanel.setBorder(BorderFactory.createTitledBorder(JOB_TYPE_LBL));
        sparkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        pythonBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        flinkBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);

        sparkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        pythonBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        flinkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(sparkBtn);
        buttonPanel.add(pythonBtn);
        buttonPanel.add(flinkBtn);

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
        lAdd2.setVGap(layoutVGAP);
        lAdd2.setMargin(margin);
        //lAdd2.setHGap(150);
        additionalInputPanel = new JPanel(lAdd2);
        Border br2 = BorderFactory.createTitledBorder("Job Type Specific Inputs");
        additionalInputPanel.setBorder(br2);
        //job config parameters panel
        GridLayoutManager layout3 = new GridLayoutManager(sparkConfigMap.size() + 1, 2);
        layout3.setVGap(layoutVGAP);
        layout3.setMargin(margin);

        //layout3.setHGap(150);
        jobConfigPanel =new JPanel(layout3);
        Border br3 = BorderFactory.createTitledBorder("Additional Job Configurations");
        jobConfigPanel.setBorder(br3);

        //advance config panel
        GridLayoutManager layout2=new GridLayoutManager(advanceFieldmap.size()+1, 2);
        layout2.setVGap(layoutVGAP);
        layout2.setMargin(margin);
        //layout2.setHGap(150);
        advanceInputPanel=new JPanel(layout2);
        Border br4 = BorderFactory.createTitledBorder("Advanced Configurations");
        advanceInputPanel.setBorder(br4);
        //add checkbox button
        JPanel buttonPanel2=new JPanel();
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2,BoxLayout.X_AXIS));
        advanceBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        advanceBtn.setText(HopsPluginUtils.ADVANCED_LBL);
        if(isAdvancedConfig){
            advanceBtn.setSelected(true);
        } else advanceBtn.setSelected(false);

        advanceBtn.addActionListener(advanceAction);
        buttonPanel2.add(advanceBtn);

        if(storedJobType == null || storedJobType.equals(HopsPluginUtils.SPARK)){
            additionalInputPanel=createInputPanel(additionalInputPanel,sparkAddInputs);
            jobConfigPanel = HopsPluginUtils.createInputPanel(jobConfigPanel, sparkConfigMap);

            if(isAdvancedConfig) {
                advanceInputPanel = HopsPluginUtils.createInputPanel(advanceInputPanel, advanceFieldmap);
                /*JSlider jSlider = new JSlider();
                jSlider.setMinimum(1);
                jSlider.setMaximum(10);
                jSlider.setSnapToTicks(true);
                advanceInputPanel.add(jSlider);*/
            }


        } else if (storedJobType.equals(HopsPluginUtils.PYTHON)){
            additionalInputPanel=createInputPanel(additionalInputPanel,pythonAddInputs);
            jobConfigPanel = HopsPluginUtils.createInputPanel(jobConfigPanel, pythonConfigMap);
            if(isAdvancedConfig)
                advanceInputPanel=createField(advanceInputPanel,0, HopsPluginUtils.FILES_LBL,advanceFieldmap.get(HopsPluginUtils.FILES_LBL));
        } else {
            additionalInputPanel=createInputPanel(additionalInputPanel,new LinkedHashMap<>());
            jobConfigPanel = HopsPluginUtils.createInputPanel(jobConfigPanel, flinkConfigMap);
            if(isAdvancedConfig)
                advanceInputPanel=createField(advanceInputPanel,0, HopsPluginUtils.MORE_PROP_LBL,advanceFieldmap.get(HopsPluginUtils.MORE_PROP_LBL));
        }

        //add all panels
        superPanel = new JPanel();
        superPanel.setLayout(new BoxLayout(superPanel,BoxLayout.Y_AXIS));
        superPanel.setAlignmentX(BoxLayout.X_AXIS);
        superPanel.add(buttonPanel); //job type buttons
        superPanel.add(inputPanel); // primary input fields
        if(!storedJobType.equals(FLINK))
        superPanel.add(additionalInputPanel); // job config specific inputs 2nd panel
        superPanel.add(jobConfigPanel); // job config parameters 3rd panel
        superPanel.add(buttonPanel2); // checkbox
        superPanel.add(advanceInputPanel); // advance configs

        return superPanel;

    }


    public void initPanelMaps(){

        //main input panel

        constantFieldsMap.put(HOPS_PROJECT_LBL,userProject);
        constantFieldsMap.put(HOPS_URL_LBL,userUrl);
        constantFieldsMap.put(HOPS_API_LBL,userKey);
        constantFieldsMap.put(JOB_NAME_LBL,jobName);
        constantFieldsMap.put(LOG_PATH_LBL, logFilePath);
        constantFieldsMap.put(EXEC_ID_LBL,execId);
        /*constantFieldsMap.put(HopsUtils.HDFS_LBL,hdfsPath);
        constantFieldsMap.put(HopsUtils.USER_ARGS_LBL,userArgs);
        constantFieldsMap.put(HopsUtils.MAIN_CLASS_LBL,mainClass);*/

        sparkAddInputs.put(HopsPluginUtils.HDFS_LBL,hdfsPath);
        sparkAddInputs.put(HopsPluginUtils.USER_ARGS_LBL,userArgs);
        sparkAddInputs.put(HopsPluginUtils.MAIN_CLASS_LBL,mainClass);

        pythonAddInputs.put(HopsPluginUtils.HDFS_LBL,hdfsPath);
        pythonAddInputs.put(HopsPluginUtils.USER_ARGS_LBL,userArgs);
        // spark configs
        sparkConfigMap.put(HopsPluginUtils.DRIVER_MEM_LBL,driverMem);
        sparkConfigMap.put(HopsPluginUtils.EXECUTOR_MEM_LBL,executorMem);
        sparkConfigMap.put(HopsPluginUtils.EXEC_VC_LBL,execVC);
        sparkConfigMap.put(HopsPluginUtils.DRIVER_VC_LBL,driverVC);
        sparkConfigMap.put(HopsPluginUtils.NUM_EXEC_LBL,numExecutor);
        slider.setMinimum(0);
        slider.setMaximum(100);
        slider.setSnapToTicks(true);
        slider.setValue(1);
        slider.setOpaque(true);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        //sparkConfigMap.put(DYNAMIC_SLIDER_LBL,slider);
        // python configs
        pythonConfigMap.put(HopsPluginUtils.MEMORY_LBL,memory);
        pythonConfigMap.put(HopsPluginUtils.CPU_LBL,cpuCore);
        // flink configs
        flinkConfigMap.put(HopsPluginUtils.JOB_MANAGER_MM_LBL,jobManagerMem);
        flinkConfigMap.put(HopsPluginUtils.NUM_TASK_LBL, numTaskManager);
        flinkConfigMap.put(HopsPluginUtils.TASK_MANAGER_MM_LBL,taskManagerMem);
        flinkConfigMap.put(HopsPluginUtils.NUM_SLOT_LBL,numSlots);
        //initiliase advance panel
        advanceFieldmap.put(HopsPluginUtils.ARCHIVES_LBL,archives);
        advanceFieldmap.put(HopsPluginUtils.JARS_LBL,additionalJars);
        advanceFieldmap.put(HopsPluginUtils.PYTHON_LBL,pythonDepend);
        advanceFieldmap.put(HopsPluginUtils.FILES_LBL,additionalFiles);
        advanceFieldmap.put(HopsPluginUtils.MORE_PROP_LBL,moreProps);



    }

    public void numberChecker(JTextField field){

        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = field.getText();
                int l = value.length();
                if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9')
                        || ke.getExtendedKeyCode() ==KeyEvent.VK_BACK_SPACE || ke.getExtendedKeyCode()==KeyEvent.VK_ESCAPE) {
                    field.setEditable(true);
                } else {
                    field.setEditable(false);
                }
            }
        });
    }

    /*
    add each  button listners
     */

    ActionListener flinkAction= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==flinkBtn){
                //labels
                int l= HopsSettingsConfigurable.this.inputPanel.getComponents().length;
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
                advanceInputPanel.removeAll();

               // advanceInputPanel.updateUI();
               // jobConfigPanel.updateUI();
               // additionalInputPanel.updateUI();
                superPanel.remove(additionalInputPanel);
                superPanel.updateUI();
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
                int l= HopsSettingsConfigurable.this.inputPanel.getComponents().length;
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
                advanceInputPanel.removeAll();

//                advanceInputPanel.updateUI();
//                jobConfigPanel.updateUI();
//                additionalInputPanel.updateUI();
                superPanel.remove(additionalInputPanel);
                superPanel.add(additionalInputPanel,2);
                superPanel.updateUI();




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
                advanceInputPanel.removeAll();
               //HopsworksConfigurable.this.inputPanel.repaint();
                /*advanceInputPanel.updateUI();
                jobConfigPanel.updateUI();
                additionalInputPanel.updateUI();*/

                superPanel.remove(additionalInputPanel);
                superPanel.add(additionalInputPanel,2);
                superPanel.updateUI();


            }
        }
    };

    ActionListener advanceAction= new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==advanceBtn){
                advanceInputPanel.removeAll();
                if(advanceBtn.isSelected()){

                    if(sparkBtn.isSelected()){
                        advanceInputPanel= HopsPluginUtils.createInputPanel(advanceInputPanel,advanceFieldmap);
//                        Component[] allComps = advanceInputPanel.getComponents();
//                        for (Component c: allComps) {
//                            c.setEnabled(true);
//                        }
                    }else if (pythonBtn.isSelected()){
                        /*HopsworksConfigurable.this.advanceInputPanel.getComponent(6).setEnabled(true);
                        HopsworksConfigurable.this.advanceInputPanel.getComponent(7).setEnabled(true);*/
                        advanceInputPanel=createField(advanceInputPanel,0, HopsPluginUtils.FILES_LBL,advanceFieldmap.get(HopsPluginUtils.FILES_LBL));
                    }else if (flinkBtn.isSelected()){
                     /*   HopsworksConfigurable.this.advanceInputPanel.getComponent(8).setEnabled(true);
                        HopsworksConfigurable.this.advanceInputPanel.getComponent(9).setEnabled(true);*/
                        advanceInputPanel=createField(advanceInputPanel,0, HopsPluginUtils.MORE_PROP_LBL,advanceFieldmap.get(HopsPluginUtils.MORE_PROP_LBL));
                    }
                }
         /*       else { //check box disable
                 *//*   Component[] allComps = advanceInputPanel.getComponents();
                    for (Component c: allComps) {
                        c.setEnabled(false);
                    }*//*

                }*/
            }
            advanceInputPanel.updateUI();

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
        storedLogFile = logFilePath.getText().trim();
        storedJob=jobName.getText().trim();
        storedProgram= hdfsPath.getText().trim();
        storedUserArgs=userArgs.getText().trim();
        storedMainClass=mainClass.getText().trim();
        storedExecId=execId.getText().trim();
        if (sparkBtn.isSelected()) {
            storedJobType = SPARK;
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
            storedJobType = PYTHON;
            stored_pyCpuCore=cpuCore.getText().trim();
            stored_pyMemory=memory.getText().trim();
            properties.setValue(PATH_PY_MEMORY,stored_pyMemory);
            properties.setValue(PATH_PY_CPU_CORE,stored_pyCpuCore);
        }
        else {
            storedJobType = FLINK;
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
        else isAdvancedConfig=false;
        storedAddFile=additionalFiles.getText().trim();
        storedAddJar=additionalJars.getText().trim();
        storedPythonDepend=pythonDepend.getText().trim();
        storedArchive=archives.getText().trim();
        storedMoreProp=moreProps.getText().trim();

        //set into project properties
        properties.setValue(HopsPluginUtils.PATH_URL, storedUrl);
        properties.setValue(HopsPluginUtils.PATH_KEY, storedKey);
        properties.setValue(HopsPluginUtils.PATH_PROJECT, storedProject);
        properties.setValue(HopsPluginUtils.PATH_FILE, storedLogFile);
        properties.setValue(HopsPluginUtils.PATH_JOB, storedJob);
        properties.setValue(HopsPluginUtils.PATH_PROGRAM, storedProgram);
        properties.setValue(HopsPluginUtils.PATH_USERARGS, storedUserArgs);
        properties.setValue(HopsPluginUtils.PATH_MAINCLASS, storedMainClass);
        properties.setValue(HopsPluginUtils.PATH_EXECID, storedExecId);
        properties.setValue(HopsPluginUtils.PATH_JOBTYPE, storedJobType);
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
            logFilePath.setText(storedLogFile);
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

        if ( storedJobType ==null || storedJobType.equals(HopsPluginUtils.SPARK)){
            sparkBtn.setSelected(true);
            if (stored_spDriverMem!=null) driverMem.setText(stored_spDriverMem);
            if (stored_spDriverVC!=null) driverVC.setText(stored_spDriverVC);
            if (stored_spExecMem!=null) executorMem.setText(stored_spExecMem);
            if (stored_spExecVC!=null) execVC.setText(stored_spExecVC);
            if (stored_spNumExec!=null) numExecutor.setText(stored_spNumExec);
        } else if (storedJobType.equals(HopsPluginUtils.PYTHON)) {
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

        if (archives != null) {
            archives.setText(storedArchive);
        }
        if (additionalJars != null) {
            additionalJars.setText(storedAddJar);
        }
        if (additionalFiles != null) {
            additionalFiles.setText(storedAddFile);
        }
        if (pythonDepend != null) {
            pythonDepend.setText(storedPythonDepend);
        }
        if (moreProps != null) {
            moreProps.setText(storedMoreProp);
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