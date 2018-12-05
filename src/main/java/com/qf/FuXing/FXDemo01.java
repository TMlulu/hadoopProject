package com.qf.FuXing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/*
泛型只在编译期有效，在运行期会被擦除掉
不在编译期 所以泛型无效
 */
public class FXDemo01 {
    public static void main(String[] args) throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);

        Class<?> clazz = Class.forName("java.util.ArrayList");
        //通过反射实例化一个类
        final Object obj = clazz.newInstance();

        //获取方法  (方法名，类型)
        final Method method = clazz.getMethod("add",Object.class);

        //调用方法，对于某个实例化对象add方法， aaa是参数
        method.invoke(obj,"aaa");

        //对于list调用add方法，参数是bbb
        //不在编译期 所以泛型无效
        method.invoke(list,"bbb");

        System.out.println(obj);
        System.out.println(list);
    }
}
