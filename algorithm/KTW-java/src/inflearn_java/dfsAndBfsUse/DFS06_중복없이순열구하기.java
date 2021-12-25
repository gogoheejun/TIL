package inflearn_java.dfsAndBfsUse;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

//DFS 어렵다. 꼭 다시 해보자.프로그래머스에서 연습 다시 해보기
//내림차순과 answer값 체크해서 return시키기 통해서 시간 단축하기
public class DFS06_중복없이순열구하기 {
    static int[] pm, ch, arr;
    static int n, m;
    public void DFS(int L){
        if(L == m){
            for(int x : pm) System.out.print(x+ " ");
            System.out.println();
        }else{
            for(int i=0; i<n; i++){
                if(ch[i] ==0){
                    ch[i] = 1;
                    pm[L] = arr[i];
                    DFS(L+1);
                    ch[i] = 0;
                }
            }
        }
    }


    public static void main(String[] args) {
        DFS06_중복없이순열구하기 T = new DFS06_중복없이순열구하기();
        Scanner kb = new Scanner(System.in);
        n = kb.nextInt();
        m = kb.nextInt();
        arr = new int[n];
        for(int i=0; i<n; i++) arr[i] = kb.nextInt();
        ch = new int[n];
        pm = new int[m];
        T.DFS(0);
    }
}
