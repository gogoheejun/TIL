package inflearn_java.dfsAndBfsUse;

import java.util.Scanner;

//{1,2,3,4,5}에서 5C3을 구한다면 이는 곧 4C2+4C3을 구하는것과 같다
//ex) (원소 1을 포함시키고 나머지 2개 뽑는 조합) + (1포함안하고 3개 뽑는조합)
public class DFS07_조합기초 {
    int[][] dy = new int[35][35];//문제에서 n, r이 총 33까지라 여유있게 35

    public int DFS(int n, int r){
        if(dy[n][r] > 0) return dy[n][r];//이미 조합구한 표에 있으니까 굳이 가지치기 또 안해도 됨
        if(n==r || r==0) return 1;
        else return dy[n][r] = DFS(n-1, r-1) + DFS(n-1, r);
    }


    public static void main(String[] args) {
        DFS07_조합기초 T = new DFS07_조합기초();
        Scanner kb = new Scanner(System.in);
        int n = kb.nextInt();
        int r = kb.nextInt();
        System.out.println(T.DFS(n,r));
    }
}
