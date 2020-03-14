package Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class PropertyRead {
    public static Properties myProperty;
    public static void init() throws IOException {
        myProperty = new Properties();
        InputStream inputStream = Object.class.getResourceAsStream("/config.properties");
        myProperty.load(inputStream);
        MsgConst.ECHO_REQUEST =  Integer.parseInt((String) myProperty.get("ECHO_REQUEST"));
        MsgConst.LOGIN_REQUEST = Integer.parseInt((String) myProperty.get("LOGIN_REQUEST"));
        MsgConst.SEND_REGISTER = Integer.parseInt((String) myProperty.get("SEND_REGISTER"));
        MsgConst.LS_REQUEST = Integer.parseInt((String) myProperty.get("LS_REQUEST"));
        MsgConst.UPLOAD_REQUEST = Integer.parseInt((String) myProperty.get("UPLOAD_REQUEST"));
        MsgConst.DOWNLOAD_REQUEST = Integer.parseInt((String) myProperty.get("DOWNLOAD_REQUEST"));
        MsgConst.ERROR = Integer.parseInt((String) myProperty.get("ERROR"));
        MsgConst.OK = Integer.parseInt((String) myProperty.get("OK"));
        MsgConst.CLIENT = Integer.parseInt((String) myProperty.get("CLIENT"));
        MsgConst.DiviedeFile = "厶";
        MsgConst.DiviedeMessage = "宄";
        MsgConst.defaultUser = (String)myProperty.get("defaultUser");
        MsgConst.EXITClIENT = Integer.parseInt((String)myProperty.get("EXITClIENT"));
        MsgConst.serverDir = (String)myProperty.get("serverDir");
        MsgConst.bufferLength = Integer.parseInt((String)myProperty.get("bufferLength"));
    }
}
