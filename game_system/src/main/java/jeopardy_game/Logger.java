package jeopardy_game;

public class Logger implements Subscriber {
    private static Logger loggerinstance;

    private Logger() {  

    }    

    public static Logger getLogger() {
        if (loggerinstance == null) {
            loggerinstance = new Logger();
        }
        return loggerinstance;
    }

    @Override
    public void update (String log) {
        System.out.println("LOG: " + log);
    }
    //singleton
}
