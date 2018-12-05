package com.qf.FuXing;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FXDemo02 {
    public static void main(String[] args) throws Exception {
        //泛型可以不用new  的方式 而获取 类的方法 属性 实例化对象


        //第一种
        final Student stu = new Student();
        final Class<? extends Student> aClass = stu.getClass();

        //第二种
        final Class<Student> stuClass = Student.class;

        //第三种
        //类的全路径
        final Class<?> clazz = Class.forName("com.qf.FuXing.Student");
       //获取无参构造
        final Object obj1 = clazz.newInstance();

        //获取私有构造参数
        final Constructor<?> ct = clazz.getDeclaredConstructor(String.class);
        ct.setAccessible(true);


        final Object lisi = ct.newInstance("lisi");

        //获取带参构造
        final Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
        final Constructor<?> constructor1 = clazz.getConstructor(new Class[]{String.class,int.class});
        final Object obj = constructor1.newInstance("zhangsan", 26);
        //获取公共有参有返回值的方法
        final Method addMethod = clazz.getMethod("add", int.class, int.class);
        final int addResult = (int)addMethod.invoke(obj, 1, 2);


        //获取私有的方法
        final Method subMethod = clazz.getDeclaredMethod("sub", int.class, int.class);
       //调用私有属性需要设置为true
        subMethod.setAccessible(true);

        final int subResult = (int)subMethod.invoke(obj, 1, 2);
        System.out.println(subResult);

        System.out.println(addResult);
        System.out.println(obj);

        final Field name = clazz.getField("name");
        //获取obj对象的 name属性
        System.out.println(name.get(obj));

        //获取私有的属性
        final Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        System.out.println(age.get(obj));

    }
}

class Student{
    public  String name ;
    private  int age;
    public Student(){}

    private Student(String name){
        this.name = name;
    }
    public Student(String name,int age){
       this.name = name;
       this.age = age;
    }
    public int add(int a,int b){
        return  a+b;
    }

    private int sub(int a,int b){
        return  a-b;
    }

    @Override
    public String toString() {
        return name+"\t"+age;
    }
}
