package com.lucloud.utils;

public class MathRandom {

    public static Integer random() {
        double randomNumber;
        randomNumber = Math.random();
        double rate1 = 0.015;
        double rate2 = 0.10;
        if (randomNumber >= 0 && randomNumber <= rate1) {
            return 1;
        } else if (randomNumber >= rate1 / 100 && randomNumber <= rate1 + rate2) {
            return 2;
        }
        return 0;
    }

    /**
     * 测试主程序
     */
    public static void main(String[] agrs) {
        int i = 0;
        int k = 0;
        for (i = 0; i <= 10000; i++){
            int b = random();
            if(b==1){
                k++;
            }
        }
        System.out.println(k);
    }
}
