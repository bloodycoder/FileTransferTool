package Client;

import Config.ClientConfig;
import Config.SSLConfig;
import Protocol.PropertyRead;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
class SSLClient {
    public static Socket getClient() throws IOException {
        // 通过套接字工厂，获取一个客户端套接字
        try{
            if(SSLConfig.openSSL){
                SSLSocketFactory socketFactory;
                socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                System.out.println("connecting "+ClientConfig.remoteIP+":"+ClientConfig.port+" withSSL");
                return socketFactory.createSocket(ClientConfig.remoteIP,ClientConfig.port);

            }
            else{
                System.out.println("connecting "+ClientConfig.remoteIP+":"+ClientConfig.port+" withoutSSL");
                return new Socket(ClientConfig.remoteIP,ClientConfig.port);
            }
        }
        catch (Exception e){
            System.out.println("Can't establish connection.Please Check configuration.");
            throw new IOException("cant");
        }

    }

    public static void main(String args[]) throws Exception {
        /*
        *
java  -Djavax.net.ssl.keyStore=sslclientkeys -Djavax.net.ssl.keyStorePassword=992288 -Djavax.net.ssl.trustStore=sslclienttrust -Djavax.net.ssl.trustStorePassword=992288 Client.SSLClient

        * */
        PropertyRead.init();
        CommandClient client = new CommandClient(SSLClient.getClient());
        client.runForever();
    }
}