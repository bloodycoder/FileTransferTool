package Server;

import Protocol.MsgConst;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private static DataBase dbInstance;
    private static String fileDir = MsgConst.serverDir+"/"+"usrdb";
    private ConcurrentHashMap<String,String>usrInfo;
    public static DataBase getDbInstance(){
        return dbInstance;
    }
    public static void init() throws IOException {
        dbInstance = new DataBase();
        File file = new File(fileDir);
        getDbInstance().usrInfo = new ConcurrentHashMap<>();
        if(!file.exists()){
            file.createNewFile();
        }
        else{
            FileReader fin = new FileReader(fileDir);
            BufferedReader bufferRead = new BufferedReader(fin);
            String s;
            while((s = bufferRead.readLine())!=null){
                String []tmp = s.split(" ");
                dbInstance.usrInfo.put(tmp[0],tmp[1]);
            }
            fin.close();
        }
    }
    public void appendUser(String user,String passwd) throws IOException {
        usrInfo.put(user,passwd);
        WriteUser(user,passwd);
    }
    public boolean checkUserPass(String user,String passwd){
        if(!isUsrExist(user)){
            return false;
        }
        String savedPass = usrInfo.get(user);
        return savedPass.compareTo(passwd) == 0;
    }
    private void WriteUser(String user,String passwd) throws IOException {
        FileWriter writer = new FileWriter(fileDir, true);
        StringBuffer sb = new StringBuffer();
        sb.append(user);
        sb.append(" ");
        sb.append(passwd);
        sb.append("\n");
        writer.write(sb.toString());
        writer.close();;
    }
    public boolean isUsrExist(String user){
        return usrInfo.containsKey(user);
    }
    public String getUsrPasswd(String user){
        return usrInfo.get(user);
    }

}
