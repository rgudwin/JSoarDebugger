/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import support.ExpandStateLibrary;
import support.TreeElement;

/**
 *
 * @author rgudwin
 */
public class WMTreeNode extends DefaultMutableTreeNode {
    
    static CopyOnWriteArrayList<WMNode> repr = new CopyOnWriteArrayList();
    
    public WMTreeNode() {
        super(new TreeElement("State", TreeElement.NODE_NORMAL, "State", TreeElement.ICON_MIND));
    }
    
    public WMTreeNode(String name, int node_type, Object element, int typeIcon) {
        super(new TreeElement(name,node_type,element,typeIcon));
    }
    
    public WMTreeNode(String name, String value, int node_type, Object element, int typeIcon) {
        super(new TreeElement(name,value,node_type,element,typeIcon));
    }
    
    public WMTreeNode(String name, int icon_type) {
        super(new TreeElement(name, TreeElement.NODE_NORMAL, name, icon_type));
    }
    
    public WMTreeNode addRootNode(String rootNodeName) {
        WMNode rootWM = new WMNode(rootNodeName);
        WMTreeNode root = new WMTreeNode(rootNodeName, TreeElement.NODE_NORMAL, rootWM, TreeElement.ICON_CONFIGURATION);
        return(root);
    }
    
    public WMTreeNode genIdNode(WMNode ido) {
        WMTreeNode idNode = new WMTreeNode(ido.getName()+ " [" + ido.getValue().toString()+"]", TreeElement.NODE_NORMAL, ido, TreeElement.ICON_OBJECT); 
        return(idNode);
    }
    
    public WMTreeNode genFinalIdNode(WMNode ido) {
        WMTreeNode idNode = new WMTreeNode(ido.getName()+ " [<font color=red>" + ido.getValue().toString()+"</font>]", TreeElement.NODE_NORMAL, ido, TreeElement.ICON_OBJECT2); 
        return(idNode);
    }
    
    public WMTreeNode genValNode(WMNode node) {
        WMTreeNode valueNode = new WMTreeNode(node.getName()+": "+node.getValue().toString(), TreeElement.NODE_NORMAL, node, TreeElement.ICON_QUALITYDIM);
        return(valueNode);
    }
    
//    public WMTreeNode genValNode2(String a, String v) {
//        WMTreeNode valueNode = new WMTreeNode(a+": "+v, TreeElement.NODE_NORMAL, a, TreeElement.ICON_QUALITYDIM);
//        return(valueNode);
//    }
    
    public WMTreeNode getIdNode(WMNode node) {
        // IF the ido is already in the list, just return it
        for (WMNode ii : repr) {
            if (equals(node,ii)) {
                WMTreeNode idNode = genFinalIdNode(node);
                //System.out.println("getIdNode: Já está na lista ... "+ido.toString());                
                return idNode;
            }
        }
        // ELSE, first add it to the list
        repr.add(node);
        // THEN generate a new node for it
        WMTreeNode idNode;
        if (node.isType(0)) idNode = genIdNode(node);
        else idNode = genFinalIdNode(node);
        return(idNode);
    }
    
    public WMTreeNode addWMNode(WMNode n) {
        if (n.isType(0) || n.isType(2)) { // n is an identifier
                WMTreeNode part = addIdentifier(n);
                return part; 
            }
            else { // v is a value 
               WMTreeNode valueNode = addValue(n);
               return valueNode;
            } 
    }
    
    int recursion = 0;
    public WMTreeNode addIdentifier(WMNode node) {
        recursion++;
        WMTreeNode idNode = getIdNode(node);
        for (WMNode n : node.getL()) {
            if (n.isType(0) || n.isType(2)) { // n is an identifier
                WMTreeNode part = addIdentifier(n);
               idNode.add(part); 
            }
            else { // v is a value 
               WMTreeNode valueNode = genValNode(n);
               idNode.add(valueNode);
            }  
        }
        add(idNode);
        return(idNode);    
    }
    
    public WMTreeNode addValue(WMNode node) {
        WMTreeNode valueNode = genValNode(node);
        add(valueNode);
        return(valueNode); 
    }
    
    // The following methods: restartRootNode, addIdentifier2 and addWME are used in a new way to construct the jTree
    
    // First, the restartRootNode creates a completely new Tree, by adding a root node and the State node, which is the
    // root of the a new Tree
    public WMTreeNode restartRootNode(List<WMNode> lwm) {
        WMTreeNode root = addRootNode("Root");
        repr = new CopyOnWriteArrayList<WMNode>();
        for (WMNode wm : lwm) {
            //recursion=0;
            WMTreeNode child = root.addIdentifier(wm);
            //System.out.println(recursion);
            ExpandStateLibrary.set(child,true);
        }
        
        Runtime.getRuntime().gc();
//        System.out.println("ExpandState: "+ExpandStateLibrary.size());
//        System.out.println("Free memory (MB): " + (Runtime.getRuntime().freeMemory()/1024)/1024);
//        System.out.println("Total memory (MB): " + (Runtime.getRuntime().totalMemory()/1024)/1024);
//        System.out.println("Maximum memory (MB): " + (Runtime.getRuntime().maxMemory()/1024)/1024);
        return root;
    }
    
    public WMTreeNode restartInputLinkNode(WMNode node) {
        WMTreeNode root = addRootNode(node.getName());
        ExpandStateLibrary.set(root,true);
        for (WMNode wm : node.getL()) {
            WMTreeNode child = root.addWMNode(wm);
            ExpandStateLibrary.set(child,true);
        }
        TreeElement oldrootte = (TreeElement)root.getUserObject();
        oldrootte.setElement(node);
        return root;
    }
    
//    public WMTreeNode addIdentifier2(WMNode node) {
//        List<WMNodeToBeCreated> toBeCreated = new ArrayList<WMNodeToBeCreated>();
//        List<WMNodeToBeCreated> toBeFurtherProcessed = new ArrayList<WMNodeToBeCreated>();
//        WMTreeNode idNode = getIdNode(node);
//        toBeCreated.add(new WMNodeToBeCreated(idNode));
//        List<WMNodeToBeCreated> nextList;
//        do {
//            nextList = new ArrayList<WMNodeToBeCreated>();
//            for (WMNodeToBeCreated wme : toBeCreated) {
//                toBeFurtherProcessed = processStep(wme.parent,wme.newId,wme.attrib);
//                for (WMNodeToBeCreated e : toBeFurtherProcessed) {
//                    Identifier id_ = e.newId;
//                    String attr_ = e.attrib;
//                    WMTreeNode part = getIdNode(id_,attr_);
//                    wme.parent.add(part);
//                    nextList.add(new WMNodeToBeCreated(part,id_,attr_));
//                }
//            }
//            toBeCreated = nextList;
//        } while (nextList.size() > 0);    
//        repr = new CopyOnWriteArrayList<Identifier>();
//        return(idNode);    
//    }
    
//    public List<WMNodeToBeCreated> addWME(WMTreeNode idNode, Wme wme) {
//        List<WMNodeToBeCreated> toBeFurtherProcessed = new ArrayList<WMNodeToBeCreated>();
//        Identifier idd = wme.getIdentifier();
//        Symbol a = wme.getAttribute();
//        Symbol v = wme.getValue();
//        Identifier testv = v.asIdentifier();
//        if (testv != null) { // v is an identifier
//            // if the identifier is final I can safely introduce it in the tree
//            if (isIdentifierFinal(testv) || idd.toString().equalsIgnoreCase(testv.toString()) ) {
//                String preference = "";
//                if (wme.isAcceptable()) preference = " +";
//                WMTreeNode newidNode = genFinalIdNode(testv,a.toString()+preference);
//                idNode.add(newidNode);
//            }
//            else { // mark the identifier to be further processed
//               String preference = "";
//               if (wme.isAcceptable()) preference = " +";
//               if (idd.toString().equalsIgnoreCase(testv.toString()))
//                   System.out.println("WME auto-recursivo detectado: ("+idd.toString()+" "+a.toString()+" "+v.toString()+")");
//               toBeFurtherProcessed.add(new WMNodeToBeCreated(null,testv,a.toString()+preference));
//            }   
//        }
//        else { // v is a value 
//               WMTreeNode valueNode = genValNode(a,v);
//               idNode.add(valueNode);
//            }
//        return(toBeFurtherProcessed);    
//    }
    
    
//    void printOperator(Wme wme) {
//        String preference = "";
//        if (wme.isAcceptable()) preference = " +";
//        if (wme.getAttribute().toString().startsWith("operator")) System.out.println("("+wme.getIdentifier()+" "+wme.getAttribute()+" "+wme.getValue()+preference+")");
//    }
    
    // This method scans the identifier ido and finds the new children to be created, inserting nodes for values and final children
//    private List<WMNodeToBeCreated> processStep(WMTreeNode idNode, Identifier ido, String attr) {    
//        List<WMNodeToBeCreated> toBeFurtherProcessed = new ArrayList();
//        // Verify all WMEs using ido as an identifier
//        Iterator<Wme> It = ido.getWmes();
//        while (It.hasNext()) {
//            Wme wme = It.next();
//            // Insert nodes for values and final children ... returns the id nodes in the list for further processing
//            List<WMNodeToBeCreated> tbfp = addWME(idNode,wme);
//            toBeFurtherProcessed.addAll(tbfp);
//        }
//        return(toBeFurtherProcessed);
//    }
    
    public boolean equals(WMNode ido, WMNode ii) {
        String s1 = ido.getName()+ido.getValue().toString();
        String s2 = ii.getName()+ii.getValue().toString();
        return(s1.toString().equalsIgnoreCase(s2.toString()));
    }
    
//    private boolean isIdentifierFinal(WMNode ido) {
//        for (WMNode ii : repr) {
//            if (equals(ido,ii)) {
//                return true;
//            }
//        }
//        return false;
//    }
    
    public TreeElement getTreeElement() {
        return((TreeElement)this.getUserObject());
    }
    
    public boolean isExpanded() {
        return(getTreeElement().expand);
    }
    
    @Override
    public void add(MutableTreeNode newChild)
    {
        WMTreeNodeComparator comparator = new WMTreeNodeComparator();
        super.add(newChild);
        if (comparator != null)
        {
            Collections.sort(this.children,comparator);
        }
    }
    
}
