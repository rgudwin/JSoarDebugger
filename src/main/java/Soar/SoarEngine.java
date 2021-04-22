/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Soar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import org.jsoar.kernel.Agent;
import org.jsoar.kernel.Phase;
import org.jsoar.kernel.RunType;
import org.jsoar.kernel.memory.Wme;
import org.jsoar.kernel.memory.Wmes;
import org.jsoar.kernel.symbols.DoubleSymbol;
import org.jsoar.kernel.symbols.Identifier;
import org.jsoar.kernel.symbols.StringSymbol;
import org.jsoar.kernel.symbols.Symbol;
import org.jsoar.kernel.symbols.SymbolFactory;
import org.jsoar.runtime.ThreadedAgent;
import org.jsoar.util.commands.SoarCommands;
import representation.WMNode;
import representation.WMNodeToBeCreated;

/**
 *
 * @author Danilo Lucentini and Ricardo Gudwin
 */
public class SoarEngine
{
    // Log Variable
    Logger logger = Logger.getLogger(SoarEngine.class.getName());

    // SOAR Variables
    Agent agent = null;
    public Identifier inputLink = null;

    public String input_link_string = "";
    public String output_link_string = "";
    public int phase=0;
    public WMNode outputLink;
    public WMNode il = new WMNode("InputLink");
    static List<Identifier> listtoavoidloops = new ArrayList<>();

    /**
     * Constructor class
     * @param _e Environment
     * @param path Path for Rule Base
     * @param startSOARDebugger set true if you wish the SOAR Debugger to be started
     */
    public SoarEngine(String path, Boolean startSOARDebugger) 
    {
        try
        {
            ThreadedAgent tag = ThreadedAgent.create();
            agent = tag.getAgent();
            SoarCommands.source(agent.getInterpreter(), path);
            inputLink = agent.getInputOutput().getInputLink();

            // Debugger line
            if (startSOARDebugger)
            {
                agent.openDebugger();
            }
        }
        catch (Exception e)
        {
            logger.severe("Error while creating SOAR Kernel");
            e.printStackTrace();
        }
    }
    
    public WMNode getOutputLink() {
        Identifier outLink = agent.getInputOutput().getOutputLink();
        return (addIdentifier2(outLink,"OutputLink"));
    }
    
    public void setInputLink(WMNode node) {
        il = node;
    }    

    public Identifier CreateIdWME(Identifier id, String s) {
        SymbolFactory sf = agent.getSymbols();
        Identifier newID = sf.createIdentifier('I');
        agent.getInputOutput().addInputWme(id, sf.createString(s), newID);
        return(newID);
    }
    
    public void CreateFloatWME(Identifier id, String s, double value) {
        SymbolFactory sf = agent.getSymbols();
        DoubleSymbol newID = sf.createDouble(value);
        agent.getInputOutput().addInputWme(id, sf.createString(s), newID);
    }
    
    public void CreateStringWME(Identifier id, String s, String value) {
        SymbolFactory sf = agent.getSymbols();
        StringSymbol newID = sf.createString(value);
        agent.getInputOutput().addInputWme(id, sf.createString(s), newID);
    }
    
    /**
     * Create the WMEs at the InputLink of SOAR
     */
    public void prepareInputLink() 
    {
        //SymbolFactory sf = agent.getSymbols();
        //System.out.println("Preparing InputLink");
        inputLink = agent.getInputOutput().getInputLink();
        createInputLink(il, inputLink);
        //System.out.println(il.toStringFull());
        try
        {
            if (agent != null)
            {
            }
        }
        catch (Exception e)
        {
            logger.severe("Error while Preparing Input Link");
            e.printStackTrace();
        }
    }

    private void resetSimulation() {
        agent.initialize();
    }
    
    /**
     * Run SOAR until HALT
     */
    private void runSOAR() 
    {
        agent.runForever();
    }
    
    public int getPhase() {
        Phase ph = agent.getCurrentPhase();
        if (ph.equals(Phase.INPUT)) return(0);
        else if (ph.equals(Phase.PROPOSE)) return(1);
        else if (ph.equals(Phase.DECISION)) return(2);
        else if (ph.equals(Phase.APPLY)) return(3);
        else if (ph.equals(Phase.OUTPUT)) {
            if (agent.getReasonForStop() == null) return(4);
            else return(5);
        }
        else return(6);
    }
    
    public String reasonForStop() {
        String reason = agent.getReasonForStop();
        if (reason == null) return("");
        else return reason;
    }
    
    private int stepSOAR() {
        agent.runFor(1, RunType.PHASES);
        return(getPhase());
    }
    
    private int cycleSOAR() {
        for (int i=0;i<5;i++) {
            agent.runFor(1, RunType.PHASES);
            if (agent.getReasonForStop() != null) return(getPhase());
        }
        return(getPhase());
    }

    private String GetParameterValue(String par) {
        List<Wme> Commands = Wmes.matcher(agent).filter(agent.getInputOutput().getOutputLink());
        List<Wme> Parameters = Wmes.matcher(agent).filter(Commands.get(0));
        String parvalue = "";
        for (Wme w : Parameters) 
           if (w.getAttribute().toString().equals(par)) parvalue = w.getValue().toString();
        return(parvalue);
    }
    
    
    /**
     * Process the OutputLink given by SOAR and return a list of commands to WS3D
     * @return A List of SOAR Commands
     */
    private void processOutputLink() 
    {System.out.println("Processing output link");   
    }
    
    /**
     * Perform a complete SOAR step
     * @throws ws3dproxy.CommandExecException
     */
    public void step() 
    {
        if (phase != -1) finish_msteps();
        resetSimulation();
        prepareInputLink();
        input_link_string = stringInputLink();
        //printInputWMEs();
        runSOAR();
        output_link_string = stringOutputLink();
        //printOutputWMEs();
        processOutputLink();
        //processCommands(commandList);
        //resetSimulation();
    }
    
    
    public void prepare_mstep() {
        resetSimulation();
        prepareInputLink();
        input_link_string = stringInputLink();
    }
    
    
    public void mstep() 
    {
        if (phase == -1) {
            prepare_mstep();
            phase = getPhase();
        }
        else {
            phase = stepSOAR();
        }
        if (phase == 5) {
            post_mstep();
            phase = -1;
        }
    }
    
    public void finish_msteps() {
        while (phase != -1) mstep();
    }
    
    public void post_mstep() {
        output_link_string = stringOutputLink();
        //printOutputWMEs();
        processOutputLink();
        //processCommands(commandList);
        //resetSimulation();
    }
    
    public void cycle() {
        for (int i=0;i<5;i++) {
            mstep();
            if (getPhase() == 5) return;
        }    
    }

//    private void processCommands(List<Command> commandList) throws CommandExecException
//    {
//    }

    
    /**
     * Try Parse a Float Element
     * @param value Float Value
     * @return The Float Value or null otherwise
     */
    private Float tryParseFloat (String value)
    {
        Float returnValue = null;

        try
        {
            returnValue = Float.parseFloat(value);
        }
        catch (Exception ex)
        {
            returnValue = null;
        }

        return returnValue;
    }
    
    public void printWME(Identifier id) {
        printWME(id,0);
        
    }
    
    public void printWME(Identifier id, int level) {
        Iterator<Wme> It = id.getWmes();
        while (It.hasNext()) {
            Wme wme = It.next();
            Identifier idd = wme.getIdentifier();
            Symbol a = wme.getAttribute();
            Symbol v = wme.getValue();
            Identifier testv = v.asIdentifier();
            for (int i=0;i<level;i++) System.out.print("   ");
            if (testv != null) {
                System.out.print("("+idd.toString()+","+a.toString()+","+v.toString()+")\n");
                printWME(testv,level+1);
            }
            else System.out.print("("+idd.toString()+","+a.toString()+","+v.toString()+")\n");
        }   
    }
    
    public void printInputWMEs(){
        Identifier il = agent.getInputOutput().getInputLink();
        System.out.println("Input --->");
        printWME(il);
    }
    
    public void printOutputWMEs(){
        Identifier ol = agent.getInputOutput().getOutputLink();
        System.out.println("Output --->");
        printWME(ol);
    }
    
    public String stringWME(Identifier id) {
        listtoavoidloops = new ArrayList<>();
        String out = stringWME(id,0);
        return(out);
    }
    
    public String stringWME(Identifier id, int level) {
        String out="";
        Iterator<Wme> It = id.getWmes();
        while (It.hasNext()) {
            Wme wme = It.next();
            Identifier idd = wme.getIdentifier();
            Symbol a = wme.getAttribute();
            Symbol v = wme.getValue();
            Identifier testv = v.asIdentifier();
            for (int i=0;i<level;i++) out += "   ";
            if (testv != null && !listtoavoidloops.contains(id)) {
                out += "("+idd.toString()+","+a.toString()+","+v.toString()+")\n";
                out += stringWME(testv,level+1);
            }
            else {
                out += "("+idd.toString()+","+a.toString()+","+v.toString()+")\n";
                listtoavoidloops.add(id);
            }
        }
       return(out); 
    }
    
    public String stringInputLink() {
        Identifier il = agent.getInputOutput().getInputLink();
        String out = stringWME(il);
        return(out);
    }
    
    public String stringOutputLink() {
        Identifier ol = agent.getInputOutput().getOutputLink();
        String out = stringWME(ol);
        return(out);
    }
    
    public Identifier getInitialState() {
        Set<Wme> allmem = agent.getAllWmesInRete();
        for (Wme w : allmem) {
            Identifier id = w.getIdentifier();
            if (id.toString().equalsIgnoreCase("S1"))
                return(id);
        }
        return(null);
    }
    
    // Scans All Working Memory selecting all the identifiers for goal states among them
    public List<Identifier> getGoalStates() {
        List<Identifier> li = new ArrayList<Identifier>();
        Set<Wme> allmem = agent.getAllWmesInRete();
        for (Wme w : allmem) {
            Identifier id = w.getIdentifier();
            if (id.isGoal()) {
                boolean alreadythere = false;
                for (Identifier icand : li)
                    if (icand == id) alreadythere = true;
                if (alreadythere == false) {
                    li.add(id);
                }
            }
        }
        return(li);
    }
    
    
    public Set<Wme> getWorkingMemory() {
        return(agent.getAllWmesInRete());
    }
    
    public void createInputLink(WMNode root, Identifier id) {
        inputLink = agent.getInputOutput().getInputLink();
        for (WMNode e : root.getL()) {
            createEntityOnParent(e,id,inputLink);
        }
    }
    
    public void createEntityOnParent(WMNode root, Identifier id, Identifier parent) {
        if (root.isType(0) || root.isType(2)) {
            Identifier newid = this.CreateIdWME(parent, root.getName());
            for (WMNode c : root.getL()) {
                createEntityOnParent(c,newid,newid);
            }
        } else {
            if (root.getValue() instanceof Double) this.CreateFloatWME(id, root.getName(), (double)root.getValue());
            else this.CreateStringWME(id, root.getName(), root.getValue().toString());
        }
    } 
    
    // The following methods are for the creation of a sequence of WMNodes related to the Working Memory
    
    CopyOnWriteArrayList<Identifier> repr = new CopyOnWriteArrayList();   
       
    public List<WMNode> getWM() {
        //WMNode root = new WMNode("Root");
        List<WMNode> root = new CopyOnWriteArrayList<>();
        List<Identifier> wo = new CopyOnWriteArrayList(getGoalStates());
        repr = new CopyOnWriteArrayList<Identifier>();
        for (Identifier ii : wo) {
            WMNode o = addIdentifier2(ii,"State");
            root.add(o);
        }
        return(root);
    }
    
    public WMNode getIdNode(Identifier ido, String attr) {
        // IF the ido is already in the list, just return it
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                WMNode idNode = new WMNode(attr,ido.toString(),2); //genFinalIdNode(ido,attr);
                //System.out.println("getIdNode: Já está na lista ... "+ido.toString());                
                return idNode;
            }
        }
        // ELSE, first add it to the list
        repr.add(ido);
        // THEN generate a new node for it
        WMNode idNode = new WMNode(attr,ido.toString(),0); //genIdNode(ido,attr);
        return(idNode);
    }
    
    public boolean equals(Identifier ido, Identifier ii) {
        return(ii.toString().equalsIgnoreCase(ido.toString()));
    }
    
    public WMNode addIdentifier2(Identifier ido, String attr) {
        List<WMNodeToBeCreated> toBeCreated = new ArrayList<WMNodeToBeCreated>();
        List<WMNodeToBeCreated> toBeFurtherProcessed = new ArrayList<WMNodeToBeCreated>();
        WMNode idNode = getIdNode(ido,attr);
        toBeCreated.add(new WMNodeToBeCreated(idNode,ido,attr));
        List<WMNodeToBeCreated> nextList;
        do {
            nextList = new ArrayList<WMNodeToBeCreated>();
            for (WMNodeToBeCreated wme : toBeCreated) {
                
                toBeFurtherProcessed = processStep(wme.parent,wme.newId,wme.attrib);
                for (WMNodeToBeCreated e : toBeFurtherProcessed) {
                    Identifier id_ = e.newId;
                    String attr_ = e.attrib;
                    WMNode part = getIdNode(id_,attr_);
                    wme.parent.add(part);
                    nextList.add(new WMNodeToBeCreated(part,id_,attr_));
                }
            }
            toBeCreated = nextList;
        } while (nextList.size() > 0);    
        repr = new CopyOnWriteArrayList<Identifier>();
        return(idNode);    
    }
    
    private boolean isIdentifierFinal(Identifier ido) {
        for (Identifier ii : repr) {
            if (equals(ido,ii)) {
                return true;
            }
        }
        return false;
    }
    
    public List<WMNodeToBeCreated> addWME(WMNode idNode, Wme wme) {
        List<WMNodeToBeCreated> toBeFurtherProcessed = new ArrayList<WMNodeToBeCreated>();
        Identifier idd = wme.getIdentifier();
        Symbol a = wme.getAttribute();
        Symbol v = wme.getValue();
        Identifier testv = v.asIdentifier();
        if (testv != null) { // v is an identifier
            // if the identifier is final I can safely introduce it in the tree
            if (isIdentifierFinal(testv) || idd.toString().equalsIgnoreCase(testv.toString()) ) {
                String preference = "";
                if (wme.isAcceptable()) preference = " +";
                WMNode newidNode = new WMNode(a.toString()+preference, testv,2); //genFinalIdNode(testv,a.toString()+preference);
                idNode.add(newidNode);
            }
            else { // mark the identifier to be further processed
               String preference = "";
               if (wme.isAcceptable()) preference = " +";
               if (idd.toString().equalsIgnoreCase(testv.toString()))
                   System.out.println("WME auto-recursivo detectado: ("+idd.toString()+" "+a.toString()+" "+v.toString()+")");
               toBeFurtherProcessed.add(new WMNodeToBeCreated(null,testv,a.toString()+preference));
            }   
        }
        else { // v is a value 
               WMNode valueNode = new WMNode(a.toString(),v.toString(),1); //genValNode(a,v);
               idNode.add(valueNode);
            }
        return(toBeFurtherProcessed);    
    }
    
    // This method scans the identifier ido and finds the new children to be created, inserting nodes for values and final children
    private List<WMNodeToBeCreated> processStep(WMNode idNode, Identifier ido, String attr) {    
        List<WMNodeToBeCreated> toBeFurtherProcessed = new ArrayList();
        //System.out.println("ProcessStep: "+idNode.toString()+" "+ido.toString()+" "+attr);
        // Verify all WMEs using ido as an identifier
        Iterator<Wme> It = ido.getWmes();
        while (It.hasNext()) {
            Wme wme = It.next();
            // Insert nodes for values and final children ... returns the id nodes in the list for further processing
            List<WMNodeToBeCreated> tbfp = addWME(idNode,wme);
            toBeFurtherProcessed.addAll(tbfp);
        }
        return(toBeFurtherProcessed);
    }
    
    
}
