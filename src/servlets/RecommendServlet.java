package servlets;

import entity.Score;
import utils.DataProcess;
import utils.FileTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "RecommendServlet")
public class RecommendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                String inputDir = "data/fresh_comp_offline/";
                String outputDir = "data/fresh_comp_offline/sample/";
                String inputPath = "data/fresh_comp_offline/sample/";
                String outputPath = "data/fresh_comp_offline/sample/out/";
//                String inputDir = args[0];
//                String outputDir = args[1];
                String userPath = inputDir + "tianchi_fresh_comp_train_user.csv";
//                String inputPath = inputDir + args[2];
//                String outputPath = inputDir + args[3];
                String userDir = "data/fresh_comp_offline/sample/user/";

        Map<String, List<Score>> scoreMap = null;
        try {
            scoreMap = FileTool.loadScoreMap(inputPath, false, "\t");
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataProcess.sortScoreMap(scoreMap);
                List<String> fileNameList = FileTool.traverseFolder(userDir);
                //我选择推荐该user的最相似的5个user的前5个item
        Map<String, Set<String>> predictMap = null;
        try {
            predictMap = DataProcess.predict(scoreMap, fileNameList, userDir, 5, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileTool.initWriter1(outputPath);
                DataProcess.outputRecommendList(predictMap);
                FileTool.closeWriter1();
                scoreMap.clear();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
