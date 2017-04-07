package senclassify.classify;

/**
 * Created by zhangzhiyong on 17-4-5.
 */

import senclassify.utils.FRead;
import senclassify.utils.Segment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FeatureSelection {
    //统计每个词以及它们之间的类别关系
    private static HashMap<String, Integer> postiveWordsCount = new HashMap<String, Integer>();
    private static HashMap<String, Integer> negativeWordsCount = new HashMap<String, Integer>();


    //获取每个词以及他对应的重要程度
    private static HashMap<String, Double> wordIG = new HashMap<String, Double>();

    //统计每个词以及它们在不同标签的次数
    public static void getWordInfor(String sentence, Double label, HashSet<String> stopwords){
        Set<String> result = new HashSet<String>(Segment.segSentence(sentence));
        if(label == 1.0){
            for(String word: result){
                if(postiveWordsCount.containsKey(word)){
                    postiveWordsCount.put(word, postiveWordsCount.get(word) + 1);
                } else {
                    postiveWordsCount.put(word, 1);
                }

            }
        }else if(label == -1.0){
            for(String word: result){
                if(negativeWordsCount.containsKey(word)){
                    negativeWordsCount.put(word, negativeWordsCount.get(word) + 1);
                } else {
                    negativeWordsCount.put(word, 1);
                }
            }
        }
    }

    //计算该词的信息熵
    public static Double calInforGain(int positiveNum, int negativeNum, int totalNum){
        double positiveDouble = positiveNum * 1.0 / totalNum;
        double negativeDouble = negativeNum * 1.0 / totalNum;

        double positiveValue = 0.0;
        double negativeValue = 0.0;

        if(positiveDouble != 0)
            positiveValue = (- 1) * Math.log(positiveDouble) * positiveDouble;
        if(negativeDouble != 0)
            negativeValue = (- 1) * Math.log(negativeDouble) * negativeDouble;

        return positiveValue + negativeValue;
    }

    //根据正负标签信息来计算每个词和它对应的信息上值
    public static void calIG(){
        Set<String> positiveWords = postiveWordsCount.keySet();
        Set<String> negativeWords = negativeWordsCount.keySet();

        //获取所有词的集合
        Set<String> vocal = new HashSet<String>();
        vocal.addAll(positiveWords);
        vocal.addAll(negativeWords);

        //计算不同类别标签的长度
        int positiveLen = postiveWordsCount.size();
        int negativeLen = negativeWordsCount.size();

        for(String word: vocal){
            int postiveNum = 0;
            int negativeNum = 0;
            int totalNum = 0;
            //获取正标签的个数
            if(postiveWordsCount.containsKey(word))
                postiveNum = postiveWordsCount.get(word);
            //获取负标签的个数
            if(negativeWordsCount.containsKey(word))
                negativeNum = negativeWordsCount.get(word);
            totalNum = negativeNum + postiveNum;

            if(totalNum < 4)
                continue;

            Double wordIGValue = calInforGain(postiveNum, negativeNum, totalNum);
            wordIG.put(word, wordIGValue + 1.01);
        }

    }

    //将此对应的信息熵写入到文件中去
    public static void writeToFile(String writefilePath) throws IOException {
        //按照信息熵的值进行排序，并选择其中的500个词作为选择依据
        List<Map.Entry<String, Double>> mapList = new ArrayList<Map.Entry<String, Double>>(wordIG.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2){
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(writefilePath)));
        int i = 0;
        for(Map.Entry<String, Double> entry: mapList){
            i ++;
            if(i > 500)
                break;
            bufferedWriter.write(entry.getKey() + "\t" + entry.getValue() + "\n");
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static void getImportWord(String trainfilePath, String writefilePath, String stopwordsPath) throws IOException {
        HashSet<String> stopwords = Segment.getStopwords(stopwordsPath);

        FRead fr = new FRead(trainfilePath);
        String line = "";
        while((line = fr.readLine()) != null){
            String[] fields = line.trim().split(",");
            if(fields.length != 2){
                continue;
            } else {
                Double label = Double.valueOf(fields[0]);
                String sentence = fields[1];
                getWordInfor(sentence,label, stopwords);

            }
        }
        calIG();
        writeToFile(writefilePath);
    }

    public static void main(String[] args) throws IOException {
        String trainfilePath = "/home/zhangzhiyong/IdeaDataFile/train.txt";
        String writefilePath = "/home/zhangzhiyong/IdeaDataFile/tt";
        String stopwordsPath = "/home/zhangzhiyong/IdeaDataFile/stopwords.txt";

        getImportWord(trainfilePath, writefilePath, stopwordsPath);

    }

}
