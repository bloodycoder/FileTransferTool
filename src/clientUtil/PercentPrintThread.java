package clientUtil;

import Client.CommandHandler;

public class PercentPrintThread implements Runnable{
    private CommandHandler comHand;
    public PercentPrintThread(CommandHandler comHand){
        this.comHand = comHand;
    }
    @Override
    public void run() {

    }

}
