package Server;

import Config.ServerConig;
import Loggerfile.LoggerInit;
import Protocol.CommonTool;
import Protocol.Msg;
import Protocol.MsgConst;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

public class RealWorker {
    public static void processEcho(Msg msg, Connection connection) throws IOException {
        Socket socket = connection.getSocket();
        DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
        msg.statusCode = MsgConst.OK;
        byte[]msgBytes = msg.toSerial();
        socketOut.writeInt(msgBytes.length);
        socketOut.write(msgBytes);
    }
    public static void processRegister(Msg msg,Connection connection)throws IOException{
        Socket socket = connection.getSocket();
        DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
        if(DataBase.getDbInstance().isUsrExist(msg.usr)){
            msg.statusCode = MsgConst.ERROR;
            msg.str = "User Already Exist";
        }
        else{
            DataBase.getDbInstance().appendUser(msg.usr,msg.str);
            msg.statusCode = MsgConst.OK;
            msg.str = "OK";
        }
        LoggerInit.synlog(Level.INFO,msg.toString());
        byte[]msgBytes = msg.toSerial();
        socketOut.writeInt(msgBytes.length);
        socketOut.write(msgBytes);
        connection.setUsername(msg.usr);
        File file = new File(ServerConig.workingdir+connection.getParentPwd());
        if(!file.exists())
            file.mkdir();
    }
    public static void processLogin(Msg msg,Connection connection)throws IOException{
        Socket socket = connection.getSocket();
        DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
        if(DataBase.getDbInstance().checkUserPass(msg.usr,msg.str)){
            msg.statusCode = MsgConst.OK;
            msg.str = "Login Success";
        }
        else{
            msg.statusCode = MsgConst.ERROR;
            msg.str = "Login Failed.Check password.";
            LoggerInit.synlog(Level.INFO,"user "+msg.usr+" login failed with invalid password.");
        }
        LoggerInit.synlog(Level.INFO,"user "+msg.usr+" login success.");
        byte[]msgBytes = msg.toSerial();
        socketOut.writeInt(msgBytes.length);
        socketOut.write(msgBytes);
        connection.setUsername(msg.usr);
        File file = new File(ServerConig.workingdir+connection.getParentPwd());
        if(!file.exists())
            file.mkdir();
    }
    public static void processLs(Msg msg,Connection connection)throws IOException{
        Socket socket = connection.getSocket();
        DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
        String currentPwd = ServerConig.workingdir+connection.getParentPwd();
        if(connection.getUsername()==null || connection.getUsername().length()<=0){
            throw new IOException("username should not be null.");
        }
        File file = new File(currentPwd);
        if(!file.exists()){
            throw new IOException("user dir "+currentPwd+" not found");
        }
        LoggerInit.synlog(Level.INFO,"user "+msg.usr+" try to show files in "+currentPwd);
        File[] files = file.listFiles();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<files.length;i++){
            sb.append(files[i].getName());
            if(i!=files.length-1)
                sb.append(MsgConst.DiviedeFile);
        }
        msg.str = sb.toString();
        byte[]msgBytes = msg.toSerial();
        socketOut.writeInt(msgBytes.length);
        socketOut.write(msgBytes);
    }
    public static void processUpload(Msg msg,Connection connection)throws IOException{
        Socket socket = connection.getSocket();
        DataInputStream socketIn = new DataInputStream(socket.getInputStream());
        DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
        if(connection.getUsername()==null || connection.getUsername().length()<=0){
            throw new IOException("username should not be null.");
        }
        String currentPwd = ServerConig.workingdir+connection.getParentPwd();
        String[] paras= msg.str.split(MsgConst.DiviedeFile);
        String fileName = paras[0];
        long fileLength = Long.parseLong(paras[1]);

        File file = new File(currentPwd+fileName);
        file.createNewFile();
        LoggerInit.synlog(Level.INFO,connection.getUsername()+" try to upload file "+fileName+" "+fileLength+"bytes");
        msg.statusCode = MsgConst.OK;
        byte[]msgBytes = msg.toSerial();
        socketOut.writeInt(msgBytes.length);
        socketOut.write(msgBytes);
        socketOut.flush();
        //recv file
        FileOutputStream fout = new FileOutputStream(file);
        byte[]bytes = new byte[MsgConst.bufferLength];
        int recv;
        long totalRecv = 0;
        while(totalRecv<fileLength && (recv = socketIn.read(bytes,0,bytes.length))!=-1){
            fout.write(bytes,0,recv);
            fout.flush();
            totalRecv+=recv;
        }
        fout.close();
    }
    public static void processDownload(Msg msg,Connection connection) throws IOException {
        Socket socket = connection.getSocket();
        DataInputStream socketIn = new DataInputStream(socket.getInputStream());
        DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
        if(connection.getUsername()==null || connection.getUsername().length()<=0){
            throw new IOException("username should not be null.");
        }
        String currentPwd = ServerConig.workingdir+connection.getParentPwd();
        String fileName =  msg.str;
        File file = new File(currentPwd+fileName);
        byte[]msgBytes;
        if(!file.exists()){
            msg.statusCode = MsgConst.ERROR;
            msg.str = "File not exist in Server";
            LoggerInit.synlog(Level.INFO,connection.getUsername()+" try to download file "+fileName+"failed not exist in "+currentPwd);
            msgBytes = msg.toSerial();
            socketOut.write(msgBytes.length);
            socketOut.write(msgBytes);
            socketOut.flush();
            return;
        }
        LoggerInit.synlog(Level.INFO,connection.getUsername()+" try to download file "+fileName+" "+file.length()+" bytes");
        msg.statusCode = MsgConst.OK;
        StringBuffer sb = new StringBuffer();
        sb.append(file.length());
        msg.str = sb.toString();
        msgBytes = msg.toSerial();
        socketOut.writeInt(msgBytes.length);
        socketOut.write(msgBytes);
        socketOut.flush();
        //recv file
        FileInputStream fin = new FileInputStream(file);
        byte[]bytes = new byte[MsgConst.bufferLength];
        int recv;
        long totalRecv = 0;
        while((recv = fin.read(bytes,0,bytes.length))!=-1){
            socketOut.write(bytes,0,recv);
            socketOut.flush();
            totalRecv+=recv;
        }
        fin.close();
    }

}
