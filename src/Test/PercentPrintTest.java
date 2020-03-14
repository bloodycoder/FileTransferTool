package Test;

import clientUtil.PercentPrint;
import org.junit.Test;

import java.io.Console;

import static org.junit.Assert.*;

public class PercentPrintTest {

    @Test
    public void fuck(){
        Console cons = System.console();
        String usrname = cons.readLine();
        if(usrname.contains("%") || usrname.contains(" ")){
            System.out.println("Error:%,space is not allowed");
        }
        if(usrname.length()==0 || usrname.length()>32){
            System.out.println("username length between 1~32");
        }
        System.out.print("Please set your password:");
        String passwd = cons.readPassword().toString();
        System.out.print("Please type your password again:");
        String passwd2 = cons.readPassword().toString();
    }
}