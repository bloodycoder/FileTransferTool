package Server;

import Protocol.Msg;
import Protocol.MsgConst;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class RequestHandler {
    public static void processMsg(Msg msgFromCli, Connection con) throws IOException {
        if(msgFromCli.msgId == MsgConst.ECHO_REQUEST){
            RealWorker.processEcho(msgFromCli,con);
        }
        else if(msgFromCli.msgId == MsgConst.SEND_REGISTER){
            RealWorker.processRegister(msgFromCli,con);
        }
        else if(msgFromCli.msgId == MsgConst.LOGIN_REQUEST){
            RealWorker.processLogin(msgFromCli,con);
        }
        else if(msgFromCli.msgId == MsgConst.LS_REQUEST){
            RealWorker.processLs(msgFromCli,con);
        }
        else if(msgFromCli.msgId == MsgConst.UPLOAD_REQUEST){
            RealWorker.processUpload(msgFromCli,con);
        }
        else if(msgFromCli.msgId == MsgConst.DOWNLOAD_REQUEST){
            RealWorker.processDownload(msgFromCli,con);
        }
    }
}
