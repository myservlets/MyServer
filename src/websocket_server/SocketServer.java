package websocket_server;


import com.google.gson.Gson;
import entity.ChatMSG;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * 服务端
 *
 */
public class SocketServer{

    public static ArrayList<ChatMSG> chatMSGS = new ArrayList<>();
    private static ArrayList<SocketThread> mThreadList = new ArrayList<>();
    public static void main(String[] args) {
        startService();
    }
    private static Socket socket= null;
    private static SocketThread currentThread = null;
    /**
     * 启动服务监听，等待客户端连接
     */
    public static void startService() {
        try {
            // 创建ServerSocket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("--开启服务器，监听端口 9999--");
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("主机名："+address.getHostName());//主机名
            //System.out.println(address.getCanonicalHostName());//主机别名
            System.out.println("IP地址："+address.getHostAddress());//获取IP地址
            System.out.println("===============");

            // 监听端口，等待客户端连接
            while (true) {
                System.out.println("--等待客户端连接--");
                socket = serverSocket.accept(); //等待客户端连接
                System.out.println("得到客户端连接：" + socket);

                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
                mThreadList.add(socketThread);
                sendMsg(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     */
    private static void sendMsg(final Socket socket) {
        new Thread() {
            @Override
            public void run() {
                ChatMSG from = null;
                boolean whileflag = true;
                try {

                    while(whileflag) {
                        if(chatMSGS.size() > 0) {
                            from = chatMSGS.get(0);
                            boolean flag = false;
                            for(SocketThread toThread : mThreadList) {
                                if(toThread.fromid.equals(from.getTargetid())) {
                                    //这里的writer是SocketThread中的writer,这样才能保证在调用writer.flush之后消息到达
                                    //我们的指定方
                                    currentThread = toThread;
                                    if(toThread.clientSocket.isClosed()){
                                        mThreadList.remove(toThread);
                                        break;
                                    }
                                    flag =true;
                                    BufferedWriter writer = toThread.mWriter;
                                    String json = new Gson().toJson(from);
                                    writer.write(json+"\n");
                                    writer.flush();
                                    System.out.println("转发消息成功");
                                    break;
                                }
                            }
                            chatMSGS.remove(from);
                            if(!flag)chatMSGS.add(from);
                        }
                        Thread.sleep(200);
                    }

                } catch (SocketException e){
                    mThreadList.remove(currentThread);
                    chatMSGS.remove(from);
                    chatMSGS.add(from);
                    whileflag = false;
                    sendMsg(socket);
                    this.stop();
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static class SocketThread {
        private Socket clientSocket;
        private String fromid;
        private BufferedReader mReader;
        private BufferedWriter mWriter;

        SocketThread(Socket clientSocket){
            this.clientSocket = clientSocket;
        }

        public void start() throws InterruptedException {
            new Thread(){
                @Override
                public void run() {

                    try {
                        // 获取读取流
                        mReader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream(),"utf-8"));
                        mWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(),"utf-8"));
                        //循环读取客户端发过来的消息
                        int i= 0;
                        while (true) {
                            if (mReader.ready()) {
                                String json=mReader.readLine();
                                Gson gson = new Gson();
                                ChatMSG msg = gson.fromJson(json,ChatMSG.class);
                                fromid = msg.getFromid();
                                chatMSGS.add(msg);
                                if(i == 0){
                                    for(int a = 0;a<mThreadList.size()-1;a++){
                                        SocketThread socketThread = mThreadList.get(a);
                                        if(fromid.equals(socketThread.fromid)){
                                            mThreadList.remove(socketThread);
                                            break;
                                        }
                                    }
                                    i++;
                                }
                                System.out.println(json);
                            }
                            Thread.sleep(100);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
