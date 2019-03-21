package conn_interface;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServletsConn {
    static String host = "http://192.168.1.102:8080/MyServlets_war_exploded/";
    public static String connServlets(String url,String json){
        Gson gson = new Gson();
        HttpURLConnection conn = null;
        System.out.println(json);
        try {
            conn = (HttpURLConnection) new URL(host+url).openConnection();
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(30000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            OutputStream out = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(json);
            bw.flush();
            out.close();
            bw.close();


            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while((str = br.readLine())!=null){
                buffer.append(str);
            }
            in.close();
            br.close();
            json=buffer.toString();
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
