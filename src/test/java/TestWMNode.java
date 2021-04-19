/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Test;
import representation.WMNode;

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
        
    }
    
}
