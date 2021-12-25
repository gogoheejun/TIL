package inflearn_java.dfsAndBfsUse;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class DFS01_합이같은부분집합 {
    static String answer = "NO";
    static int n, total = 0;
    boolean flag = false;
    public void DFS(int L, int sum, int[] arr){
        if(flag) return; //이게 포인트임

        if(L==n){
            if((total-sum) == sum){
                answer = "YES";
                flag = true;
            }
        }else{
            DFS(L+1, sum+arr[L], arr);
            DFS(L+1, sum, arr);
        }
    }
    public static void main(String[] args) {
        DFS01_합이같은부분집합 T = new DFS01_합이같은부분집합();
        Scanner kb = new Scanner(System.in);
        n = kb.nextInt();
        int[] arr = new int[n];

        for(int i=0; i<n; i++){
            arr[i] = kb.nextInt();
            total +=arr[i];
        }
        T.DFS(0,0,arr);
        System.out.println(answer);
    }
}
