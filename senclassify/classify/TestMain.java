package senclassify.classify;

/**
 * Created by zhangzhiyong on 17-3-31.
 */

import senclassify.utils.FRead;
import senclassify.utils.Segment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TestMain {

    public static void main(String[] args){
        String traindataPath = "/home/zhangzhiyong/IdeaDataFile/train.txt";
        String testdataPath = "/home/zhangzhiyong/IdeaDataFile/test.txt";
        String filePath = "/home/zhangzhiyong/IdeaDataFile/tt";
        String stopwordsPath = "/home/zhangzhiyong/IdeaDataFile/stopwords.txt";

        LogisticRegresion lr = new LogisticRegresion();

        HashMap<String, List<String>> wordMap = Segment.getWordMap(filePath);
        System.out.println("read the wordMap finished !!!");

        HashSet<String> stopwords = Segment.getStopwords(stopwordsPath);
        System.out.println("read the stopwords finished !!!");

        //对数据进行预处理
        Process.traindataToFormat(traindataPath, stopwords, wordMap);
        System.out.println("preprocess the data finished !!!");


        double[][] xx = Process.X;
        double[] yy = Process.Y;
        System.out.println("the number of sample is:" + xx.length + "the dim is:" + xx[0].length);

        //训练数据集，使得模型
        lr.train(Process.X, Process.Y);
        System.out.println("train the model finished !!!");

        FRead fr = new FRead(testdataPath);
        String line = "";

        while((line = fr.readLine()) != null){
            String[] fields = line.trim().split(",", 2);

            if(fields.length == 2){
                Double label = Double.valueOf(fields[0]);
                String content = fields[1];

                if(label == 1.0)
                    System.out.print("The true label is : " + label + "   ");
                else if(label == -1.0)
                    System.out.print("The true label is : " + 0.0 + "    ");
                else
                    continue;

                double[] vector = Process.senToVector(content, stopwords, wordMap);
                double preLabel = lr.predicted(vector);
                System.out.println(" The predicted label is :" + preLabel);
            }
        }

    }
}
