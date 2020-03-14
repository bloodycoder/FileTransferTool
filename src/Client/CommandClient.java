package Client;

import Protocol.MsgConst;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class CommandClient {
    private Socket socket = null;
    public CommandClient(Socket socket) throws IOException {
        this.socket = socket;
    }
    public void connect() {
        try {
            // 获取客户端套接字输出流
            PrintWriter output = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            // 将用户名和密码通过输出流发送到服务器端
            String userName = "principal";
            output.println(userName);
            String password = "credential";
            output.println(password);
            output.flush();

            // 获取客户端套接字输入流
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            // 从输入流中读取服务器端传送的数据内容，并打印出来
            String response = input.readLine();
            response += "\n " + input.readLine();
            System.out.println(response);

            // 关闭流资源和套接字资源
            output.close();
            input.close();
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public void runForever() throws Exception {
        System.out.println("Welcome.Type 'help' for help");
        Scanner commandIn = new Scanner(System.in);
        CommandHandler handle = new CommandHandler(this.socket);
        String cmd;
        int err;
        while(socket.isConnected()){
            System.out.print(Usrname.usrname+">");
            cmd = commandIn.nextLine();
            err = CommandParser.parseStr(cmd,handle);
            if(err == -1){
                System.out.println("Server error please retry.");
                break;
            }
            else if(err == MsgConst.EXITClIENT){
                System.out.println("Exit system.");
                break;
            }
        }
        socket.close();
        commandIn.close();
    }
}
