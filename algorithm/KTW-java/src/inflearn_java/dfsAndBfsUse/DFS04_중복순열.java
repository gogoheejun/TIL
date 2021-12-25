package inflearn_java.dfsAndBfsUse;

import java.util.Scanner;
//이 문제부터는 이진트리가 아님.
public class DFS04_중복순열 {
    static int[] pm;
    static int n, m;
    public  void DFS(int L){
        if(L == m){
            for(int x : pm) System.out.println(x + " ");
            System.out.println();
        }else{
            for(int i=1; i<=n; i++){
                pm[L] = i;
                DFS(L+1);
            }
        }
    }
    public static void main(String[] args) {
        DFS04_중복순열 T = new DFS04_중복순열();
        Scanner kb = new Scanner(System.in);
        n = kb.nextInt();
        m = kb.nextInt();
        pm = new int[m];
        T.DFS(0);
    }
}
