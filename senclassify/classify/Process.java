package senclassify.classify;

import senclassify.utils.FRead;
import senclassify.utils.Segment;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangzhiyong on 17-4-6.
 */

public class Process {
    public static double[][] X;
    public static double[] Y;

    //主要是将句子转换成向量来进行表示
    /*@para segSentence : the sentence segment into words
    *@para  stopwords : the stopwords
    *@para  wordMap : the word and corresponding index and IG value
    * @return : the corresponding logistic value
     */
    public static double[] senToVector(String content, HashSet<String> stopwords, HashMap<String, List<String>> wordMap){
        //将句子进行分词处理
        List<String> segSentence = Segment.segSentence(content);

        //计算逻辑回归的维度
        int dim = wordMap.size();

        //计算一下逻辑回归的维度并且进行初始化为0
        double[] logVector = new double[dim];
        for(int i = 0; i < dim; i ++){
            logVector[i] = 0.0;
        }

        //采用０-１特征表示方法，将列表转换成集合的方式
        Set<String> segSen = new HashSet<String>(segSentence);

        //对每个词进行操作获取wordMap中对应的信息熵的值
        for(String word: segSen){
            if(stopwords.contains(word))
                continue;
            else{
                if(wordMap.containsKey(word)){
                    List<String> temp = wordMap.get(word);
                    if(temp.size() == 2){
                        int index = Integer.valueOf(temp.get(0));
                        double inforGain = Double.valueOf(temp.get(1));
                        logVector[index] = inforGain;
                    }
                }
            }
        }
        return logVector;
    }
   
    //主要是将训练集转换成数组类型的
    public static void traindataToFormat(String trainfilePath, HashSet<String> stopwords, HashMap<String, List<String>> wordMap){
        List<double[]> tempX = new ArrayList<double[]>();
        List<Double> tempY = new ArrayList<Double>();

        FRead fr = new FRead(trainfilePath);
        String line = "";

        while((line = fr.readLine()) != null){
            String[] fields = line.trim().split(",", 2);  //一定要注意设置分割符号

            if(fields.length != 2)
                continue;
            Double label = Double.valueOf(fields[0]);
            String content = fields[1];

            if(label == 1.0)
                label = 1.0;
            else if(label == -1.0)
                label = 0.0;
            else
                continue;

            double[] vector = senToVector(content, stopwords, wordMap);
            tempX.add(vector);
            tempY.add(label);
        }

//        for(int i = 0; i < tempX.size(); i ++){
//            double[] dd = tempX.get(i);
//            for(int j = 0; j < dd.length; j ++)
//                System.out.print(dd[i] + " ");
//            System.out.println();
//        }

        int len = tempX.size();
        X = new double[len][wordMap.size()];
        Y = new double[len];

        for(int i = 0; i < len; i ++){
            X[i] = tempX.get(i);
            System.out.print(X[i] + " ");
            Y[i] = tempY.get(i);
            System.out.println(Y[i]);
        }

    }

    public static void main(String[] args){
        System.out.println("hello world");
    }

}
