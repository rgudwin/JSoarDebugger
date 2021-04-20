/*******************************************************************************
 * Copyright (c) 2012  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * Contributors:
 *     K. Raizer, A. L. O. Paraense, R. R. Gudwin - initial API and implementation
 ******************************************************************************/
package support;

import Soar.SoarEngine;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import representation.WMNode;
import representation.WMTreeNode;

/**
 *
 * @author rgudwin
 */
public class WmeEditor extends javax.swing.JFrame {
    
    //Identifier root;
    WMNode root;
    List<WmeEditorListener> listeners;
    SoarEngine sb = null;
    WMTreeNode rootlink;
    boolean editable;
    MindView mv;

    /**
     * Creates new form WmeEditor
     */
    public WmeEditor(WMNode rootId, MindView mv, boolean editable) {
        //root = rootId;
        
        this.sb = mv.sb;
        this.mv = mv;
        this.editable = editable;
        if (rootId != null) root = rootId;
        else root = new WMNode("InputLink","I2",0);
        listeners = new ArrayList<WmeEditorListener>();
        initComponents();
        if (editable == false) send.setEnabled(false);
        jsp.setViewportView(jtree);
        setTitle("InputLink Editor");
        //Identifier id = sb.inputLink;
        //WMNode il = new WMNode("InputLink",id.toString(),0);
        // "InputLink ["+sb.inputLink.toString()+"]"
        rootlink = new WMTreeNode(root.getName(),root.getValue().toString(),TreeElement.NODE_NORMAL,root,TreeElement.ICON_MIND);
        //inputlink.getTreeElement().setExpand(true);
        ExpandStateLibrary.set(rootlink,true);
        DefaultTreeModel tm = new DefaultTreeModel(rootlink);
        jtree.setModel(tm);
        jtree.setCellRenderer(new RendererJTree());
        
        MouseListener ml;
        ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = jtree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = jtree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1 && e.getButton() == 3) {
                        WMTreeNode tn = (WMTreeNode)selPath.getLastPathComponent();
                        TreeElement te = (TreeElement)tn.getUserObject();
                        WMTreeNode parentnode = (WMTreeNode)tn.getParent();
                        TreeElement parent=null;
                        Object element=null;
                        if (parentnode != null) {
                            parent = (TreeElement)(parentnode).getUserObject();
                            element = parent.getElement();
                        }
                        element = te.getElement();
                        WMNode node = (WMNode) element;
                        if (node.isType(1)) {
                            JPopupMenu popup = new JPopupMenu();
                            JMenuItem jm1 = new JMenuItem("Edit Value");
                            ActionListener al = new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    editValue(tn);
                                }
                            };
                            jm1.addActionListener(al);
                            JMenuItem jm5 = new JMenuItem("Delete this Value");
                            ActionListener al5;
                            al5 = new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    deleteComponent(tn);
                                }
                            };
                            jm5.addActionListener(al5);
                            popup.add(jm1);
                            popup.add(jm5);
                            popup.show(jtree, e.getX(), e.getY());
                        }
                        else if (node.isType(0) || node.isType(1)) {
                            //Identifier p = (Identifier) te.getElement();
                            JPopupMenu popup = new JPopupMenu();
                            JMenuItem jm1 = new JMenuItem("Edit Value");
                            ActionListener al = new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    editIdentifier(tn);
                                }
                            };
                            jm1.addActionListener(al);
                            JMenuItem jm2 = new JMenuItem("Add child Identifier");
                            ActionListener al2 = new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    createIdentifier(tn);
                                }
                            };
                            jm2.addActionListener(al2);
                            JMenuItem jm3 = new JMenuItem("Add child value");
                            ActionListener al3;
                            al3 = new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    createValue(tn);
                                }
                            };
                            jm3.addActionListener(al3);
                            JMenuItem jm4 = new JMenuItem("Delete this Identifier");
                            ActionListener al4;
                            al4 = new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    deleteComponent(tn);
                                }
                            };
                            jm4.addActionListener(al4);
                            popup.add(jm1);
                            popup.add(jm2);
                            popup.add(jm3);
                            popup.add(jm4);
                            popup.show(jtree, e.getX(), e.getY());
                        }
                    }
                }
            }};
        if (editable) jtree.addMouseListener(ml);
    }
    
    public void addListener(WmeEditorListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners() {
        for (WmeEditorListener listener : listeners) {
            //listener.notifyRootChange(root);
        }
    }
    
    private void createIdentifier(WMTreeNode parent) {
        TreeElement.reset();
        WMTreeNode newao = DialogFactory.getIdentifier(parent,sb);
        //newao.getTreeElement().setExpand(true);
        //System.out.println("inputLink "+sb.inputLink);
        parent.add(newao);
        ExpandStateLibrary.set(newao,true);
        TreeModel tm = new DefaultTreeModel(rootlink);
        jtree.setModel(tm);
        restoreExpansion(jtree);
    }
    
    private void createValue(WMTreeNode parent) {
        TreeElement.reset();
        WMTreeNode newao = DialogFactory.getValue(parent,sb);
        //newao.getTreeElement().setExpand(true);
        //System.out.println("inputLink "+sb.inputLink);
        parent.add(newao);
        ExpandStateLibrary.set(newao,true);
        TreeModel tm = new DefaultTreeModel(rootlink);
        jtree.setModel(tm);
        restoreExpansion(jtree);
    }
    
    private void editIdentifier(WMTreeNode node) {
        DialogFactory.editIdentifier(node);
        TreeModel tm = new DefaultTreeModel(rootlink);
        jtree.setModel(tm);
        restoreExpansion(jtree);
    }
    
    private void editValue(WMTreeNode p) {
        DialogFactory.editValue(p);
        TreeModel tm = new DefaultTreeModel(rootlink);
        jtree.setModel(tm);
        restoreExpansion(jtree);
    }
    
    private void deleteComponent(Object parent) {
        if (parent == null) return;
        String parentclass = parent.getClass().getCanonicalName();
        System.out.println("Parent class: "+parentclass);
        //String childclass = child.getClass().getCanonicalName();
        if (parent instanceof WMTreeNode){
            WMTreeNode pnode = (WMTreeNode) parent;
            System.out.println("Trying to remove "+pnode.getTreeElement().getName()+" from its parent");
            WMTreeNode pparent = (WMTreeNode) pnode.getParent();
            WMNode wmparent = (WMNode)pparent.getTreeElement().getElement();
            WMNode wmtoberemoved = (WMNode) pnode.getTreeElement().getElement();
            pnode.removeFromParent();
            if (pparent != null) System.out.println("Now trying to identify the parent WMNode: "+wmparent.toString());
            else System.out.println("Unfortunately, its parent is null .....");
            wmparent.getL().remove(wmtoberemoved);
            TreeModel tm = new DefaultTreeModel(rootlink);
            jtree.setModel(tm);
            restoreExpansion(jtree);
        }
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
        search = new javax.swing.JButton();
        send = new javax.swing.JButton();
        jsp = new javax.swing.JScrollPane();
        jtree = new javax.swing.JTree();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mLoad = new javax.swing.JMenuItem();
        mSave = new javax.swing.JMenuItem();
        mClose = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setFloatable(false);
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

        search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/binoculars.png"))); // NOI18N
        search.setFocusable(false);
        search.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        search.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        jToolBar1.add(search);

        send.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/redo-icon.png"))); // NOI18N
        send.setFocusable(false);
        send.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        send.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });
        jToolBar1.add(send);

        jtree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                jtreeTreeExpanded(evt);
            }
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
                jtreeTreeCollapsed(evt);
            }
        });
        jsp.setViewportView(jtree);

        jMenu1.setText("File");

        mLoad.setText("Load");
        mLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mLoadActionPerformed(evt);
            }
        });
        jMenu1.add(mLoad);

        mSave.setText("Save");
        mSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSaveActionPerformed(evt);
            }
        });
        jMenu1.add(mSave);

        mClose.setText("Close");
        mClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCloseActionPerformed(evt);
            }
        });
        jMenu1.add(mClose);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
            .addComponent(jsp)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void zoom_inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoom_inActionPerformed
        expandAllNodes(jtree);
    }//GEN-LAST:event_zoom_inActionPerformed

    private void zoom_outActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoom_outActionPerformed
        collapseAllNodes(jtree);
    }//GEN-LAST:event_zoom_outActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        // TODO add your handling code here:

        //� so um teste!
//        TreeElement.reset();
//        DefaultMutableTreeNode ao = addIdentifier(root, true);
//        TreeModel ttm = new DefaultTreeModel(ao);
//        jtree.setModel(ttm);
//        expandAllNodes(jtree);
//
//        String nameNode = null;
//        nameNode = JOptionPane.showInputDialog("Node Name:");
//        DefaultMutableTreeNode root = (DefaultMutableTreeNode) jtree.getModel().getRoot();
//        List<DefaultMutableTreeNode> ol = find(root, nameNode);
//        for (DefaultMutableTreeNode o : ol) {
//            ((TreeElement) o.getUserObject()).setColor(TreeElement.NODE_CHANGE);
//        }
//
//        //� so um teste!
//        TreeModel tm = new DefaultTreeModel(root);
//        jtree.setModel(tm);
//        expandAllNodes(jtree);


    }//GEN-LAST:event_searchActionPerformed

    private void mSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSaveActionPerformed
        String filename = "";
        try {JFileChooser chooser = new JFileChooser();
             if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                  filename = chooser.getSelectedFile().getCanonicalPath();
             }
             if (!filename.equals("")) {
                File logFile = new File(filename);
	        BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
                String s = this.buildFromWMTreeNode(rootlink);
                writer.write(s);                
                writer.close();
                //System.out.println("Wrote to "+filename);
                //System.out.println(s);
             }
        } catch (Exception e) { e.printStackTrace(); }
    }//GEN-LAST:event_mSaveActionPerformed

    private void mCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_mCloseActionPerformed

    private void mLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mLoadActionPerformed
        root = load();
        updateTree2();
        //System.out.println(root.toStringFull());
    }//GEN-LAST:event_mLoadActionPerformed

    private void jtreeTreeCollapsed(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jtreeTreeCollapsed
        collapse(evt);
    }//GEN-LAST:event_jtreeTreeCollapsed

    private void jtreeTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jtreeTreeExpanded
        expanded(evt);
    }//GEN-LAST:event_jtreeTreeExpanded

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
        sb.createInputLink(root, sb.inputLink);
        String wm = sb.stringInputLink();
        mv.wmi.updateTree();
        mv.setInputLink(wm);
    }//GEN-LAST:event_sendActionPerformed
    
    private void expanded(TreeExpansionEvent evt) {
        TreePath tp = evt.getPath();
        Object[] path = tp.getPath();
        for (Object t : path) {
            if (t instanceof WMTreeNode) {
                WMTreeNode n = (WMTreeNode) t;
                TreeElement e = (TreeElement)n.getUserObject();
                //e.setExpand(true);
                ExpandStateLibrary.set(n, true);
                //System.out.println(e.getName());
            }
        }
    }
    
    private void collapse(TreeExpansionEvent evt) {
        TreePath tp = evt.getPath();
        Object[] path = tp.getPath();
        for (Object t : path) {
            if (t instanceof WMTreeNode) {
                WMTreeNode n = (WMTreeNode) t;
                TreeElement e = (TreeElement)n.getUserObject();
                if (t != path[path.length-1]) ExpandStateLibrary.set(n, true);//e.setExpand(true);
                else {
                    //e.setExpand(false);
                    ExpandStateLibrary.set(n, false);
                    //System.out.println("Setting "+e.getName()+" to collapse");
                }
                //System.out.println(e.getName());
            }
        }
    }
    
    private void treatExpansionOrCollapse(TreeExpansionEvent evt) {
        TreePath tp = evt.getPath();
        JTree tree = (JTree) evt.getSource(); 
        TreeModel tm = tree.getModel();
        WMTreeNode rootNode = (WMTreeNode)tm.getRoot();
        Object[] path = tp.getPath();
        for (Object t : path) {
            if (t instanceof TreeElement) {
                TreeElement e = (TreeElement) t;
            } else if (t instanceof WMTreeNode) {
                WMTreeNode n = (WMTreeNode) t;
                TreeElement e = (TreeElement)n.getUserObject();
            }
        }    
    }
    
    public enum mode {NULL, LINK, VALUE}
    
    private int getLevel(String splitted[]) {
        int level = 0;
        for (int i=0;i<splitted.length;i++) {
            if (splitted[i].equals("")) level++;
        }
        return(level/3);
    }
    
    private mode getMode(String splitted[]) {
        mode m = mode.NULL;
        for (int i=0;i<splitted.length;i++) {
            if (splitted[i].equals("*")) {
                m = mode.LINK;
                break;
            }
            if (splitted[i].equals("-")) {
                m = mode.VALUE;
                break;
            }
        }
        return(m);
    }
    
    private String getName(String splitted[]) {
        int level = getLevel(splitted);
        String almost = splitted[3*level+1];
        String last = almost.split(":")[0];
        return(last);
    }
    
    private String getValue(String splitted[]) {
        String value = "";
        for (int i=0;i<splitted.length;i++) {
            if (splitted[i].endsWith(":")) {
                value = splitted[i+1];
                break;
            }
        }    
        return(value);    
    }
    
    private WMNode load() {
        //boolean result = false;
        String line;
        String filename = "";
        WMNode newAO = null;
        List<WMNode> parseAOs = new ArrayList<WMNode>();
        try {JFileChooser chooser = new JFileChooser();
             if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                  filename = chooser.getSelectedFile().getCanonicalPath();
             }
             if (!filename.equals("")) {
                File logFile = new File(filename);
	        BufferedReader reader = new BufferedReader(new FileReader(logFile));
                while ((line = reader.readLine()) != null) {
                    String linesplitted[] = line.split(" ");
                    int level = getLevel(linesplitted);
                    mode m = getMode(linesplitted);
                    String name = getName(linesplitted);
                    String value = getValue(linesplitted);
                    WMNode ao;
                    WMNode father;
                    if (level == 0) {
                        newAO = new WMNode(name);
                        parseAOs.add(newAO);
                    }
                    else {
                          switch(m) {
                                case LINK:ao = new WMNode(name);
                                               parseAOs.add(ao);
                                               father = (WMNode) parseAOs.get(level-1);
                                               father.add(ao);
                                               if (level >= parseAOs.size()) parseAOs.add(ao);
                                               else parseAOs.set(level, ao);
                                               break;
                                case VALUE:WMNode qd = new WMNode(name,value);
                                           father = (WMNode) parseAOs.get(level-1);
                                           father.add(qd);
                                           break;
                          }                            
                    }
                }
                notifyListeners();
                reader.close();
             }
        } catch (Exception e) { e.printStackTrace(); }
        return(newAO);
    }
    
    private void expandNode(JTree tree, WMTreeNode node) {
        //System.out.println("Expanding node "+node.getTreeElement().getName()+" that has expand "+node.getTreeElement().getExpand());
        TreePath tp = new TreePath(node.getPath());
        tree.expandPath(tp);
        ExpandStateLibrary.set(node, true);
    }
    
    private void collapseNode(JTree tree, WMTreeNode node) {
        //System.out.println("Collapsing node "+node.getTreeElement().getName()+" that has expand "+node.getTreeElement().getExpand());
        TreePath tp = new TreePath(node.getPath());
        tree.collapsePath(tp);
        ExpandStateLibrary.set(node, false);
    }
    
    private void restoreExpansion(WMTreeNode wn, JTree tree) {
        //if (wn.isExpanded()) expandNode(tree,wn);
        if (ExpandStateLibrary.get(wn)) expandNode(tree,wn);
        else collapseNode(tree,wn);
        int childnumber = wn.getChildCount();
        for (int i=0;i<childnumber;i++) {
            WMTreeNode child = (WMTreeNode) wn.getChildAt(i);
            //if (child.isExpanded()) restoreExpansion(child,tree);
            if (ExpandStateLibrary.get(child)) restoreExpansion(child,tree);
        }
    }
    
    private void restoreExpansion(JTree tree) { 
        TreeModel tm = tree.getModel();
        WMTreeNode rootNode = (WMTreeNode)tm.getRoot();
        restoreExpansion(rootNode,tree);
    }
       
    private void expandAllNodes(JTree tree) {
         expandAllNodes(tree, 0, tree.getRowCount());
    }
    
    private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
       for(int i=startingIndex;i<rowCount;++i){
          tree.expandRow(i);
          WMTreeNode node = (WMTreeNode)tree.getPathForRow(i).getLastPathComponent();
          //((TreeElement)node.getUserObject()).setExpand(true);
          ExpandStateLibrary.set(node, true);
          //System.out.println("Expanding node "+((TreeElement)node.getUserObject()).getName());
       }
       if(tree.getRowCount()!=rowCount){
          expandAllNodes(tree, rowCount, tree.getRowCount());
       }
    }
    
    private void collapseAllNodes(JTree tree) {
       int row = tree.getRowCount() - 1;
       while (row >= 0) {
          tree.collapseRow(row);
          WMTreeNode node = (WMTreeNode)tree.getPathForRow(row).getLastPathComponent();
          //((TreeElement)node.getUserObject()).setExpand(false);
          ExpandStateLibrary.set(node,false);
          //System.out.println("Collapsing node "+((TreeElement)node.getUserObject()).getName());
          row--;
       }
       //tree.expandRow(0);
       row = tree.getRowCount() - 1;
       while (row >= 0) {
          tree.expandRow(row);
          WMTreeNode node = (WMTreeNode)tree.getPathForRow(row).getLastPathComponent();
          //((TreeElement)node.getUserObject()).setExpand(true);
          ExpandStateLibrary.set(node,true);
          //System.out.println("Expanding node "+((TreeElement)node.getUserObject()).getName());
          row--;
       }
    }
    
    private List<DefaultMutableTreeNode> findObject(DefaultMutableTreeNode root, Object obj, AtomicReference<String> path) {
        List<DefaultMutableTreeNode> results = new ArrayList<>();
        AtomicReference<String> rootPath = new AtomicReference<>(path.get());
        for (Enumeration children = root.children(); children.hasMoreElements(); ) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            if (((TreeElement) child.getUserObject()).getElement() == obj) {
                results.add(child);
            }
        }
        if (!results.isEmpty()) {
            int dot = path.get().lastIndexOf(".");
            path.set(path.get().substring(0, (dot < 0 ? 0 : dot)));
        }
        for (Enumeration children = root.children(); children.hasMoreElements(); ) {
            AtomicReference<String> rootPath2 = new AtomicReference<>(rootPath.get());
            List<DefaultMutableTreeNode> result = findObject((DefaultMutableTreeNode) children.nextElement(), obj, rootPath2);
            if (!result.isEmpty()) {
                results.addAll(result);
                int dot = rootPath2.get().lastIndexOf(".");
                rootPath2.set(rootPath2.get().substring(0, (dot < 0 ? 0 : dot)));
                if (path.get().equals(rootPath.get()) || path.get().length() < rootPath2.get().length()) {
                    path.set(rootPath2.get());
                }
            }
        }
        if (!results.isEmpty() && !path.get().isEmpty() && !path.get().equals(rootPath.get())) {
            results.add(0, root);
        }
        return results;
    }
    
    
    private List<DefaultMutableTreeNode> find(DefaultMutableTreeNode root, String s) {    
        
        if (s != null) {
            //Wme aoRoot = (Wme) ((TreeElement) root.getUserObject()).getElement();
            List<Object> results = null;//aoRoot.search(s);
            List<DefaultMutableTreeNode> treeResults = new ArrayList<>();
            for (Object result : results) {
                if (((TreeElement) root.getUserObject()).getElement() == result) {
                    treeResults.add(root);
                } else {
                    List<DefaultMutableTreeNode> treeResult = findObject(root, result, new AtomicReference<>(s));
                    treeResults.addAll(treeResult);
                }
            }
            return treeResults;
        } else {
            return new ArrayList<>();
        }
        
        
        /*DefaultMutableTreeNode root2 = root;
        
        Enumeration<DefaultMutableTreeNode> e = root2.depthFirstEnumeration();
        List<TreePath> listPath = new ArrayList<>();
        TreePath raffledPath = null;
        int cont = 0;
        
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = e.nextElement();
             TreeElement te = (TreeElement)node.getUserObject();
                        
            if (te.getName().toString().equalsIgnoreCase(s)) {
                cont++;
                listPath.add(new TreePath(node.getPath()));
                
                //return new TreePath(node.getPath());
            }
        }
        
        if( listPath.size()> 0){
            Random r = new Random();
            raffledPath = listPath.get(r.nextInt(listPath.size()));

            DefaultMutableTreeNode tn = (DefaultMutableTreeNode) raffledPath.getLastPathComponent();
            TreeElement te = (TreeElement) tn.getUserObject();
            te.setColor(TreeElement.NODE_CHANGE);
            
            
            
        
        }
       
        
        System.out.println("Achei: "+cont);
       // return raffledPath;
       return root2; */
        
    }
    
    public void updateTree2() {
       rootlink = rootlink.restartInputLinkNode(root);
       System.out.println(root.toStringFull());
       TreeModel tm = new DefaultTreeModel(rootlink);
       ExpandStateLibrary.set(rootlink, true);
       jtree.setModel(tm);
       restoreExpansion(jtree);
    }
    
    // This method creates a complete new Tree in the Editor
    public void updateTree() {
       rootlink = rootlink.restartRootNode(sb.getWM());
       TreeModel tm = new DefaultTreeModel(rootlink);
       ExpandStateLibrary.set(rootlink, true);
       jtree.setModel(tm);
       restoreExpansion(jtree);
    }
    
    public WMNode addWMTreeNode(WMTreeNode node) {
        TreeElement te = node.getTreeElement();
        if (te.getIcon() == TreeElement.ICON_OBJECT || te.getIcon() == TreeElement.ICON_OBJECT2 || 
            te.getIcon() == TreeElement.ICON_CONFIGURATION || te.getIcon() == TreeElement.ICON_MIND ) {
            WMNode ln = new WMNode(te.getName());
            int numberofchildren = node.getChildCount();
            for (int i=0;i<numberofchildren;i++) {
                WMTreeNode c = (WMTreeNode) node.getChildAt(i);
                WMNode e = addWMTreeNode(c);
                ln.add(e);
            }
            return(ln);
        }
        else {
            String parse[] = te.getName().split(": ");
            WMNode vn = new WMNode(parse[0],parse[1]);
            return(vn);
        }
    }
    
    public String buildFromWMTreeNode(WMTreeNode rootwme) {
        WMNode e = addWMTreeNode(rootwme);
        return(e.toStringFull());
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JScrollPane jsp;
    private javax.swing.JTree jtree;
    private javax.swing.JMenuItem mClose;
    private javax.swing.JMenuItem mLoad;
    private javax.swing.JMenuItem mSave;
    private javax.swing.JButton search;
    private javax.swing.JButton send;
    private javax.swing.JButton zoom_in;
    private javax.swing.JButton zoom_out;
    // End of variables declaration//GEN-END:variables
}
