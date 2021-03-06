package Server;
import Loggerfile.LoggerInit;
import Protocol.PropertyRead;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLServer {

    // 服务器端授权的用户名和密码
    private static final String USER_NAME = "principal";
    private static final String PASSWORD = "credential";
    // 服务器端保密内容
    private static final String SECRET_CONTENT =
            "This is confidential content from server X, for your eye!";

    private ServerSocket serverSocket = null;
    public SSLServer() throws Exception {
        // 通过套接字工厂，获取一个服务器端套接字
        serverSocket = MySocketFactory.getServerSocket();
    }

    public void runServer() {
        while (true) {
            try {
                LoggerInit.synlog(Level.INFO,"Waiting for connection...");
                // 服务器端套接字进入阻塞状态，等待来自客户端的连接请求
                Socket socket = serverSocket.accept();
                Connection newConnect = new Connection(socket);
                Thread thread = new Thread(newConnect);
                thread.start();
                /*
                // 获取服务器端套接字输入流
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 从输入流中读取客户端用户名和密码
                String userName = input.readLine();
                String password = input.readLine();

                // 获取服务器端套接字输出流
                PrintWriter output = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream()));

                // 对请求进行认证，如果通过则将保密内容发送给客户端
                if (userName.equals(USER_NAME) && password.equals(PASSWORD)) {
                    output.println("Welcome, " + userName);
                    output.println(SECRET_CONTENT);
                } else {
                    output.println("Authentication failed, you have no access to server X...");
                }

                // 关闭流资源和套接字资源
                output.close();
                input.close();*/

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    public static void main(String args[]) throws Exception {
        /**
         * java -Djavax.net.ssl.keyStore=sslserverkeys -Djavax.net.ssl.keyStorePassword=992288 -Djavax.net.ssl.trustStore=sslservertrust -Djavax.net.ssl.trustStorePassword=992288 Server.SSLServer
         */
        PropertyRead.init();
        DataBase.init();
        LoggerInit.init();
        SSLServer server = new SSLServer();
        server.runServer();
    }

}