package inflearn_java.twoPointer;

import inflearn_java.sorting.Sotring02_버블정렬;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class TwoPointer_01배열합치기 {
    public ArrayList<Integer> solution(int n, int m, int[] a, int[] b){

        int p1 = 0;
        int p2 = 0;
        ArrayList<Integer> answer = new ArrayList<>();
        while(p1 < n && p2 <m){
            if(a[p1] > b[p2]){
                answer.add(a[p1]);
                p1++;
            }else{
                answer.add(a[p2]);
                p2++;
            }
        }
        while(p1<n) answer.add(a[p1++]);
        while(p2<m) answer.add(b[p2++]);
        return answer;

    }

    public static void main(String[] args) {
        TwoPointer_01배열합치기 T = new TwoPointer_01배열합치기();
        Scanner kb = new Scanner(System.in);
        int n = kb.nextInt();
        int[] a = new int[n];
        for(int i=0; i<n; i++){
            a[i] = kb.nextInt();
        }
        int m = kb.nextInt();
        int[] b = new int[m];
        for(int i=0; i<n; i++){
            b[i] = kb.nextInt();
        }
        for(int x: T.solution(n,m,a,b)) System.out.println(x +" ");
    }
}

