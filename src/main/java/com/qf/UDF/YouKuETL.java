package com.qf.UDF;

import org.apache.hadoop.hive.ql.exec.UDF;

public class YouKuETL extends UDF {
    public String evaluate(String value){
        final String[] split = value.split("\t");

        if(split.length<10) return  "";

        final String category = split[3].replaceAll(" ", "");

        String relatedIds = "";
        for(int i=9;i<split.length;i++){
            if(i!=split.length-1){
                relatedIds = relatedIds+split[i]+"&";
            }else{
                relatedIds = relatedIds + split[i];
            }
        }

        return  split[0]+"\t"+split[1]+"\t"+split[2]+"\t"+category+"\t"+split[4]+"\t"+split[5]+"\t"+
                split[6]+"\t"+split[7]+"\t"+split[8]+"\t"+relatedIds;
    }
}
