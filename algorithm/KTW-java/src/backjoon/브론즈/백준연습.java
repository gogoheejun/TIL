package backjoon.브론즈;

import java.util.Scanner;

public class 백준연습 {
    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);
        int a = kb.nextInt();
        int b = kb.nextInt();

        System.out.println(solution(a,b));

        kb.close();
    }

    static int solution(int a, int b){
        return a-b;
    }
}
