package Protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;

public class CommonTool {
    public static  byte[]recvBytes(DataInputStream WebStreamIn) throws IOException {
        int length = WebStreamIn.readInt();
        byte[] msgBytes = new byte[length];
        int totalBytesRecv = 0;
        int bytesRead = 0;
        while(totalBytesRecv<length){
            if((bytesRead = WebStreamIn.read(msgBytes,bytesRead,length-totalBytesRecv)) == -1)
                throw new SocketException("Connection closed");
            totalBytesRecv+=bytesRead;
        }
        return msgBytes;
    }
}
