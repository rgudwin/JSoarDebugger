/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.Formatter;
import java.util.Iterator;
import org.jsoar.kernel.memory.Preference;
import org.jsoar.kernel.memory.Wme;
import org.jsoar.kernel.symbols.Identifier;
import org.jsoar.kernel.symbols.Symbol;

/**
 *
 * @author rgudwin
 */
public class WmeSpec implements Wme {
    
    Identifier identifier;
    Symbol attribute;
    Symbol value;
    int timeTag;
    boolean acceptable;

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public Symbol getAttribute() {
        return attribute;
    }

    @Override
    public Symbol getValue() {
        return value;
    }

    @Override
    public int getTimetag() {
        return timeTag;
    }

    @Override
    public boolean isAcceptable() {
        return acceptable;
    }

    @Override
    public Iterator<Wme> getChildren() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Preference> getPreferences() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void formatTo(Formatter frmtr, int i, int i1, int i2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getAdapter(Class<?> type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
