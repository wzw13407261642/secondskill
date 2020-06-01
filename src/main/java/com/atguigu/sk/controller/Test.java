package com.atguigu.sk.controller;

public class Test {

    public static void main(String[] args) {
           Object a=12L;
           int b= (int)(long) a;
    }

}
class Base{
    private static String baseName="base";
    public Base(){
        callName();
    }
    public void callName(){
        System.out.println(baseName);
    }
    public static void main(String[] args) {
           Base b=new Sub();
    }
}

class Sub extends Base{
    private static String baseName="sub";
    private int id;

    public Sub() {
        super();
        this.baseName="wzw";
    }

    public void callName(){
        System.out.println(baseName);
    }
}