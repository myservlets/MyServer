package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.RecruitmentDAO;
import entity.Recruitment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.UUID;

@WebServlet(name = "RecruitmentServlet")
public class RecruitmentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        System.out.println("recruitment:");
        Recruitment recruitment = new Recruitment();
        try(PrintWriter out = response.getWriter()){
            BufferedReader bf= request.getReader();
            String json = bf.readLine();
            System.out.println(json);
            bf.close();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            int sign = Integer.parseInt(jsonObject.get("sign").toString());
            recruitment = gson.fromJson(jsonObject.get("recruitment"),Recruitment.class);
            switch (sign){
                case 0://insert插入新的兼职项
                    recruitment.setRecruId(UUID.randomUUID().toString());
                    recruitment = RecruitmentDAO.insertRecru(recruitment);
                    break;
                case 1://update修改兼职项
                    recruitment=RecruitmentDAO.updateRecru(recruitment);
                    break;
                case 2://delete
                    recruitment=RecruitmentDAO.deleteRecru(recruitment);
                    break;
                case 3://query
                    break;
                case 4://模糊搜索
                    out.write(gson.toJson(RecruitmentDAO.fuzzySearchRecru("兼职")));
                    return;
            }


            out.write(gson.toJson(recruitment));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
