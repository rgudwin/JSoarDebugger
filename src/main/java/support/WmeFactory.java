/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.HashMap;
import org.jsoar.kernel.memory.Wme;

/**
 *
 * @author rgudwin
 */
public class WmeFactory {
    
    static HashMap<String,WmeNode> repository = new HashMap<>();
    
    public static WmeNode createWmeNode(String name, int node_type, Object element, int typeIcon) {
        WmeNode checkIfAvailable = repository.get(name);
        if (checkIfAvailable != null) return(checkIfAvailable);
        else {
           WmeNode novo = new WmeNode(name,node_type,element,typeIcon);
           return(novo);
        }
    }
    
    public static WmeNode createWmeNode(String name, String value, int node_type, Object element, int typeIcon) {
        WmeNode checkIfAvailable = repository.get(name);
        if (checkIfAvailable != null) return(checkIfAvailable);
        else {
           WmeNode novo = new WmeNode(name,value,node_type,element,typeIcon);
           return(novo);
        }
    }
    
    public static WmeNode createWmeNode(String name, int icon_type) {
        WmeNode checkIfAvailable = repository.get(name);
        if (checkIfAvailable != null) return(checkIfAvailable);
        else {
           WmeNode novo = new WmeNode(name,icon_type);
           return(novo);
        }
    }
    
}
