/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import org.jsoar.kernel.symbols.Identifier;

/**
 *
 * @author rgudwin
 */
public class WmeToBeCreated {
    public WmeNode parent;
    public String attrib;
    public Identifier newId;
    
    public WmeToBeCreated(WmeNode nparent, Identifier nnewId, String nattrib) {
        parent = nparent;
        attrib = nattrib;
        newId = nnewId;
    }
}
