/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import support.ToString;

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
        listtoavoidloops = new ArrayList<>();
        return(toStringFull(1));
    }
    
    //List<String> listtoavoidloops = new ArrayList<>();
    
    public String toStringPlus() {
        if (isType(1)) return("- "+name+": "+value);
        else {
            String appendix = "";
            if (value != null && !value.equals("")) appendix = "["+value+"]";
            return("* "+ name+appendix);
        }            
    }
    
    public String toStringFull(int level) {
        String out; 
        if (isType(1)) {
           out = toStringPlus()+"\n";
           return out; 
        }
        else {
            out = toStringPlus()+"\n";
            listtoavoidloops.add(toStringPlus());
            for (WMNode ln : l) {
                for (int i=0;i<level;i++) out += "   ";
                if (listtoavoidloops.contains(ln.toStringPlus()) || already_exists(ln.toStringPlus())) {
                    out += ln.toStringPlus()+"\n";
//                    System.out.println("--------------\nGotcha: "+ln.toStringPlus());
//                    for (Object o : listtoavoidloops) {
//                        System.out.println(o.toString());
//                    }
//                    System.out.println("--------------");
                }
                    
                else out += ln.toStringFull(level+1);
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
    
    transient static ArrayList<Object> listtoavoidloops = new ArrayList<>();
    
    public boolean already_exists(Object o) {
        for (Object oo : listtoavoidloops)
           if (oo.hashCode() == o.hashCode()) return true;
        return false;
    }
    
    public void addObject(Object obj, String fullname) {
        if (obj == null) {
            return;
        }
        if (listtoavoidloops.contains(obj) || already_exists(obj)) {
             //System.out.println("Object found in listtoavoidloops");
             WMNode child = new WMNode(ToString.getSimpleName(fullname),obj.toString(),1);
             add(child);
             //DefaultMutableTreeNode node = addString(obj.toString(),fullname);
            return;            
        }
        String s = ToString.from(obj);
        //System.out.println("listtoavoidloops.size() = "+listtoavoidloops.size());
        //System.out.println(s+" "+obj.hashCode()+" "+fullname);
        if (s != null) {
            //System.out.println(ToString.getSimpleName(fullname)+" "+s);
            WMNode child = new WMNode(ToString.getSimpleName(fullname),s,1);
            add(child);
            //DefaultMutableTreeNode node = addString(s,fullname);
            return;
        }
        else if (obj.getClass().isArray()) {
            int l = Array.getLength(obj);
            String type = obj.getClass().getSimpleName();
            if (l>0) {
                Object otype = Array.get(obj,0);
                if (otype != null)
                    type = otype.getClass().getSimpleName();
            }
            if (type.equalsIgnoreCase("Double") || type.equalsIgnoreCase("Integer") || 
                type.equalsIgnoreCase("String") || type.equalsIgnoreCase("Float") || 
                type.equalsIgnoreCase("Long") || type.equalsIgnoreCase("Boolean")) {
                //Property p = new Property(ToString.getSimpleName(fullname));
                WMNode anode = new WMNode(ToString.getSimpleName(fullname));
                for (int i=0;i<l;i++) {
                    Object oo = Array.get(obj,i);
                    WMNode node = new WMNode(ToString.el(fullname, i),oo,1);
                    anode.add(node);
                    //p.addQualityDimension(ToString.el(fullname, i),oo);
                }
                this.add(anode);
            } 
            else {
                WMNode onode = new WMNode(ToString.getSimpleName(fullname));
                for (int i=0;i<l;i++) {
                    Object oo = Array.get(obj,i);
                    onode.addObject(oo,ToString.el(fullname, i));
                }
                this.add(onode);
            }    
            return;
        }
        else if (obj instanceof List) {
            List ll = (List) obj;
            String label = "";
            if (ll.size() > 0) label = "List["+ll.size()+"] of "+ll.get(0).getClass().getSimpleName();
            else label = "List[0]";
            WMNode onode = new WMNode(ToString.getSimpleName(fullname));
            int i=0;
            for (Object o : ll) {
                onode.addObject(o,ToString.el(fullname,i));
                i++;
            }
            this.add(onode);
            return;
        }
        else if (obj instanceof WMNode) {
            System.out.println("Haha ... object is already a WMNode");
            WMNode ao = (WMNode) obj;
            this.add(ao);
            listtoavoidloops.add(obj);            
            return;
        }
        else {
            WMNode ao = new WMNode(ToString.getSimpleName(fullname));
            listtoavoidloops.add(obj);
            //System.out.println("Adding obj "+fullname+" to listtoavoidloops");
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fname = field.getName();
                //if (!field.isAccessible()) field.setAccessible(true);
                Object fo=null;
                try {
                    fo = field.get(obj);
                } catch (Exception e) {
                    //e.printStackTrace();
                } 
                if (fo != null && !already_exists(fo))
                   ao.addObject(fo,fullname+"."+fname);  
            }
            this.add(ao);
            return;
        }
    }
    
}