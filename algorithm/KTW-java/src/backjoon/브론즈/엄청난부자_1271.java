package backjoon.브론즈;

import java.math.BigInteger;
import java.util.Scanner;

public class 엄청난부자_1271 {
    public BigInteger[] solution(BigInteger a, BigInteger b){
        BigInteger[] answer = new BigInteger[2];
        answer[0] = a.divide(b);
        answer[1] = a.mod(b);
        return answer;
    }

    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);
        BigInteger a = kb.nextBigInteger();
        BigInteger b = kb.nextBigInteger();


        엄청난부자_1271 T = new 엄청난부자_1271();
        System.out.println(T.solution(a,b)[0]);
        System.out.println(T.solution(a,b)[1]);

        kb.close();
    }
}
