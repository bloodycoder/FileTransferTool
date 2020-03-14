package Server;

import Config.SSLConfig;
import Config.ServerConig;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

public class MySocketFactory {
    public static ServerSocket getServerSocket() throws IOException {
        if(SSLConfig.openSSL){
            SSLServerSocketFactory socketFactory = (SSLServerSocketFactory)
                    SSLServerSocketFactory.getDefault();
            return socketFactory.createServerSocket(ServerConig.port);
        }
        else{
            return new ServerSocket(ServerConig.port);
        }
    }
}
