/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author rgudwin
 */
public class WMNode {
    private String name="";
    private Object value="";
    private List<WMNode> l= new CopyOnWriteArrayList<>();
    private int type=0;

    public WMNode() {
        
    }
    
    public WMNode(String name) {
        this.name = name;
    }
    
    public WMNode(String name, Object value, int type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }
    
    public WMNode(String name, Object value) {
        type = 1;
        this.name = name;
        if (value instanceof String) {
            String svalue = (String) value;
            try {
                int ivalue = Integer.parseInt(svalue);
                this.value = ivalue;
            } catch(Exception e) {
                try {
                    double dvalue = Double.parseDouble(svalue);
                    this.value = dvalue;
                } catch(Exception e2) {
                    this.value = svalue;
                }   
            } 
        }
        else this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WMNode> getL() {
        return l;
    }

    public void setL(List<WMNode> l) {
        this.l = l;
    }

    public WMNode add(WMNode node) {
        l.add(node);
        return(node);
    }
    
    public String toString() {
        return(name);
    }
    
    public String toStringFull() {
        return(toStringFull(1));
    }
    
    public String toStringFull(int level) {
        String out; 
        if (isType(1)) {
           out="- ";
           out += name+": "+value+"\n";
           return out; 
        }
        else {
            out="* "+ name+"\n";
            for (WMNode ln : l) {
                for (int i=0;i<level;i++) out += "   ";
                out += ln.toStringFull(level+1);
            }
            return(out);
        }
    }
    
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        type = 1;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public boolean isType(int type) {
        return(this.type==type);
    }
    
    public boolean isDouble() {
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.lang.Double"))
            return(true);
        return(false);   
    }
    
    public boolean isFloat() {
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.lang.Double"))
            return(true);
        return(false);   
    }
    
    public boolean isInteger() {
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.lang.Long"))
            return(true);
        return(false);   
    }
    
    public boolean isLong() {
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.lang.Long"))
            return(true);
        return(false);   
    }
    
    public boolean isNumber() {
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.lang.Float") || objectClassName.equals("java.lang.Double") || objectClassName.equals("java.lang.Integer") || objectClassName.equals("java.lang.Long"))
            return(true);
        return(false);
    }

    public boolean isHashMap(){
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.util.HashMap"))
            return(true);
        return(false);
    }
    
    public boolean isString() {
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.lang.String"))
            return(true);
        return(false);
    }
    
    public boolean isBoolean() {
        String objectClassName = value.getClass().getName();
        if (objectClassName.equals("java.lang.Boolean"))
            return(true);
        return(false);
    }

    public WMNode clone() {
        WMNode newnode;
           newnode = new WMNode(getName(), getValue(), getType());
           newnode.l = new CopyOnWriteArrayList(newnode.l);
        return newnode;
    }
    
    public String getResumedValue() {
        String result; 
        if (isFloat() || isDouble()) {
            result = String.format("%4.1f",getValue());
        }
        else {
            try {
               int trial = Integer.parseInt(getValue().toString());
               result = String.format("%d",trial);
            } catch(Exception ee) {
               try { 
                    double trial = Double.parseDouble(getValue().toString());
                    result = String.format("%4.1f",trial);
               }
               catch(Exception e) {
                   result = getValue().toString();
               }
            }   
        }           
        return(result);
    }
    
}