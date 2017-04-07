package senclassify.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zhangzhiyong on 17-3-31.
 */

public class FRead {
    public BufferedReader br;
    public FRead(String filename){
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
        }
        catch (IOException e){
            System.out.println("No such file");
        }
    }

    public String readLine(){
        try{
            return br.readLine();
        }catch (IOException e){
            System.out.println("read error !!!");
            return null;
        }
    }

    public void close(){
        try{
            br.close();
        }catch (IOException e){
            System.out.println("error!!");
        }
    }
}
