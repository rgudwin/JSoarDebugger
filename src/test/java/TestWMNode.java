/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Soar.SoarEngine;
import java.util.Date;
import javax.swing.tree.DefaultMutableTreeNode;
import org.junit.Test;
import representation.WMNode;
import representation.WMPanel;

/**
 *
 * @author rgudwin
 */
public class TestWMNode {
    
    @Test 
    public void testWMNode() {
        WMNode ln = new WMNode("a");
        WMNode ln2 = new WMNode("b");
        WMNode ln3 = new WMNode("c");
        ln.add(ln2);
        ln2.add(ln3);
        WMNode v1 = new WMNode("v","3");
        ln.add(v1);
        ln2.add(v1);
        ln3.add(v1);
        System.out.println(ln.toStringFull());
        Date d = new Date();
        WMNode complexnode = new WMNode("teste");
        complexnode.addObject(d,"date");
        DefaultMutableTreeNode dt = new DefaultMutableTreeNode(d);
        complexnode.addObject(dt, "defaultMutableTreeNode");
        complexnode.addObject(complexnode,"recursion");
        WMPanel wmp = new WMPanel(new WMNode("Root","[S1]",0),new SoarEngine("rules/soar-rules.soar",false),true);
        complexnode.addObject(wmp, "wmpanel");
        System.out.println("Finished creation of objects");
        System.out.println(complexnode.toStringFull());
        
        
    }
    
}
