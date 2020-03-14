package Protocol;

import javax.print.DocFlavor;
import java.io.IOException;

public class Msg {
    /*
    * 1 send echo request
    * 2 send register request
    * 3 send login request
    * 4 send file lookup request
    * */
    public  int msgId;
    public int statusCode;
    public String usr;
    public String str;
    public void setMsgId(int msgId){
        this.msgId = msgId;
    }
    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
    }
    public void setUsr(String usr){
        this.usr = usr;
    }
    public void setStr(String str){
        this.str = str;
    }
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(msgId);
        sb.append(MsgConst.DiviedeMessage);
        sb.append(statusCode);
        sb.append(MsgConst.DiviedeMessage);
        sb.append(usr);
        sb.append(MsgConst.DiviedeMessage);
        sb.append(str);
        return sb.toString();
    }
    public byte[] toSerial(){
        //return JsonEncoder.encode(this);
        return toString().getBytes();
    }
    public static Msg getMsg(byte[]bytes){
        //return JsonDecoder.decode(bytes,Msg.class);

        Msg msg = new Msg();
        String mys = new String(bytes);
        String[]s = mys.split(MsgConst.DiviedeMessage);
        msg.setMsgId(Integer.parseInt(s[0]));
        msg.setStatusCode(Integer.parseInt(s[1]));
        msg.setUsr(s[2]);
        msg.setStr(s[3]);
        return msg;
    }
}
