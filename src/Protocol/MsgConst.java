package Protocol;

public class MsgConst {
    /*
     * 1 send echo request
     * 2 send register request
     * 3 send login request
     * 4 send file lookup request
     * */
    public static int ECHO_REQUEST;
    public static int SEND_REGISTER;
    public static int LOGIN_REQUEST;
    public static int LS_REQUEST;
    public static int UPLOAD_REQUEST;
    public static int DOWNLOAD_REQUEST;

    /*
    *
    * */
    public static int ERROR;
    public static int OK;
    public static int CLIENT;
    public static String DiviedeFile;
    public static String DiviedeMessage;
    /*client*/
    public static String defaultUser;
    public static int EXITClIENT;
    /*server*/
    public static String serverDir;
    public static int bufferLength;
}
