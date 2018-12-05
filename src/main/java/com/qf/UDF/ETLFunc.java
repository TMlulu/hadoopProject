package com.qf.UDF;

import org.apache.hadoop.hive.ql.exec.UDF;

public class ETLFunc extends UDF{
    private int a = 0;
   /* create table house_t(name string,link string,zone string,huxing string,area string ,Orientation string,Renovation string
            ,Elevator string,price int,sumprice int) row format delimited fields terminated by '\t';*/
    public String evaluate(String name,String link,String zone,String huxing,String area,
                           String Orientation ,String Renovation,String Elevator,String price,String sumprice ){

        //天通苑北一区 3室2厅 510万	  1.01101E+11	天通苑北一区	3-2厅	143.09 平米	南北	简装	有电梯	35642	510
        /**
         * 处理后的数据如下：
         处理规则：
         1、过滤，整行数据完全相同的只留一行即可。
         2、第1列数据某些字段需要加"[]"（自己结合分析前后观察规律）
         3、第2列数据不需要。
         4、将第4列中的"-"替换成"室"。
         5、将第5列数据中的空格去掉。
         5、将6、7、8列改成如下格式。
         6、将10列数据中值小于300的排除掉整行数据，并将剩下的第10列数据后面加上单位"万"。
         7、清洗完后的第1列和第2列用Tab键分隔，第2、3、4、5、6、7列分别用空格分隔。
         8、全部按规则输出，并且输出文件中第一行是抬头。
         天通苑北一区 [3室2厅 510万]
         com.qf.UDF.ETLFunc
         */
        String result = "";
        if(a!=0){
            String newName = etl(name);
            String newHuxiang = huxing.replace("-","室");
            String newArea = area.replaceAll(" ", "");
            String ted = Orientation +"-"+ Renovation +"-"+Elevator;
           result=  newName+ "\t" +zone+"\t"+newHuxiang+"\t"+newArea+"\t"+ ted +"\t"+price+"\t"+sumprice+"万";
        }

        else{
            String newName = etl(name);
            String newHuxiang = huxing.replace("-","室");
            String newArea = area.replaceAll(" ", "");
            String ted = Orientation +"-"+ Renovation +"-"+Elevator;
           // result=  newName+ "\t" +zone+"\t"+newHuxiang+"\t"+newArea+"\t"+ ted +"\t"+price+"\t"+sumprice+"万";
           result= "name"+"\t"+"zone"+"\t"+"huxing"+"\t"+"area"+"\t"+
                   "tedian"+"\t"+"price"+"\t"+"sumprice"+"\n"+
                    newName+ "\t" +zone+"\t"+newHuxiang+"\t"+newArea+"\t"+ ted +"\t"+price+"\t"+sumprice+"万";
        }
        a++;
        return result;
    }

    public String etl(String name){
        final String[] split = name.split(" ");
        String sv = "";
        for(int j=1;j<split.length;j++){
            if(j==1){
                sv = sv + "["+ split[j]+ " ";
            }else if(j==split.length-1){
                sv = sv + split[j] +"]";
            }else{
                sv = sv + split[j]+" ";
            }
        }
        //split[0]+"["+split[1]+" "+split[2]+"]";

        return split[0]+" "+sv;

    }


}
