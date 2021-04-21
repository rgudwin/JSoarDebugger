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
public interface WmeEditorListener {
    
    public void notifyRootChange(Identifier newAO);
    
}
