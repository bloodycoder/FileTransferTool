package Client;

import Protocol.MsgConst;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CommandParser {
    public static int parseStr(String cmd,CommandHandler handle) throws Exception {
        String[]s;
        int errcode = 0;
        try{
            s = cmd.split(" ");
        }
        catch (Exception e){
            System.out.println("command error.please type 'help' for help");
            return -1;
        }
        if(s.length>0){
            /**/
            if(s[0].compareTo("echo")==0){
                StringBuffer sb = new StringBuffer();
                for(int i=1;i<s.length;i++){
                    sb.append(s[i]);
                    sb.append(" ");
                }
                errcode = handle.processEcho(sb.toString());
            }
            else if(s[0].compareTo("quit") == 0 || s[0].compareTo("exit") == 0){
                return MsgConst.EXITClIENT;
            }
            if(Usrname.usrname == MsgConst.defaultUser){
                if(s[0].compareTo("register")==0){
                    System.out.print("Please input your username you want to register:");
                    Scanner cons = new Scanner(System.in);
                    /*
                    Console cons = System.console();
                    String usrname = cons.readLine();*/
                    String usrname = cons.nextLine();
                    if(usrname.contains("%") || usrname.contains(" ")){
                        System.out.println("Error:%,space is not allowed");
                        return -1;
                    }
                    if(usrname.length()==0 || usrname.length()>32){
                        System.out.println("username length between 1~32");
                        return -1;
                    }
                    System.out.print("input your password:");
                    //String passwd = new String(cons.readPassword());
                    String passwd = cons.nextLine();
                    System.out.print("input your password again:");
                    //String passwd2 =new String(cons.readPassword());
                    //System.out.println("passwd"+passwd+" "+passwd2);
                    String passwd2 = cons.nextLine();
                    if(passwd.compareTo(passwd2)!=0){
                        System.out.println("Error:password not equal.");
                        return -1;
                    }
                    if(passwd.length()==0 || passwd.length()>32){
                        System.out.println("password length between 1~32");
                        return -1;
                    }
                    errcode = handle.processRegister(usrname,passwd);
                }
                else if(s[0].compareTo("login")==0){
                    System.out.print("username:");
                    //Scanner cons = new Scanner(System.in);
                    Console cons = System.console();
                    String usrname = cons.readLine();
                    //String usrname = cons.nextLine();
                    if(usrname.contains("%") || usrname.contains(" ")){
                        System.out.println("Error:%,space is not allowed");
                        return -1;
                    }
                    if(usrname.length()==0 || usrname.length()>32){
                        System.out.println("username length between 1~32");
                        return -1;
                    }
                    System.out.print("input your password:");
                    String passwd = new String(cons.readPassword());
                    errcode = handle.processLogin(usrname,passwd);
                }
                else{
                    System.out.println("Please login or register first.");
                }
            }
            else{
                if(s[0].compareTo("ls")==0){
                    errcode = handle.processLs();
                }
                else if(s[0].compareTo("upload") == 0){
                    if(s.length == 2){
                        //upload one file to default location.
                        File fileToSend = new File(s[1]);
                        if(!fileToSend.exists()){
                            System.out.println("File not exist.Check your path.Usage upload <localFilePath>");
                            return 0;
                        }
                        errcode = handle.processUpload(fileToSend);
                    }
                    else{
                        System.out.println("invalid upload path.Usage upload <localFilePath>");
                    }
                }
                else if(s[0].compareTo("download") == 0){
                    if(s.length == 3){
                        File fileOut = new File(s[2]);
                        if(!fileOut.createNewFile()){
                            System.out.println("invalid local path.Usage download <remoteFile> <LocalFilePath>");
                            return 0;
                        }
                        errcode = handle.processDownload(s[1],fileOut);
                    }
                    else{
                        System.out.println("invalid local path.Usage download <remoteFile> <LocalFilePath>");
                    }
                }
            }
        }
        return errcode;
    }
}
