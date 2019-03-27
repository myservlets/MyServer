package socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 服务端 
 *
 */
public class SocketServer{

    static ArrayList<Message> messages = new ArrayList<>();
    private static ArrayList<SocketThread> mThreadList = new ArrayList<>();
    public static void main(String[] args) {
        startService();
    }

    /**
     * 启动服务监听，等待客户端连接 
     */

    private static void startService() {
        try {
            // 创建ServerSocket  
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("--开启服务器，监听端口 9999--");
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("主机名：" + address.getHostName());//主机名
            System.out.println("主机别名：" + address.getCanonicalHostName());//主机别名
            System.out.println("主机ip：" + address.getHostAddress());//获取IP地址
            System.out.println("===============");

            // 监听端口，等待客户端连接  
            while (true) {
                System.out.println("--等待客户端连接--");
                Socket socket = serverSocket.accept(); //等待客户端连接  
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

                BufferedWriter mWriter;
                try {

                    while(true) {
                        if(messages.size() > 0) {
                            Message from = messages.get(0);
                            for(SocketThread toThread : mThreadList) {
                                //遍历mThreadList如果to.socketID==from.to说明这个toThread与mMsgList中的这条内容是对应的
                                //这里toThread的作用是通过它得到这条消息的BufferedWriter，mMsgList.get(0)得到这条消息，然后通过
                                //BufferedWriter将这条消息发送到指定方
                                if(toThread.fromId == from.getTo()) {
                                    //这里的writer是SocketThread中的writer,这样才能保证在调用writer.flush之后消息到达
                                    //我们的指定方
                                    BufferedWriter writer = toThread.mWriter;
                                    String json = "{'from':"+ from.getFrom() +",'time':"+ from.getTime() +",'msg':"+ from.getMsg() +"}";
                                    writer.write(json + "\n"); // 写一个UTF-8的信息
                                    writer.flush();
                                    System.out.println("转发消息成功");
                                    break;
                                }
                            }
                            messages.remove(0);
                        }
                        Thread.sleep(200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public static class SocketThread {
        private Socket clientSocket;
        private int fromId;
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
                        while (true) {
                            if (mReader.ready()) {
                                String json=mReader.readLine();
                                Gson gson = new Gson();
                                JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
                                Message msg = new Message();
                                msg.setTo(Integer.parseInt(jsonObject.get("to").toString()));
                                msg.setMsg(jsonObject.get("msg").toString());
                                fromId = Integer.parseInt(jsonObject.get("from").toString());
                                msg.setFrom(fromId);
                                msg.setTime( System.currentTimeMillis());
                                messages.add(msg);
                                System.out.println("用户："+msg.getFrom()+"向用户："+msg.getTo()+"发送的消息内容为："+msg.getMsg());
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