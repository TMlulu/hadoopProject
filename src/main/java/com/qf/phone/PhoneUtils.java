package com.qf.phone;

import javax.xml.crypto.Data;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.stream.IntStream;

public class PhoneUtils {
    public static void main(String[] args) {
        getStartTime();
    }
    public static String getStartTime(){
        final Random random = new Random();
        //final IntStream ints = random.ints(2016, 2018);
        final int a = (int) (Math.random() * 2);

        final int b = (int) (Math.random() * 9);
       // System.out.println(v);
        int c = (int) (Math.random() * 12)+1;
        final DecimalFormat df = new DecimalFormat("00");


        String tm =  2+""+0+""+a+""+b+"-"+df.format(c)+"-"+df.format((int) (Math.random() * 31))+" "+df.format((int) (Math.random() * 25))+":"+df.format((int) (Math.random() * 60))+":"+df.format((int) (Math.random() * 31));
        //System.out.println(tm);
        return  tm;

    }

    public static String call_Time(){


/*        ? ? ? String a="1";
?DecimalFormat format=new DecimalFormat("000000");//设置格式
? ? ?String str=df.format(Integer.parseInt(a));//格式转换
? ? ?System.out.println(str);
?//数字1是整型，如下处理：
? ? ? ? ?int a=1;
?DecimalFormat df=new DecimalFormat("000000");//设置格式
? ? ?String str=df.format(a);//格式转换
? ? ?System.out.println(str);
        */
        long time = (long)( Math.random()*3601);
         //String a ="1";
        DecimalFormat format = new DecimalFormat("0000");
        //Integer.parseInt(a)
        String str =  format.format(time);//参数是整数， 如果不是整数就是用 Integer.parseInt()
        //最后结果的格式都是 0002 0236 1023 5226 等
        return str;
    }
}
