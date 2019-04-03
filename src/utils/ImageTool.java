package utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class ImageTool {
    /**
     * 字符串转图片
     * @param imgStr --->图片字符串
     * @param filename    --->图片名
     * @return
     */
    public static boolean generateImage(String imgStr, String filename) throws IOException {

        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream("D:/Systems/"+filename);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 图片转字符串
     * @param filePath    --->文件路径
     * @return
     */
    public static String getImageStr(String filePath) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(filePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /*
     * 测试代码
     */
    public static void main(String[] args) throws IOException {
        String imageStr = getImageStr("D:\\001.jpg");
        System.out.println(imageStr);
        boolean generateImage = generateImage(imageStr, "001.jpg");
        System.out.println(generateImage);
    }
}
