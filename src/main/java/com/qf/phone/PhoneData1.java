package com.qf.phone;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneData1 {
    public static void main(String[] args) {
        List<String> pn = new ArrayList<>();
        Map<String,String> personAndPhon = new HashMap<>();
        BufferedReader brPho = null;
        BufferedReader brPerson = null;
        BufferedWriter bw = null;
        try {
            //参数 手机号 姓名   日志文件
            bw = new BufferedWriter(new FileWriter(args[2],true));
            brPho = new BufferedReader(new FileReader(args[0]));
            brPerson = new BufferedReader(new FileReader(args[1]));
            String person = "";
            while((person=brPerson.readLine())!=null){
               String phon =  brPho.readLine();
               person = new String(person.getBytes(),"utf-8");
               pn.add(phon);
               personAndPhon.put(phon,person);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                brPerson.close();
                brPho.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        while(true){

            int s = (int)(Math.random()*pn.size());
            int s1 = (int)(Math.random()*pn.size());
            while(s==s1){
                s1 = (int)(Math.random()*pn.size());
            }

            final String phone1 = pn.get(s);
            final String phone2 = pn.get(s1);

            final String persion1 = personAndPhon.get(phone1);
            final String persion2 = personAndPhon.get(phone2);

            final String startTime = PhoneUtils.getStartTime();
            final String callTime = PhoneUtils.call_Time();
            String value = phone1+"\t"+persion1+"\t"+phone2+"\t"+persion2+"\t"+startTime+"\t"+callTime;
            try{

                bw.write(value);
                bw.newLine();
                bw.flush();
            }catch (Exception e){

            }finally {

            }

            System.out.println(phone1+"\t"+persion1+"\t"+phone2+"\t"+persion2+"\t"+startTime+"\t"+callTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
