package inflearn_java.dfsAndBfsUse;

import java.util.Scanner;

//n: 1~n
//m: L깊이
//1~n 까지의 숫자를 m번 뽑을 때 경우의 수구하기(카드뽑기처럼 중복안됨)
public class DFS09_조합구하기 {
    static int[] combi;
    static int n, m;

    public void DFS(int L, int s){ //s: start
        if(L==m){
            for(int x: combi) System.out.print(x+" ");
            System.out.println();
        }else{
            for(int i=s; i<n; i++){ //1부터 시작함 어차피
                combi[L] = i;//L에 계속 덮어씌우는거임
                DFS(L+1, i+1);
            }
        }
    }


    public static void main(String[] args) {
        DFS09_조합구하기 T = new DFS09_조합구하기();
        Scanner kb = new Scanner(System.in);
        n = kb.nextInt();
        m = kb.nextInt();
        combi = new int[m];
        T.DFS(0,1);
    }
}
