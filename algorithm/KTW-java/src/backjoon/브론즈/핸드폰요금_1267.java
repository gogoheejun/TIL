package backjoon.브론즈;


import java.util.Scanner;

public class 핸드폰요금_1267 {
    public void solution(int n, int[] times) {

        int yPrice = getPrice(n, times, 30);
        int mPrice = getPrice(n, times, 60);

        if (yPrice > mPrice) {
            System.out.println("M " + mPrice);
        } else if (mPrice > yPrice) {
            System.out.println("Y " + yPrice);
        }else{
            System.out.println("Y M "+ yPrice);
        }
    }

    private int getPrice(int n, int[] times, int sec) {

        int multi = (sec == 30)? 10: 15;

        int totalPrice = 0;
        for(int time : times){
            int p = (time/sec + 1)*multi;
            totalPrice += p;
        }
        return totalPrice;
    }


    public static void main(String[] args) {
        핸드폰요금_1267 T = new 핸드폰요금_1267();
        Scanner kb = new Scanner(System.in);
        int n = kb.nextInt();
        int[] times = new int[n];
        for(int i=0; i<times.length; i++){
            times[i] = kb.nextInt();
        }
        kb.close();

        T.solution( n, times);
    }
}
