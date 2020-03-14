package Loggerfile;

import Config.ServerConig;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerInit {
    private static Logger logger = Logger.getLogger("serverLog");
    public static void init() throws IOException {
        FileHandler fileHandler = new FileHandler(ServerConig.logdir);
        fileHandler.setLevel(Level.INFO);
        logger.addHandler(fileHandler);
    }
    public static Logger getLogger(){
        return logger;
    }
    public static synchronized void synlog(Level level,String s){
        if(logger != null)
            logger.log(level,s);
        else
            throw new IllegalStateException("logger not init");
    }
}
