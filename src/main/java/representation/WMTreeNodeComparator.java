/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import support.*;
import java.util.Comparator;

/**
 *
 * @author rgudwin
 */
public class WMTreeNodeComparator implements Comparator {
    
        private final boolean order;

        public WMTreeNodeComparator()
        {
            this(true);
        }

        public WMTreeNodeComparator(boolean order)
        {
            this.order = order;
        }
        
        int getInt(String s) {
            int n;
            try { 
               n = Integer.parseInt(s);
            } catch(Exception e) {
               return(Integer.MIN_VALUE);     
            }
            return(n); 
        }

        @Override
        public int compare(Object o1, Object o2)
        {
            WMTreeNode wo1 = (WMTreeNode) o1;
            WMTreeNode wo2 = (WMTreeNode) o2;
            TreeElement te1 = wo1.getTreeElement();
            TreeElement te2 = wo2.getTreeElement();
            //System.out.println("Comparing "+te1.getNamePlusValuePlusId()+" "+te1.getIcon()+" with "+te2.getNamePlusValuePlusId()+" "+te2.getIcon());
            int try1 = getInt(te1.getName().split("\\[")[0].trim());
            int try2 = getInt(te2.getName().split("\\[")[0].trim());
            if (try1 != Integer.MIN_VALUE && try2 != Integer.MIN_VALUE) {
                if (try1 > try2) return(1);
                else return(-1);
            }
            if (te1.getName().startsWith("operator") && !te2.getName().startsWith("operator")) return(1);
            else if(!te1.getName().startsWith("operator") && te2.getName().startsWith("operator")) return(-1); 
            else if (te1.getIcon() == te2.getIcon() || te1.getIcon() == 2 && te2.getIcon() == 18 ||
                     te1.getIcon() == 18 && te2.getIcon() == 2) {
                return te1.getNamePlusValuePlusId().compareTo(te2.getNamePlusValuePlusId());
            }    
            else if (te1.getIcon() == 2 && te2.getIcon() == 5 || te1.getIcon() == 2 && te2.getIcon() == 18 || te1.getIcon() == 18 && te2.getIcon() == 5 ) {
                return(-1);
            }
            else return(1);
        }    
}
