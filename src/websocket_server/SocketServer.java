package websocket_server;


import com.google.gson.Gson;
import entity.ChatMSG;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务端
 *
 */
public class SocketServer{

    public static CopyOnWriteArrayList<ChatMSG> chatMSGS = new CopyOnWriteArrayList<>();
    private static ArrayList<SocketThread> mThreadList = new ArrayList<>();
    public static void main(String[] args) {
        startService();
    }
    private static Socket socket= null;
    private static SocketThread currentThread = null;
    private static ChatMSG emptymsg = new ChatMSG();
    /**
     * 启动服务监听，等待客户端连接
     */
    public static void startService() {
        try {
            // 创建ServerSocket
            emptymsg.setContent("");

            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("--开启服务器，监听端口 9999--");
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("主机名："+address.getHostName());//主机名
            //System.out.println(address.getCanonicalHostName());//主机别名
            System.out.println("IP地址："+address.getHostAddress());//获取IP地址
            System.out.println("===============");
            sendMsg();
            // 监听端口，等待客户端连接
            while (true) {
                System.out.println("--等待客户端连接--");
                socket = serverSocket.accept(); //等待客户端连接
                System.out.println("得到客户端连接：" + socket);

                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
                socketThread.checkConn();
                mThreadList.add(socketThread);
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
    private static void sendMsg() {
        new Thread() {
            @Override
            public void run() {
                ChatMSG from = null;
                boolean whileflag = true;
                try {

                    while(whileflag) {
                        if(chatMSGS.size() > 0) {
                            from = chatMSGS.get(0);
                            if(from == null)continue;
                            boolean flag = false;
                            for(SocketThread toThread : mThreadList) {
                                if(toThread.fromid == null){
                                    continue;
                                }
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
                                    System.out.println("转发消息成功"+from.getContent());
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
                    sendMsg();
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
        private SocketThread myself;
        private boolean flag = true;
        SocketThread(Socket clientSocket){
            this.clientSocket = clientSocket;
            myself = this;
        }

        //心跳包
        private void checkConn(){
            new Thread() {
                @Override
                public void run(){
                    try
                    {
                        int index = 1;
                        while (true) {
                            BufferedWriter writer = myself.mWriter;
                            if(writer == null){
                                Thread.sleep(3*100);
                                continue;
                            }
                            String json = new Gson().toJson(emptymsg);
                            writer.write(json+"\n");
                            writer.flush();
                           //socket.sendUrgentData(32);//发送心跳包
                            System.out.println("目标处于链接状态！");
                            Thread.sleep(3*1000);
                        }
                    } catch (IOException e) {
                        try {
                            socket.close();
                            mThreadList.remove(myself);
                            flag = false;
                            System.out.println("服务器关闭连接！");

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
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
                        while (flag) {
                            if (mReader.ready()) {
                                String json=mReader.readLine();
                                Gson gson = new Gson();
                                ChatMSG msg = gson.fromJson(json,ChatMSG.class);
                                fromid = msg.getFromid();
                                msg.setDate(new Date());
                                chatMSGS.add(0,msg);
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
