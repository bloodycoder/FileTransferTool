package Server;

import Loggerfile.LoggerInit;
import Protocol.CommonTool;
import Protocol.Msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

public class Connection implements Runnable{
    private Socket socket;
    private ArrayList<String>pwd;
    private String username;
    public void setUsername(String s){
        username = s;
    }
    public String getUsername(){
        return username;
    }
    public Connection(Socket socket){
        this.socket = socket;
        pwd = new ArrayList<>();
        pwd.add("/");
    }
    public String getpwd(){
        StringBuffer sb = new StringBuffer();
        for(String s:pwd){
            sb.append(s);
        }
        return sb.toString();
    }
    public String getParentPwd(){
        StringBuffer sb = new StringBuffer();
        sb.append("/");
        sb.append(username);
        for(String s:pwd){
            sb.append(s);
        }
        return sb.toString();
    }
    public Socket getSocket(){
        return socket;
    }

    @Override
    public void run() {
        try{
            DataInputStream socketIn = new DataInputStream(socket.getInputStream());
            //DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
            byte[]bytes;
            while(socket.isConnected()){
                bytes = CommonTool.recvBytes(socketIn);
                Msg msg = Msg.getMsg(bytes);
                LoggerInit.synlog(Level.INFO," receive Message from "+socket.getRemoteSocketAddress().toString()+msg);
                RequestHandler.processMsg(msg,this);
            }
        }
        catch (Exception e){
            LoggerInit.synlog(Level.INFO," thread "+Thread.currentThread().getId()+" exited");
        }

    }
}
