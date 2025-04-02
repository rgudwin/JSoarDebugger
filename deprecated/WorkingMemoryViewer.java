/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import Soar.SoarEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.jsoar.kernel.memory.Wme;
import org.jsoar.kernel.symbols.Identifier;
import org.jsoar.kernel.symbols.Symbol;

/**
 *
 * @author rgudwin
 */
public class WorkingMemoryViewer extends javax.swing.JFrame {

    /**
     * Creates new form WorkingMemoryViewer
     */
    public WorkingMemoryViewer() {
        initComponents();
    }
    
    CopyOnWriteArrayList<Identifier> wog;
    SoarEngine sb;
    CopyOnWriteArrayList<Identifier> repr;

    /**
     * Creates new form WorkingMemoryViewer
     */
    public WorkingMemoryViewer(String windowName, SoarEngine sbo) {
        initComponents();
        sb = sbo;
        wog = new CopyOnWriteArrayList(sb.getGoalStates());
        TreeModel tm = createTreeModel(wog);
        jTree1 = new JTree(tm);
        collapseAllNodes(jTree1);
        jScrollPane1.setViewportView(jTree1);
        jTree1.setCellRenderer(new RendererJTree());
        setTitle(windowName);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WorkingMemoryViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WorkingMemoryViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WorkingMemoryViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WorkingMemoryViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
           NativeUtils.loadFileFromJar("/soar-rules.soar");
        } catch(Exception e) {
            e.printStackTrace();
        }   
        String soarRulesPath = "soar-rules.soar";
        //Environment e = new Environment(Boolean.FALSE);
        SoarEngine soarEngine = new SoarEngine(soarRulesPath,true);
        WorkingMemoryViewer ov = new WorkingMemoryViewer("Teste",soarEngine);
        ov.setVisible(true);
        System.out.println("Teste:");
        ov.updateTree(soarEngine.getGoalStates());
    }
  
    public void setWO(CopyOnWriteArrayList<Identifier> newwog) {
        wog = newwog;
    }
    
    private DefaultMutableTreeNode addRootNode(String rootNodeName) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeElement(rootNodeName, TreeElement.NODE_NORMAL, null, TreeElement.ICON_CONFIGURATION));
        return(root);
    }
    
    public boolean equals(Identifier ido, Identifier ii) {
        return(ii.toString().equalsIgnoreCase(ido.toString()));
    }
    
    public DefaultMutableTreeNode genIdNode(Identifier ido, String attr) {
        DefaultMutableTreeNode idNode = new DefaultMutableTreeNode(new TreeElement(attr+ " [" + ido.toString()+"]", TreeElement.NODE_NORMAL, ido.toString(), TreeElement.ICON_OBJECT)); 
        return(idNode);
    }
    
    public DefaultMutableTreeNode genFinalIdNode(Identifier ido, String attr) {
        DefaultMutableTreeNode idNode = new DefaultMutableTreeNode(new TreeElement(attr+ " [<font color=red>" + ido.toString()+"</font>]", TreeElement.NODE_NORMAL, ido.toString(), TreeElement.ICON_OBJECT2)); 
        return(idNode);
    }
    
    public DefaultMutableTreeNode genValNode(Symbol a, Symbol v) {
        DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(new TreeElement(a.toString()+": "+v.toString(), TreeElement.NODE_NORMAL, a.toString()+": "+v.toString(), TreeElement.ICON_QUALITYDIM));
        return(valueNode);
    }
    
    private boolean isIdentifierFinal(Identifier ido) {
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                return true;
            }
        }
        return false;
    }

    private DefaultMutableTreeNode getIdNode(Identifier ido, String attr) {
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                DefaultMutableTreeNode idNode = genFinalIdNode(ido,attr);
                //System.out.println("Já está na lista ... "+ido.toString());                
                return idNode;
            }
        }
        repr.add(ido);
        DefaultMutableTreeNode idNode = genIdNode(ido,attr);
        return(idNode);
    }
    
    private DefaultMutableTreeNode addIdentifier(Identifier ido, String attr) {
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                DefaultMutableTreeNode idNode = genFinalIdNode(ido,attr);
                //System.out.println("Já está na lista ... "+ido.toString());                
                return idNode;
            }
        }
        repr.add(ido);
        DefaultMutableTreeNode idNode = genIdNode(ido,attr);
        Iterator<Wme> It = ido.getWmes();
        while (It.hasNext()) {
            Wme wme = It.next();
            Identifier idd = wme.getIdentifier();
            Symbol a = wme.getAttribute();
            Symbol v = wme.getValue();
            Identifier testv = v.asIdentifier();
            if (testv != null) { // v is an identifier
               String preference = "";
               if (wme.isAcceptable()) preference = " +";
               if (idd.toString().equalsIgnoreCase(testv.toString()))
                   System.out.println("Peguei algo estranho aqui");
               DefaultMutableTreeNode part = addIdentifier(testv,a.toString()+preference);
               idNode.add(part); 
            }
            else { // v is a value 
               DefaultMutableTreeNode valueNode = genValNode(a,v);
               idNode.add(valueNode);
            } 
        }    
        return(idNode);    
    }
    
    private Map<Identifier,String> addWME(DefaultMutableTreeNode idNode, Wme wme) {
        Map<Identifier,String> toBeFurtherProcessed = new HashMap<Identifier,String>();
        Identifier idd = wme.getIdentifier();
        Symbol a = wme.getAttribute();
        Symbol v = wme.getValue();
        System.out.println("Processando "+idd+" "+a+" "+v);
        Identifier testv = v.asIdentifier();
        if (testv != null) { // v is an identifier
            if (isIdentifierFinal(testv)) {
                String preference = "";
                if (wme.isAcceptable()) preference = " +";
                DefaultMutableTreeNode newidNode = genFinalIdNode(testv,a.toString()+preference);
                idNode.add(newidNode);
            }
            else {
               String preference = "";
               if (wme.isAcceptable()) preference = " +";
               if (idd.toString().equalsIgnoreCase(testv.toString()))
                   System.out.println("Peguei algo estranho aqui");
               toBeFurtherProcessed.put(testv,a.toString()+preference);
               //System.out.println("To be further processed "+toBeFurtherProcessed);
               DefaultMutableTreeNode part = addIdentifier2(testv,a.toString()+preference);
               idNode.add(part); 
            }   
        }
        else { // v is a value 
               DefaultMutableTreeNode valueNode = genValNode(a,v);
               idNode.add(valueNode);
            }
        return(toBeFurtherProcessed);    
    }
    
    private Map<Identifier,String> processStep(DefaultMutableTreeNode idNode, Identifier ido, String attr) {
        Map<Identifier,String> toBeFurtherProcessed = new HashMap<Identifier,String>();
        Iterator<Wme> It = ido.getWmes();
        System.out.println("---------------------------");
        while (It.hasNext()) {
            Wme wme = It.next();
            Map<Identifier,String> tbfp = addWME(idNode,wme);
            //toBeFurtherProcessed.add(tbfp);
        }
        return(toBeFurtherProcessed);
    }
    
    private DefaultMutableTreeNode addIdentifier2(Identifier ido, String attr) {
        Map<Identifier,String> toBeFurtherProcessed = new HashMap<Identifier,String>();
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                DefaultMutableTreeNode idNode = genFinalIdNode(ido,attr);
                //System.out.println("Já está na lista ... "+ido.toString());                
                return idNode;
            }
        }
        repr.add(ido);
        DefaultMutableTreeNode idNode = genIdNode(ido,attr);
        do {
            toBeFurtherProcessed = processStep(idNode,ido,attr);
            for (Map.Entry<Identifier,String> e : toBeFurtherProcessed.entrySet()) {
                Identifier id_ = e.getKey();
                String attr_ = e.getValue();
                DefaultMutableTreeNode part = addIdentifier2(id_,attr_);
                idNode.add(part); 
            }
        } while (toBeFurtherProcessed.size() > 0);
        repr = new CopyOnWriteArrayList<Identifier>();
        return(idNode);    
    }
    
    public TreeModel createTreeModel(List<Identifier> wo) {
        DefaultMutableTreeNode root = addRootNode("Root");
        repr = new CopyOnWriteArrayList<Identifier>();
        for (Identifier ii : wo) {
            DefaultMutableTreeNode o = addIdentifier2(ii,"State");
            root.add(o);
        }
        TreeModel tm = new DefaultTreeModel(root);
        return(tm);
    }
    
    public void printStates() {
        List<Identifier> li = sb.getGoalStates();
        System.out.println("********* Getting states ... ***********");
        for (Identifier ii : li)
            System.out.println(ii.toString());
        System.out.println("*****************************************");
    }
    
    public void printWme(Wme wme) {
        Identifier idd = wme.getIdentifier();
        Symbol a = wme.getAttribute();
        Symbol v = wme.getValue();
        String preference = "";
        if (wme.isAcceptable()) preference = " +";
        System.out.println("("+idd.toString()+" ^"+a.toString()+" "+v.toString()+")"+preference);
    }
   
    private void expandAllNodes(JTree tree) {
         expandAllNodes(tree, 0, tree.getRowCount());
    }
    
    private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
       for(int i=startingIndex;i<rowCount;++i){
          tree.expandRow(i);
       }
       if(tree.getRowCount()!=rowCount){
          expandAllNodes(tree, rowCount, tree.getRowCount());
       }
    }
    
    private void collapseAllNodes(JTree tree) {
       int row = tree.getRowCount() - 1;
       while (row >= 0) {
          tree.collapseRow(row);
          row--;
       }
       tree.expandRow(0);
       row = tree.getRowCount() - 1;
       while (row >= 0) {
          tree.expandRow(row);
          row--;
       }
    }
 
    public void updateTree(List<Identifier> wo) {
       TreeModel tm = createTreeModel(wo);
       jTree1.setModel(tm);
       collapseAllNodes(jTree1);
    }
    
    public void updateTree() {
       wog = new CopyOnWriteArrayList(sb.getGoalStates());
       updateTree(wog);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        zoom_in = new javax.swing.JButton();
        zoom_out = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        zoom_in.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/zoom-in-icon.png"))); // NOI18N
        zoom_in.setFocusable(false);
        zoom_in.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoom_in.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoom_in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoom_inActionPerformed(evt);
            }
        });
        jToolBar1.add(zoom_in);

        zoom_out.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/zoom-out-icon.png"))); // NOI18N
        zoom_out.setFocusable(false);
        zoom_out.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoom_out.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoom_out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoom_outActionPerformed(evt);
            }
        });
        jToolBar1.add(zoom_out);

        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void zoom_inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoom_inActionPerformed
        // TODO add your handling code here:
        expandAllNodes(jTree1);
    }//GEN-LAST:event_zoom_inActionPerformed

    private void zoom_outActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoom_outActionPerformed
        // TODO add your handling code here:
        collapseAllNodes(jTree1);
    }//GEN-LAST:event_zoom_outActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton zoom_in;
    private javax.swing.JButton zoom_out;
    // End of variables declaration//GEN-END:variables
}
