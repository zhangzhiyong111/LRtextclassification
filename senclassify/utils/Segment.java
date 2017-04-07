package senclassify.utils;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by zhangzhiyong on 17-3-31.
 */

public class Segment {
    public static List<String> segSentence(String sentence){
        List<String> result = new ArrayList<String>();
        for(Term term: BaseAnalysis.parse(sentence)){
            result.add(term.getName());
        }
        return result;
    }

    public static HashSet<String> getStopwords(String stopwordPath){
        HashSet<String> stopwords = new HashSet<String>();
        FRead fr = new FRead(stopwordPath);
        String word = "";
        while((word = fr.readLine()) != null){
            stopwords.add(word);
        }
        return stopwords;
    }


    //主要是为了读取计算出来的特征，返回值是一个map,存放的是这个值的索引和信息熵值
    public static HashMap<String, List<String>> getWordMap(String filePath){
        FRead fr = new FRead(filePath);
        String line = "";
        HashMap<String, List<String>> wordMap = new HashMap<String, List<String>>();

        int i = 0;
        while((line = fr.readLine()) != null){
            String[] fields = line.trim().split("\t");
            if(fields.length != 2){
                System.out.println("read the feature selection existed line that split is not equal to 2 !!! ");
            } else {
                String word = fields[0];
                List<String> temp = new ArrayList<String>();
                temp.add(String.valueOf(i));
                temp.add(fields[1]);

                wordMap.put(word, temp);
                i ++;
            }
        }
        return wordMap;
    }

    public static void main(String[] args){
        List<String> result = segSentence("你是一个好人");
        for(String word: result){
            System.out.println(word);
        }
    }
}
