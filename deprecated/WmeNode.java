/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.jsoar.kernel.memory.Wme;
import org.jsoar.kernel.symbols.Identifier;
import org.jsoar.kernel.symbols.Symbol;

/**
 *
 * @author rgudwin
 */
public class WmeNode extends DefaultMutableTreeNode {
    
    CopyOnWriteArrayList<Identifier> repr = new CopyOnWriteArrayList();
    
    public WmeNode() {
        super(new TreeElement("State", TreeElement.NODE_NORMAL, "State", TreeElement.ICON_MIND));
    }
    
    public WmeNode(String name, int node_type, Object element, int typeIcon) {
        super(new TreeElement(name,node_type,element,typeIcon));
    }
    
    public WmeNode(String name, String value, int node_type, Object element, int typeIcon) {
        super(new TreeElement(name,value,node_type,element,typeIcon));
    }
    
    public WmeNode(String name, int icon_type) {
        super(new TreeElement(name, TreeElement.NODE_NORMAL, name, icon_type));
    }
    
    public WmeNode addRootNode(String rootNodeName) {
        WmeNode root = new WmeNode(rootNodeName, TreeElement.NODE_NORMAL, null, TreeElement.ICON_CONFIGURATION);
        return(root);
    }
    
    public WmeNode genIdNode(Identifier ido, String attr) {
        WmeNode idNode = new WmeNode(attr+ " [" + ido.toString()+"]", TreeElement.NODE_NORMAL, ido, TreeElement.ICON_OBJECT); 
        return(idNode);
    }
    
    public WmeNode genFinalIdNode(Identifier ido, String attr) {
        WmeNode idNode = new WmeNode(attr+ " [<font color=red>" + ido.toString()+"</font>]", TreeElement.NODE_NORMAL, ido, TreeElement.ICON_OBJECT2); 
        return(idNode);
    }
    
    public WmeNode genValNode(Symbol a, Symbol v) {
        WmeNode valueNode = new WmeNode(a.toString()+": "+v.toString(), TreeElement.NODE_NORMAL, a, TreeElement.ICON_QUALITYDIM);
        return(valueNode);
    }
    
    public WmeNode genValNode2(String a, String v) {
        WmeNode valueNode = new WmeNode(a+": "+v, TreeElement.NODE_NORMAL, a, TreeElement.ICON_QUALITYDIM);
        return(valueNode);
    }
    
    public WmeNode getIdNode(Identifier ido, String attr) {
        // IF the ido is already in the list, just return it
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                WmeNode idNode = genFinalIdNode(ido,attr);
                //System.out.println("getIdNode: Já está na lista ... "+ido.toString());                
                return idNode;
            }
        }
        // ELSE, first add it to the list
        repr.add(ido);
        // THEN generate a new node for it
        WmeNode idNode = genIdNode(ido,attr);
        return(idNode);
    }
    
    public WmeNode addIdentifier(Identifier ido, String attr) {
        WmeNode idNode = getIdNode(ido,attr);
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
                   System.out.println("WME auto-recursivo detectado: ("+idd.toString()+" "+a.toString()+" "+v.toString()+")");
               WmeNode part = addIdentifier(testv,a.toString()+preference);
               idNode.add(part); 
            }
            else { // v is a value 
               WmeNode valueNode = genValNode(a,v);
               idNode.add(valueNode);
            } 
        }    
        return(idNode);    
    }
    
    public WmeNode addValue(String attr, String value) {
        WmeNode valueNode = genValNode2(attr,value);
        return(valueNode); 
    }
    
    // The following methods: restartRootNode, addIdentifier2 and addWME are used in a new way to construct the jTree
    
    // First, the restartRootNode creates a completely new Tree, by adding a root node and the State node, which is the
    // root of the a new Tree
    public WmeNode restartRootNode(List<Identifier> wo) {
        WmeNode root = addRootNode("Root");
        repr = new CopyOnWriteArrayList<Identifier>();
        for (Identifier ii : wo) {
            WmeNode o = addIdentifier2(ii,"State");
            root.add(o);
            ExpandStateLibrary.set(o,true);
        }
        return root;
    }
    
    public WmeNode restartInputLinkNode(Identifier inputLink) {
        WmeNode o = addIdentifier2(inputLink,"InputLink");
        ExpandStateLibrary.set(o,true);
        return o;
    }
    
    public WmeNode addIdentifier2(Identifier ido, String attr) {
        List<WmeToBeCreated> toBeCreated = new ArrayList<WmeToBeCreated>();
        List<WmeToBeCreated> toBeFurtherProcessed = new ArrayList<WmeToBeCreated>();
        WmeNode idNode = getIdNode(ido,attr);
        toBeCreated.add(new WmeToBeCreated(idNode,ido,attr));
        List<WmeToBeCreated> nextList;
        do {
            nextList = new ArrayList<WmeToBeCreated>();
            for (WmeToBeCreated wme : toBeCreated) {
                toBeFurtherProcessed = processStep(wme.parent,wme.newId,wme.attrib);
                for (WmeToBeCreated e : toBeFurtherProcessed) {
                    Identifier id_ = e.newId;
                    String attr_ = e.attrib;
                    WmeNode part = getIdNode(id_,attr_);
                    wme.parent.add(part);
                    nextList.add(new WmeToBeCreated(part,id_,attr_));
                }
            }
            toBeCreated = nextList;
        } while (nextList.size() > 0);    
        repr = new CopyOnWriteArrayList<Identifier>();
        return(idNode);    
    }
    
    public List<WmeToBeCreated> addWME(WmeNode idNode, Wme wme) {
        List<WmeToBeCreated> toBeFurtherProcessed = new ArrayList<WmeToBeCreated>();
        Identifier idd = wme.getIdentifier();
        Symbol a = wme.getAttribute();
        Symbol v = wme.getValue();
        Identifier testv = v.asIdentifier();
        if (testv != null) { // v is an identifier
            // if the identifier is final I can safely introduce it in the tree
            if (isIdentifierFinal(testv) || idd.toString().equalsIgnoreCase(testv.toString()) ) {
                String preference = "";
                if (wme.isAcceptable()) preference = " +";
                WmeNode newidNode = genFinalIdNode(testv,a.toString()+preference);
                idNode.add(newidNode);
            }
            else { // mark the identifier to be further processed
               String preference = "";
               if (wme.isAcceptable()) preference = " +";
               if (idd.toString().equalsIgnoreCase(testv.toString()))
                   System.out.println("WME auto-recursivo detectado: ("+idd.toString()+" "+a.toString()+" "+v.toString()+")");
               toBeFurtherProcessed.add(new WmeToBeCreated(null,testv,a.toString()+preference));
            }   
        }
        else { // v is a value 
               WmeNode valueNode = genValNode(a,v);
               idNode.add(valueNode);
            }
        return(toBeFurtherProcessed);    
    }
    
    
    void printOperator(Wme wme) {
        String preference = "";
        if (wme.isAcceptable()) preference = " +";
        if (wme.getAttribute().toString().startsWith("operator")) System.out.println("("+wme.getIdentifier()+" "+wme.getAttribute()+" "+wme.getValue()+preference+")");
    }
    
    // This method scans the identifier ido and finds the new children to be created, inserting nodes for values and final children
    private List<WmeToBeCreated> processStep(WmeNode idNode, Identifier ido, String attr) {    
        List<WmeToBeCreated> toBeFurtherProcessed = new ArrayList();
        // Verify all WMEs using ido as an identifier
        Iterator<Wme> It = ido.getWmes();
        while (It.hasNext()) {
            Wme wme = It.next();
            // Insert nodes for values and final children ... returns the id nodes in the list for further processing
            List<WmeToBeCreated> tbfp = addWME(idNode,wme);
            toBeFurtherProcessed.addAll(tbfp);
        }
        return(toBeFurtherProcessed);
    }
    
    public boolean equals(Identifier ido, Identifier ii) {
        return(ii.toString().equalsIgnoreCase(ido.toString()));
    }
    
    private boolean isIdentifierFinal(Identifier ido) {
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                return true;
            }
        }
        return false;
    }
    
    public TreeElement getTreeElement() {
        return((TreeElement)this.getUserObject());
    }
    
    public boolean isExpanded() {
        return(getTreeElement().expand);
    }
    
    @Override
    public void add(MutableTreeNode newChild)
    {
        WmeNodeComparator comparator = new WmeNodeComparator();
        super.add(newChild);
        if (comparator != null)
        {
            Collections.sort(this.children,comparator);
        }
    }
    
}
