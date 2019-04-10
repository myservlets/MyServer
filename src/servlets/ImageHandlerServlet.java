package servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@WebServlet(name = "ImageHandlerServlet")
public class ImageHandlerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //创建配置工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 2. 根据配置工厂创建解析请求中文件上传内容的解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 3. 判断当前请求是不是多段提交
        if (!upload.isMultipartContent(request)) {
            throw new RuntimeException("不是多段提交！");
        }
        try {
            // 4. 解析request对象，将已经分割过的内容放进了List
            List<FileItem> list = upload.parseRequest(request);
            if (list != null) {
                String userId="";
                for (FileItem fileItem : list) {
                    // 判断当前段是普通字段还是文件,这个方法是判断普通段
                    if (fileItem.isFormField()) {
                        // 获得name属性对应的值,这里是username
                        String fname = fileItem.getFieldName();
                        // 获得键对应的值
                        userId = fileItem.getString("utf-8");
                        System.out.println(fname +  "=>"+userId );
                        // 否则就是文件了
                    } else {
                        if(userId.isEmpty()){
                            break;
                        }
                        // 获得文件上传段中，文件的流
                        InputStream in = fileItem.getInputStream();
                        String str1 = fileItem.getName();


                        // 使用用户上传的文件名来保存文件的话，文件名可能重复。
                        // 所以保存文件之前，要保证文件名不会重复。使用UUID生成随机字符串
                        String fileName = "icon"+str1.substring(str1.lastIndexOf("."));

                                //UUID.randomUUID().toString()+str1.substring(str1.lastIndexOf("."));
                        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("/yyyy/MM/dd/");
                        //String datePath = simpleDateFormat.format(new Date()); // 解析成    /2017/04/15/  的样子, 注意这是三个文件夹
                        String wholePath = "E:/upload/"+userId+"/";
                        // 字节输出流，用以保存文件，也不需要后缀名，因为我们只是保存用户的数据，不需要查看他们的数据。待用户想下载的时候，再加上后缀名
                        File dir = new File(wholePath);
                        // mkdirs可以建立多级目录。即使所有层级的目录都不存在。这些文件夹都会创建,比如我们事先并没有创建在D盘创建upload和2017等这些文件夹
                        // mkdir只能用于父级目录已经存在的情况下使用，在已存在的父级目录下再新建一级。只能一级！比如File("D:\\upload\\2017\\04")。且D:\\upload\\2017是已经存在的。父级 目录存且只新建一级。故file.makedir()返回true成功创建。
                        // 但是File("D:\\upload\\2017\\04\\15")且D:\\upload\\2017存在，但不存在15文件夹。因为父级目录不存在所以创建失败返回false
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        FileOutputStream fos = new FileOutputStream(wholePath+fileName);
                        // 将输入流复制到输出流中
                        IOUtils.copy(in, fos);
                        fos.close();
                        deleteLastIcon();
                        updateDB();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        }

    private void updateDB() {
    }

    private void deleteLastIcon() {
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
