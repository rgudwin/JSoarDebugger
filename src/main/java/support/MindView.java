/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import Soar.SoarEngine;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import org.jsoar.kernel.memory.Wme;
import representation.WMNode;
import representation.WMPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author gudwin
 */
public class MindView extends javax.swing.JFrame implements Observer {
    
    int debugstate = 1;
    public SoarEngine sb;
    ImageIcon pause_icon = new ImageIcon(getClass().getResource("/images/pause-icon.png")); 
    ImageIcon play_icon = new ImageIcon(getClass().getResource("/images/play-icon.png"));
    //WorkingMemoryViewer wmi;
    public WmeEditor wmi;
    WMPanel ilpanel;
    WMPanel olpanel;
    WMPanel wmpanel;
    RSyntaxTextArea editor;
    String rulesPath;

    /**
     * Creates new form MindView
     */
    public MindView(String soarRulesPath) {
        rulesPath = soarRulesPath;
        initComponents();
        sb = new SoarEngine(soarRulesPath,false);
        this.setTitle("JSOAR Debugger");
        jSplitPane1.setDividerLocation(0.5);
        jSplitPane1.setResizeWeight(.5d);
        currentPhase.setHorizontalAlignment(JTextField.CENTER);
        //wmi = new WorkingMemoryViewer("SOAR Working Memory",sb);
        WMNode rootNode = new WMNode("Root",sb.getInitialState(),0);
        //wmi = new WmeEditor(rootNode,this,false);
        //wmi.setTitle("SOAR Working Memory");
        pack();
        currentRuleFile.setText(soarRulesPath);
        WMNode il = new WMNode("InputLink",sb.inputLink.toString(),0);
        wmpanel = new WMPanel(rootNode,sb,false);
        wmpanel.updateTree();
        jTabbedPane1.removeAll();
        jTabbedPane1.addTab("Working Memory:", wmpanel);
        jTabbedPane1.revalidate();
        jTabbedPane2.removeAll();
        ilpanel = new WMPanel(il,sb,true);
        jTabbedPane2.addTab("Input Link:", ilpanel);
        jTabbedPane2.revalidate();
        WMNode ol = new WMNode("OutputLink",sb.output_link_string,0);
        jTabbedPane3.removeAll();
        olpanel = new WMPanel(ol,sb,false);
        jTabbedPane3.addTab("Output Link:",olpanel);
        jTabbedPane3.revalidate();
        editor = new RSyntaxTextArea(20, 60);
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LISP);
        editor.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(editor);
        String rules = loadRules(soarRulesPath);
        editor.setText(rules);
        jTabbedPane1.addTab("Rules", sp);
        jTabbedPane1.revalidate();
        setPhaseIndication();
    }
    
    public String loadRules(String filename) {
        File logFile = new File(filename);
        BufferedReader reader=null;
        String line;
        String output="";
        try {
            reader = new BufferedReader(new FileReader(logFile));
            while ((line = reader.readLine()) != null) {
                output += line+"\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }    
        return(output);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar2 = new javax.swing.JToolBar();
        jLabel3 = new javax.swing.JLabel();
        currentRuleFile = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        startstop = new javax.swing.JButton();
        mstep = new javax.swing.JButton();
        step = new javax.swing.JButton();
        mexpand = new javax.swing.JButton();
        mcollapse = new javax.swing.JButton();
        mfind = new javax.swing.JButton();
        msendtowm = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        currentPhase = new javax.swing.JTextField();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        ilPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        input_link = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        olPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        output_link = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        wmPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taBig = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        mFile = new javax.swing.JMenu();
        mLoad = new javax.swing.JMenuItem();
        mLoadEditor = new javax.swing.JMenuItem();
        mSaveEditor = new javax.swing.JMenuItem();
        mReload = new javax.swing.JMenuItem();
        mExit = new javax.swing.JMenuItem();
        mDebug = new javax.swing.JMenu();
        mStart = new javax.swing.JMenuItem();
        mStop = new javax.swing.JMenuItem();
        mWatch = new javax.swing.JMenuItem();
        mEdit = new javax.swing.JMenu();
        ilEdit = new javax.swing.JMenuItem();
        ilLoad = new javax.swing.JMenuItem();
        ilSave = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        mPrintAllWMEs = new javax.swing.JMenuItem();
        mESL = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar2.setFloatable(false);
        jToolBar2.setAlignmentY(0.0F);
        jToolBar2.setBorderPainted(false);

        jLabel3.setText(" Rules File: ");
        jToolBar2.add(jLabel3);

        currentRuleFile.setText(" ");
        jToolBar2.add(currentRuleFile);

        jToolBar1.setRollover(true);

        startstop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pause-icon.png"))); // NOI18N
        startstop.setToolTipText("Play/Pause");
        startstop.setFocusable(false);
        startstop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startstop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        startstop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startstopActionPerformed(evt);
            }
        });
        jToolBar1.add(startstop);

        mstep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/skip-forward-icon.png"))); // NOI18N
        mstep.setToolTipText("micro-step");
        mstep.setEnabled(false);
        mstep.setFocusable(false);
        mstep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mstep.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mstep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mstepActionPerformed(evt);
            }
        });
        jToolBar1.add(mstep);

        step.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/forward-icon.png"))); // NOI18N
        step.setToolTipText("step");
        step.setEnabled(false);
        step.setFocusable(false);
        step.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        step.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        step.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepActionPerformed(evt);
            }
        });
        jToolBar1.add(step);

        mexpand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/zoom-in-icon.png"))); // NOI18N
        mexpand.setFocusable(false);
        mexpand.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mexpand.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mexpand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mexpandActionPerformed(evt);
            }
        });
        jToolBar1.add(mexpand);

        mcollapse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/zoom-out-icon.png"))); // NOI18N
        mcollapse.setFocusable(false);
        mcollapse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mcollapse.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mcollapse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mcollapseActionPerformed(evt);
            }
        });
        jToolBar1.add(mcollapse);

        mfind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/binoculars.png"))); // NOI18N
        mfind.setFocusable(false);
        mfind.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mfind.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(mfind);

        msendtowm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/redo-icon.png"))); // NOI18N
        msendtowm.setFocusable(false);
        msendtowm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        msendtowm.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        msendtowm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msendtowmActionPerformed(evt);
            }
        });
        jToolBar1.add(msendtowm);

        jPanel3.setPreferredSize(new java.awt.Dimension(355, 44));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        jToolBar1.add(jPanel3);

        jPanel4.setMaximumSize(new java.awt.Dimension(110, 44));
        jPanel4.setMinimumSize(new java.awt.Dimension(110, 44));
        jPanel4.setPreferredSize(new java.awt.Dimension(110, 44));

        jLabel4.setText("Current Phase:");

        currentPhase.setText("Current Phase: <HALT>");
        currentPhase.setMaximumSize(new java.awt.Dimension(50, 19));
        currentPhase.setMinimumSize(new java.awt.Dimension(50, 19));
        currentPhase.setPreferredSize(new java.awt.Dimension(50, 19));
        currentPhase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentPhaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 3, Short.MAX_VALUE))
            .addComponent(currentPhase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentPhase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jToolBar1.add(jPanel4);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane1.setDividerLocation(450);

        jScrollPane2.setViewportView(input_link);

        javax.swing.GroupLayout ilPanelLayout = new javax.swing.GroupLayout(ilPanel);
        ilPanel.setLayout(ilPanelLayout);
        ilPanelLayout.setHorizontalGroup(
            ilPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        );
        ilPanelLayout.setVerticalGroup(
            ilPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Input Link:", ilPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jScrollPane3.setViewportView(output_link);

        javax.swing.GroupLayout olPanelLayout = new javax.swing.GroupLayout(olPanel);
        olPanel.setLayout(olPanelLayout);
        olPanelLayout.setHorizontalGroup(
            olPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        );
        olPanelLayout.setVerticalGroup(
            olPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Output Link: ", olPanel);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );

        jSplitPane1.setRightComponent(jPanel2);

        jSplitPane2.setLeftComponent(jSplitPane1);

        taBig.setColumns(20);
        taBig.setRows(5);
        jScrollPane1.setViewportView(taBig);

        javax.swing.GroupLayout wmPanelLayout = new javax.swing.GroupLayout(wmPanel);
        wmPanel.setLayout(wmPanelLayout);
        wmPanelLayout.setHorizontalGroup(
            wmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 939, Short.MAX_VALUE)
            .addGroup(wmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE))
        );
        wmPanelLayout.setVerticalGroup(
            wmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
            .addGroup(wmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Working Memory:", wmPanel);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1))
        );

        jSplitPane2.setRightComponent(jPanel5);

        mFile.setText("File");

        mLoad.setText("Load");
        mLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadActionPerformed(evt);
            }
        });
        mFile.add(mLoad);

        mLoadEditor.setText("Load Editor");
        mLoadEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mLoadEditorActionPerformed(evt);
            }
        });
        mFile.add(mLoadEditor);

        mSaveEditor.setText("Save Editor");
        mSaveEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSaveEditorActionPerformed(evt);
            }
        });
        mFile.add(mSaveEditor);

        mReload.setText("Reload");
        mReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mReloadActionPerformed(evt);
            }
        });
        mFile.add(mReload);

        mExit.setText("Exit");
        mExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mExitActionPerformed(evt);
            }
        });
        mFile.add(mExit);

        jMenuBar1.add(mFile);

        mDebug.setText("Debug");

        mStart.setText("Start");
        mStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mStartActionPerformed(evt);
            }
        });
        mDebug.add(mStart);

        mStop.setText("Stop");
        mStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mStopActionPerformed(evt);
            }
        });
        mDebug.add(mStop);

        mWatch.setText("Watch");
        mWatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mWatchActionPerformed(evt);
            }
        });
        mDebug.add(mWatch);

        jMenuBar1.add(mDebug);

        mEdit.setText("InputLink");

        ilEdit.setText("Edit");
        ilEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ilEditActionPerformed(evt);
            }
        });
        mEdit.add(ilEdit);

        ilLoad.setText("Load");
        ilLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ilLoadActionPerformed(evt);
            }
        });
        mEdit.add(ilLoad);

        ilSave.setText("Save");
        ilSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ilSaveActionPerformed(evt);
            }
        });
        mEdit.add(ilSave);

        jMenuBar1.add(mEdit);

        jMenu1.setText("Support");

        mPrintAllWMEs.setText("Print All WMEs");
        mPrintAllWMEs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mPrintAllWMEsActionPerformed(evt);
            }
        });
        jMenu1.add(mPrintAllWMEs);

        mESL.setText("Print ExpandStateLibrary");
        mESL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mESLActionPerformed(evt);
            }
        });
        jMenu1.add(mESL);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mStopActionPerformed
        stopDebugState();
    }//GEN-LAST:event_mStopActionPerformed

    private void mStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mStartActionPerformed
        startDebugState();  
    }//GEN-LAST:event_mStartActionPerformed

    private void mExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mExitActionPerformed

    private void mstepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mstepActionPerformed
        mstepDebugState();
    }//GEN-LAST:event_mstepActionPerformed

    private void startstopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startstopActionPerformed
//        if (debugstate == 0) {
//           startDebugState();
//        }
//        else {
//           stopDebugState();   
//        }
        stepDebugState();
    }//GEN-LAST:event_startstopActionPerformed

    private void stepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepActionPerformed
        // TODO add your handling code here:
        cycleDebugState();
    }//GEN-LAST:event_stepActionPerformed

    private void mWatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mWatchActionPerformed
        // TODO add your handling code here:
        //wmi = new WorkingMemoryViewer("SOAR Working Memory",sb);
        WMNode rootNode = new WMNode("Root",sb.getInitialState(),0);
        wmi = new WmeEditor(rootNode,this,false);
        Date date = new Date(); 
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentdate = formatter.format(date);
        wmi.setTitle("SOAR Working Memory "+currentdate);
        wmi.updateTree();
        wmi.setVisible(true);
    }//GEN-LAST:event_mWatchActionPerformed

    private void LoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadActionPerformed
        // TODO add your handling code here:
        //Create a file chooser
    JFileChooser fc = new JFileChooser(rulesPath);
    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            rulesPath = file.getAbsolutePath();
            //This is where a real application would open the file.
            //System.out.println("Opening: " + file.getAbsolutePath());
            //wmi.setVisible(false);
            sb = new SoarEngine(file.getAbsolutePath(),false);
            currentRuleFile.setText(file.getAbsolutePath());
            //wmi = new WorkingMemoryViewer("SOAR Working Memory",sb);
            WMNode rootNode = new WMNode("Root",sb.getInitialState(),0);
            this.wmpanel.restartPanel(rootNode,sb);
            //System.out.println(wmpanel.getRoot().toStringFull());
            setPhaseIndication();
            //wmi = new WmeEditor(rootNode,this,false);
            //wmi.setTitle("SOAR Working Memory");
            //wmi.updateTree();
            //wmi.setVisible(true);
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }//GEN-LAST:event_LoadActionPerformed

    private void ilEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ilEditActionPerformed
        WMNode rootNode = new WMNode("InputLink",sb.getInitialState(),0);
        WmeEditor e = new WmeEditor(rootNode,this,true);
        e.setVisible(true);
    }//GEN-LAST:event_ilEditActionPerformed

    private void mReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mReloadActionPerformed
        sb = new SoarEngine(currentRuleFile.getText(),false);
        //wmi.dispose();
        WMNode rootNode = new WMNode("Root",sb.getInitialState(),0);
        this.wmpanel.restartPanel(rootNode,sb);
        setPhaseIndication();
        //wmi = new WmeEditor(rootNode,this,false);
        //wmi.setTitle("SOAR Working Memory");
        //wmi.updateTree();
        //wmi.setVisible(true);
    }//GEN-LAST:event_mReloadActionPerformed

    private void mPrintAllWMEsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mPrintAllWMEsActionPerformed
        Set<Wme> allwmes = sb.getWorkingMemory();
        Iterator<Wme> It = allwmes.iterator();
        String all = "";
        while(It.hasNext()) {
            Wme w = It.next();
            String acc = (w.isAcceptable())?" +":"";
            all += "("+w.getIdentifier().toString()+" "+w.getAttribute().toString()+" "+w.getValue()+acc+")\n";
        }
        InfoPanel ip = new InfoPanel();
        ip.setText(all);
        Date date = new Date(); 
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentdate = formatter.format(date);
        ip.setTitle("All WMEs in SOAR "+currentdate);
        ip.setVisible(true);
    }//GEN-LAST:event_mPrintAllWMEsActionPerformed

    private void currentPhaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentPhaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_currentPhaseActionPerformed

    private void mexpandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mexpandActionPerformed
        // TODO add your handling code here:
        wmpanel.expandAllNodes();
    }//GEN-LAST:event_mexpandActionPerformed

    private void mcollapseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mcollapseActionPerformed
        // TODO add your handling code here:
        wmpanel.collapseAllNodes();
    }//GEN-LAST:event_mcollapseActionPerformed

    private void msendtowmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msendtowmActionPerformed
        WMNode root = ilpanel.getRoot();
        sb.createInputLink(root, sb.inputLink);
        String wm = sb.stringInputLink();
        wmpanel.updateTree();
        setInputLink(wm);
    }//GEN-LAST:event_msendtowmActionPerformed

    private void ilSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ilSaveActionPerformed
        ilpanel.save();
    }//GEN-LAST:event_ilSaveActionPerformed

    private void ilLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ilLoadActionPerformed
        ilpanel.load();
        ilpanel.updateTree2();
    }//GEN-LAST:event_ilLoadActionPerformed

    private void mESLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mESLActionPerformed
        InfoPanel ip = new InfoPanel();
        ip.setText(ExpandStateLibrary.dump());
        Date date = new Date(); 
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentdate = formatter.format(date);
        ip.setTitle("ExpandStateLibrary "+currentdate);
        ip.setVisible(true);
    }//GEN-LAST:event_mESLActionPerformed

    private void mLoadEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mLoadEditorActionPerformed
       JFileChooser fc = new JFileChooser(rulesPath);
    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            rulesPath = file.getAbsolutePath();
            //This is where a real application would open the file.
            //System.out.println("Opening: " + file.getAbsolutePath());
            //wmi.setVisible(false);
            String rules = loadRules(file.getAbsolutePath());
            editor.setText(rules);
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }//GEN-LAST:event_mLoadEditorActionPerformed

    private void mSaveEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSaveEditorActionPerformed
        File rulesFile = new File(rulesPath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rulesFile, false));
            String s = editor.getText();
            writer.write(s);                
            writer.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_mSaveEditorActionPerformed

    public void update(Observable arg0, Object arg1) {
        this.repaint();
    }
    
    public void step() {
        try {
           sb.step();
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public void cycle() {
        try {
           sb.cycle();
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public int getDebugState() {
        return(debugstate);
    }
    
    public void startDebugState() {
        startstop.setIcon(play_icon); // NOI18N 
        mstep.setEnabled(true);
        step.setEnabled(true);
        setPhaseIndication();
        //wmi.updateTree();
        //wmi.setVisible(true);
        this.wmpanel.updateTree();
        debugstate = 1;
        try {
           //sb.c.stop();
        } catch (Exception e) {
           e.printStackTrace();
        } 
    }
    
    public void stopDebugState() {
        startstop.setIcon(pause_icon); // NOI18N 
        mstep.setEnabled(false);
        step.setEnabled(false);
        stepDebugState();
        setPhaseIndication();
        //wmi.setVisible(false);
        debugstate = 0;
        try {
              //sb.c.start();
        } catch (Exception e) {
              e.printStackTrace();
        }
    }
    
    public void stepDebugState() {
        if (debugstate == 1) {
          try {
            sb.step();
            set_input_link_text();
            set_output_link_text();
            setPhaseIndication();
            //wmi.updateTree();
            //wmi.setVisible(true);
            wmpanel.updateTree();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
    }
    
    public void cycleDebugState() {
        if (debugstate == 1) {
          try {
            sb.cycle();
            set_input_link_text();
            set_output_link_text();
            setPhaseIndication();
            //wmi.updateTree();
            //wmi.setVisible(true);
            wmpanel.updateTree();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
    } 
    
    public void mstepDebugState() {
        if (debugstate == 1) {
          try {
            sb.mstep();
            set_input_link_text();
            set_output_link_text();
            setPhaseIndication();
            //System.out.println("Starting updateTree de WMI");
            //wmi.updateTree();
            //System.out.println("Finishing updateTree de WMI");
            //wmi.setVisible(true);
            wmpanel.updateTree();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
    }
    
    public void setPhaseIndication() {
        if (sb.phase == -1)
               currentPhase.setText("<HALT>");
        else if (sb.phase == 0)
               currentPhase.setText("<INPUT>"); 
        else if (sb.phase == 1)
               currentPhase.setText("<PROPOSE>");
        else if (sb.phase == 2)
               currentPhase.setText("<DECISION>");
        else if (sb.phase == 3)
               currentPhase.setText("<APPLY>");
        else if (sb.phase == 4)
               currentPhase.setText("<OUTPUT>"); 
        else if (sb.phase == 5)
               currentPhase.setText("<HALT>"); 
    }
    
    public void setInputLink(String text) {
        input_link.setText(text);
    }
    
    public void setOutputLink(String text) {
        output_link.setText(text);
        WMNode ol = sb.getOutputLink();
        System.out.println(ol.toStringFull());
        
    }
    
    public void set_input_link_text() {
        String text;
        text = sb.stringInputLink();
        input_link.setText(text);
        WMNode node = ilpanel.getRoot();
        //System.out.println(node.toStringFull());
        sb.setInputLink(node);
        wmpanel.updateTree();
    }
    
    public void set_output_link_text() {
        String text;
        text = sb.stringOutputLink();
        output_link.setText(text);
        System.out.println(sb.reasonForStop());
        WMNode decision = sb.getOutputLink();
        //System.out.println(decision.toStringFull());
        olpanel.setRoot(decision);
        olpanel.updateTree2();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField currentPhase;
    private javax.swing.JTextField currentRuleFile;
    private javax.swing.JMenuItem ilEdit;
    private javax.swing.JMenuItem ilLoad;
    private javax.swing.JPanel ilPanel;
    private javax.swing.JMenuItem ilSave;
    private javax.swing.JTextPane input_link;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenu mDebug;
    private javax.swing.JMenuItem mESL;
    private javax.swing.JMenu mEdit;
    private javax.swing.JMenuItem mExit;
    private javax.swing.JMenu mFile;
    private javax.swing.JMenuItem mLoad;
    private javax.swing.JMenuItem mLoadEditor;
    private javax.swing.JMenuItem mPrintAllWMEs;
    private javax.swing.JMenuItem mReload;
    private javax.swing.JMenuItem mSaveEditor;
    private javax.swing.JMenuItem mStart;
    private javax.swing.JMenuItem mStop;
    private javax.swing.JMenuItem mWatch;
    private javax.swing.JButton mcollapse;
    private javax.swing.JButton mexpand;
    private javax.swing.JButton mfind;
    private javax.swing.JButton msendtowm;
    private javax.swing.JButton mstep;
    private javax.swing.JPanel olPanel;
    private javax.swing.JTextPane output_link;
    private javax.swing.JButton startstop;
    private javax.swing.JButton step;
    private javax.swing.JTextArea taBig;
    private javax.swing.JPanel wmPanel;
    // End of variables declaration//GEN-END:variables
}
