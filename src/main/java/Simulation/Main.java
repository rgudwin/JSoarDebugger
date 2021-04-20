/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Simulation;

import support.MindView;
import support.NativeUtils;
import Soar.SoarEngine;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Danilo
 */
public class Main
{
    Logger logger = Logger.getLogger(Main.class.getName());
        
    private void SilenceLoggers() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.jsoar")).setLevel(ch.qos.logback.classic.Level.OFF);
        Logger.getLogger("Simulation").setLevel(Level.SEVERE);
    }

    public Main() {
        SilenceLoggers();
        try
        {
            //NativeUtils.loadFileFromJar("/soar-rules.soar");
            //String soarRulesPath = "soar-rules.soar";
            //String soarRulesPath = "/home/rgudwin/Local/Cursos/IA941/SoarTutorial_9.6.0-Multiplatform_64bit/agents/mac/mac.soar";
            //String soarRulesPath = "/home/rgudwin/Local/Cursos/IA941/SoarTutorial_9.6.0-Multiplatform_64bit/agents/TankSoar/tutorial/mapping-bot.soar";

            String soarRulesPath = "rules/soar-rules.soar";
            //Start enviroment data
            //Environment e = new Environment(Boolean.FALSE);
            //SoarBridge soarBridge = new SoarEngine(soarRulesPath,true);
            MindView mv = new MindView(soarRulesPath);
            mv.startDebugState();
            //mv.stopDebugState();
            mv.setVisible(true);

            // Wait for the GUI to become visible
            Thread.sleep(3000);

            // Run Simulation until some criteria was reached
            while(true)
            {
                if (mv.getDebugState() == 0) {
                   mv.step();
                   mv.set_input_link_text();
                   mv.set_output_link_text();
                }
                //else e.c.updateState();
                Thread.sleep(100);                   
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
            ex.printStackTrace();
            logger.severe("Unknown error"+ex);
        }
    }

    public static void main(String[] args)
    {
        Main m = new Main();
    }


}
