package Client;

import Protocol.Msg;
import Protocol.MsgConst;
import Protocol.CommonTool;
import clientUtil.PercentPrint;

import java.io.*;
import java.net.Socket;

public class CommandHandler {
    private Socket socket;
    private DataOutputStream WebStreamOut;
    private DataInputStream WebStreamIn;
    int byteOnce;
    long bytesTotal;
    public CommandHandler(Socket socket) throws IOException {
        this.socket = socket;
        WebStreamOut = new DataOutputStream(socket.getOutputStream());
        WebStreamIn = new DataInputStream(socket.getInputStream());
    }
    public int processEcho(String word) throws Exception {
        try{
            Msg newMsg = new Msg();
            newMsg.setUsr("null");
            newMsg.setStatusCode(MsgConst.CLIENT);
            newMsg.setMsgId(MsgConst.ECHO_REQUEST);
            newMsg.setStr(word);
            byte[]msgBytes = newMsg.toSerial();
            WebStreamOut.writeInt(msgBytes.length);
            WebStreamOut.write(msgBytes);
            msgBytes = CommonTool.recvBytes(WebStreamIn);
            newMsg = Msg.getMsg(msgBytes);
            System.out.println("received EchoMessage: "+newMsg.str);
            return 0;
        }
        catch (Exception e){
            return -1;
        }
    }
    public int processRegister(String usrname,String passwd){
        try{
            Msg newMsg = new Msg();
            newMsg.setUsr(usrname);
            newMsg.setStatusCode(MsgConst.CLIENT);
            newMsg.setMsgId(MsgConst.SEND_REGISTER);
            newMsg.setStr(passwd);
            byte[]msgBytes = newMsg.toSerial();
            WebStreamOut.writeInt(msgBytes.length);
            WebStreamOut.write(msgBytes);
            msgBytes = CommonTool.recvBytes(WebStreamIn);
            newMsg = Msg.getMsg(msgBytes);
            if(newMsg.statusCode == MsgConst.ERROR){
                System.out.println(newMsg.str);
                return -1;
            }
            System.out.println("Register Successfully for user "+usrname);
            Usrname.usrname = usrname;
            return 0;
        }
        catch (Exception e){
            return -1;
        }
    }
    public int processLogin(String usrname,String passwd){
        try{
            Msg newMsg = new Msg();
            newMsg.setUsr(usrname);
            newMsg.setStatusCode(MsgConst.CLIENT);
            newMsg.setMsgId(MsgConst.LOGIN_REQUEST);
            newMsg.setStr(passwd);
            byte[]msgBytes = newMsg.toSerial();
            WebStreamOut.writeInt(msgBytes.length);
            WebStreamOut.write(msgBytes);
            msgBytes = CommonTool.recvBytes(WebStreamIn);
            newMsg = Msg.getMsg(msgBytes);
            if(newMsg.statusCode == MsgConst.ERROR){
                System.out.println(newMsg.str);
                return -1;
            }
            System.out.println("Login Successfully for user "+usrname);
            Usrname.usrname = usrname;
            return 0;
        }
        catch (Exception e){
            return -1;
        }
    }
    public int processLs(){
        try{
            Msg newMsg = new Msg();
            newMsg.setUsr(Usrname.usrname);
            newMsg.setStatusCode(MsgConst.CLIENT);
            newMsg.setMsgId(MsgConst.LS_REQUEST);
            newMsg.setStr("request for ls");
            byte[]msgBytes = newMsg.toSerial();
            WebStreamOut.writeInt(msgBytes.length);
            WebStreamOut.write(msgBytes);
            msgBytes = CommonTool.recvBytes(WebStreamIn);
            newMsg = Msg.getMsg(msgBytes);
            if(newMsg.statusCode == MsgConst.ERROR){
                System.out.println(newMsg.str);
                return -1;
            }
            String[] fileList = newMsg.str.split(MsgConst.DiviedeFile);
            System.out.println(fileList.length+" files Listed:");
            for(String file:fileList){
                if(file.charAt(0)!='.')
                    System.out.print(file+" ");
            }
            System.out.println();
            return 0;
        }
        catch (Exception e){
            return -1;
        }
    }
    public int processUpload(File fileToSend){
        try{
            StringBuffer sb = new StringBuffer();
            sb.append(fileToSend.getName());
            sb.append(MsgConst.DiviedeFile);
            sb.append(fileToSend.length());
            Msg newMsg = new Msg();
            newMsg.setUsr(Usrname.usrname);
            newMsg.setStatusCode(MsgConst.CLIENT);
            newMsg.setMsgId(MsgConst.UPLOAD_REQUEST);
            newMsg.setStr(sb.toString());
            byte[]msgBytes = newMsg.toSerial();
            WebStreamOut.writeInt(msgBytes.length);
            WebStreamOut.write(msgBytes);
            System.out.println("waiting for ack");
            //waiting for Ack
            msgBytes = CommonTool.recvBytes(WebStreamIn);
            newMsg = Msg.getMsg(msgBytes);
            if(newMsg.statusCode == MsgConst.ERROR){
                System.out.println(newMsg.str);
                return -1;
            }
            //start to transfer file;
            byteOnce = 0;
            bytesTotal = 0;
            FileInputStream fis = new FileInputStream(fileToSend);
            byte[]fileBuffer = new byte[MsgConst.bufferLength];
            System.out.flush();
            PercentPrint prt = new PercentPrint(0, 100, 20, '=');
            while((byteOnce = fis.read(fileBuffer,0,fileBuffer.length))!=-1){
                WebStreamOut.write(fileBuffer,0,byteOnce);
                WebStreamOut.flush();
                bytesTotal += byteOnce;
                prt.show(100*bytesTotal/fileToSend.length());
            }
            fis.close();
            System.out.println("file upload successful!");
            return 0;
        }
        catch (Exception e){
            System.out.println("file upload failed!");
            return -1;
        }
    }
    public int processDownload(String fileToDownload,File downloadPath){
        try{
            Msg newMsg = new Msg();
            newMsg.setUsr(Usrname.usrname);
            newMsg.setStatusCode(MsgConst.CLIENT);
            newMsg.setMsgId(MsgConst.DOWNLOAD_REQUEST);
            newMsg.setStr(fileToDownload);
            byte[]msgBytes = newMsg.toSerial();
            WebStreamOut.writeInt(msgBytes.length);
            WebStreamOut.write(msgBytes);
            System.out.println("waiting for ack");
            //waiting for Ack
            msgBytes = CommonTool.recvBytes(WebStreamIn);
            newMsg = Msg.getMsg(msgBytes);
            if(newMsg.statusCode == MsgConst.ERROR){
                System.out.println(newMsg.str);
                return -1;
            }
            long length = Long.parseLong(newMsg.str);
            //start to transfer file;
            byteOnce = 0;
            bytesTotal = 0;

            FileOutputStream fos = new FileOutputStream(downloadPath);
            byte[]fileBuffer = new byte[MsgConst.bufferLength];
            System.out.flush();
            PercentPrint prt = new PercentPrint(0, 100, 20, '=');
            while(bytesTotal<length){
                if((byteOnce = WebStreamIn.read(fileBuffer,0,fileBuffer.length))!=-1){
                    fos.write(fileBuffer,0,byteOnce);
                    fos.flush();
                    bytesTotal += byteOnce;
                    prt.show(100*bytesTotal/length);
                }
            }
            fos.close();
            System.out.println("file download successful!");
            return 0;
        }
        catch (Exception e){
            System.out.println("file upload failed!");
            return -1;
        }
    }
}
