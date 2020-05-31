package com.kumuluzee.blocker.ai.api;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;

import java.io.*;

public class PythonHandler {
    public static BufferedReader inp;
    public static BufferedWriter out;
    public static Process p;
    final static Logger LOG = LogManager.getLogger(CustomDiscovery.class.getName());

    public static void init(){
        String cmd = "python ai-api/python-app/python-app.py";

        try {
            ProcessBuilder pBuilder = new ProcessBuilder();
            pBuilder.redirectErrorStream(true);
            pBuilder.command("bash", "-c", cmd);
            p = pBuilder.start();

            inp = new BufferedReader( new InputStreamReader(p.getInputStream()) );
            out = new BufferedWriter( new OutputStreamWriter(p.getOutputStream()) );
            LOG.info("Python app alive? " + p.isAlive());
        }
        catch (Exception err) {
            LOG.error(err.getMessage());
        }
    }

    public PythonHandler() {
        init();
    }

    public static String pipe(String msg) {
        String ret;
        if(!p.isAlive()){
            try {
                init();
                if(!p.isAlive()) throw new Exception("Couldnt start python process.");
            }
            catch (Exception err) {
                LOG.error("3333" + err.toString());
                return "Couldnt start python process.";
            }
        }

        try {
            out.write( msg + "\n" );
            out.flush();
            ret = inp.readLine();
            return ret;
        }
        catch (Exception err) {
            LOG.error("1234" + err.toString());
        }
        return "IO python process failure.";
    }
}
