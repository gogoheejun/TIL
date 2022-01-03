package inflearn_java.sorting;

import java.util.Scanner;

public class Sotring02_버블정렬 {
    public int[] solution(int n, int[] arr){
        for(int i=0; i<n; i++){
            for(int j=0; j<n-1-i; j++){
                if(arr[j] > arr[i]){
                    int tmp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        Sotring02_버블정렬 T = new Sotring02_버블정렬();
        Scanner kb = new Scanner(System.in);
        int n = kb.nextInt();
        int[] arr = new int[n];
        for(int i=0; i<n; i++) arr[i] = kb.nextInt();
        for(int x : T.solution(n,arr)) System.out.println(x + "");
    }
}
