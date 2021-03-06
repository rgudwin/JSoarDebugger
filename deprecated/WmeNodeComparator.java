/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.Comparator;

/**
 *
 * @author rgudwin
 */
public class WmeNodeComparator implements Comparator {
    
        private final boolean order;

        public WmeNodeComparator()
        {
            this(true);
        }

        public WmeNodeComparator(boolean order)
        {
            this.order = order;
        }

        @Override
        public int compare(Object o1, Object o2)
        {
            WmeNode wo1 = (WmeNode) o1;
            WmeNode wo2 = (WmeNode) o2;
            TreeElement te1 = wo1.getTreeElement();
            TreeElement te2 = wo2.getTreeElement();
            //System.out.println("Comparing "+te1.getNamePlusValuePlusId()+" "+te1.getIcon()+" with "+te2.getNamePlusValuePlusId()+" "+te2.getIcon());
            if (te1.getName().startsWith("operator") && !te2.getName().startsWith("operator")) return(1);
            else if(!te1.getName().startsWith("operator") && te2.getName().startsWith("operator")) return(-1); 
            else if (te1.getIcon() == te2.getIcon()) {
                return te1.getNamePlusValuePlusId().compareTo(te2.getNamePlusValuePlusId());
            }    
            else if (te1.getIcon() == 2 && te2.getIcon() == 5 || te1.getIcon() == 2 && te2.getIcon() == 18 || te1.getIcon() == 18 && te2.getIcon() == 5 ) {
                return(-1);
            }
            else return(1);
        }    
}
