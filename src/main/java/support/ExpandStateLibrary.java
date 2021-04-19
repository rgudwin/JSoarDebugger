/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.HashMap;
import representation.WMTreeNode;

/**
 *
 * @author rgudwin
 */
public class ExpandStateLibrary {
    
    static HashMap<String,Boolean> repository = new HashMap<>();
    
    public static boolean alreadyExists(String key) {
        Boolean expandstate = repository.get(key);
        if (expandstate != null) return(true);
        else return(false);
    }
    
    public static Boolean get(String identifier, String attribute, String value) {
        String key = identifier+"."+attribute+"."+value;
        Boolean retorno = repository.get(key);
        if (retorno == null) {
            set(key,false);
            return(false);
        }
        else return(retorno);
    }
    
    public static Boolean get(WmeNode node) {
        WmeNode parent = (WmeNode) node.getParent();
        String identifier;
        if (parent != null) identifier = ((TreeElement)parent.getUserObject()).getName();
        else identifier = "";
        String attribute = ((TreeElement)node.getUserObject()).getName();
        String value = ((TreeElement)node.getUserObject()).getValue();
        return(get(identifier,attribute,value));
    }
    
    public static Boolean get(WMTreeNode node) {
        WMTreeNode parent = (WMTreeNode) node.getParent();
        String identifier;
        if (parent != null) identifier = ((TreeElement)parent.getUserObject()).getName();
        else identifier = "";
        String attribute = ((TreeElement)node.getUserObject()).getName();
        String value = ((TreeElement)node.getUserObject()).getValue();
        return(get(identifier,attribute,value));
    }
    
    public static void set(String identifier, String attribute, String value, boolean expandstate) {
        String key = identifier+"."+attribute+"."+value;
        set(key,expandstate);
    }
    
    public static void set(String key, boolean expandstate) {
        if (alreadyExists(key)) repository.replace(key, expandstate);
        repository.put(key,expandstate);
    }
    
    public static void set(WmeNode node,boolean expandstate) {
        WmeNode parent = (WmeNode) node.getParent();
        String identifier;
        if (parent != null) identifier = ((TreeElement)parent.getUserObject()).getName();
        else identifier = "";
        String attribute = ((TreeElement)node.getUserObject()).getName();
        String value = ((TreeElement)node.getUserObject()).getValue();
        set(identifier,attribute,value,expandstate);
    }
    
    public static void set(WMTreeNode node,boolean expandstate) {
        WMTreeNode parent = (WMTreeNode) node.getParent();
        String identifier;
        if (parent != null) identifier = ((TreeElement)parent.getUserObject()).getName();
        else identifier = "";
        String attribute = ((TreeElement)node.getUserObject()).getName();
        String value = ((TreeElement)node.getUserObject()).getValue();
        set(identifier,attribute,value,expandstate);
    }
    
}
